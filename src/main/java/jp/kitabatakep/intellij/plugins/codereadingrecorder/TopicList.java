package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.messages.MessageBus;
import org.jdom.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

public class TopicList
{
    private Project project;
    private ArrayList<Topic> topics = new ArrayList<>();
    private Integer nextTopicId = 1;

    public TopicList(Project project)
    {
        this.project = project;
    }

    public void addTopic(String name)
    {
        Topic topic = new Topic(project, nextTopicId, name, new Date());
        topics.add(topic);
        nextTopicId++;

        MessageBus messageBus = project.getMessageBus();
        TopicListNotifier publisher = messageBus.syncPublisher(TopicListNotifier.TOPIC_LIST_NOTIFIER_TOPIC);
        publisher.topicAdded(topic);
    }

    public void deleteTopic(Topic topic)
    {
        topics.remove(topic);
        MessageBus messageBus = project.getMessageBus();
        TopicListNotifier publisher = messageBus.syncPublisher(TopicListNotifier.TOPIC_LIST_NOTIFIER_TOPIC);
        publisher.topicDeleted(topic);
    }

    public Iterator<Topic> iterator()
    {
        Collections.sort(topics);
        return topics.iterator();
    }

    public void clearTopicList()
    {
        topics.clear();
    }


    public void loadState(Element element)
    {
        Element topicsElement = element.getChild("topics");
        for (Element topicElement : topicsElement.getChildren("topic")) {
            int id = Integer.valueOf(topicElement.getAttributeValue("id")).intValue();
            String name = topicElement.getAttributeValue("name");
            String updatedAtString = topicElement.getAttributeValue("updatedAt");

            Topic topic;
            try {
                topic = new Topic(project, id, name, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(updatedAtString));
            } catch (ParseException e) {
                Logger.getInstance(AppConstants.appName).error(e.getMessage());
                continue;
            }

            topic.setMemo(topicElement.getAttributeValue("memo"));

            Element topicLinesElement = topicElement.getChild("topicLines");
            for (Element topicLineElement : topicLinesElement.getChildren("topicLine")) {
                String url = topicLineElement.getAttributeValue("url");
                String lineString = topicLineElement.getAttributeValue("line");
                int line = Integer.parseInt(lineString);
                VirtualFile file = VirtualFileManager.getInstance().findFileByUrl(url);

                TopicLine topicLine = new TopicLine(project, file, line);
                topicLine.setOrder(Integer.parseInt(topicLineElement.getAttributeValue("order")));
                topicLine.setMemo(topicLineElement.getAttributeValue("memo"));

                topic.addLine(topicLine, false);
            }
            topics.add(topic);
        }
        Collections.sort(topics);

        Element stateElement = element.getChild("state");
        if (stateElement != null) {
            nextTopicId = Integer.valueOf(stateElement.getAttributeValue("nextTopicId"));
        } else {
            nextTopicId = 1;
        }
    }

    public Element getState()
    {
        Element container = new Element(AppConstants.appName);
        Element topicsElement = new Element("topics");
        for (Topic topic : topics) {
            Element topicElement = new Element("topic");
            topicElement.setAttribute("id", Integer.toString(topic.id()));
            topicElement.setAttribute("name", topic.name());
            topicElement.setAttribute("memo", topic.memo());
            topicElement.setAttribute("updatedAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(topic.updatedAt()));

            topicsElement.addContent(topicElement);

            Element topicLinesElement = new Element("topicLines");
            Iterator<TopicLine> linesIterator = topic.linesIterator();
            while (linesIterator.hasNext()) {
                TopicLine topicLine = linesIterator.next();

                Element topicLineElement = new Element("topicLine");
                topicLineElement.setAttribute("line", String.valueOf(topicLine.line()));
                topicLineElement.setAttribute("url", topicLine.file().getUrl());
                topicLineElement.setAttribute("memo", topicLine.memo());
                topicLineElement.setAttribute("order", String.valueOf(topicLine.order()));
                topicLinesElement.addContent(topicLineElement);
            }

            topicElement.addContent(topicLinesElement);
        }
        container.addContent(topicsElement);

        Element state = new Element("state");
        state.setAttribute("nextTopicId", Integer.toString(nextTopicId));
        container.addContent(state);
        return container;
    }
}
