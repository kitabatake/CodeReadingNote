package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
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

    public TopicList(Project project)
    {
        this.project = project;
    }

    public void addTopic(String name)
    {
        Topic topic = new Topic(project, name, new Date());
        topics.add(topic);

        MessageBus messageBus = project.getMessageBus();
        TopicListNotifier publisher = messageBus.syncPublisher(TopicListNotifier.TOPIC_LIST_NOTIFIER_TOPIC);
        publisher.topicAdded(topic);
    }

    public void removeTopic(Topic topic)
    {
        topics.remove(topic);
        MessageBus messageBus = project.getMessageBus();
        TopicListNotifier publisher = messageBus.syncPublisher(TopicListNotifier.TOPIC_LIST_NOTIFIER_TOPIC);
        publisher.topicRemoved(topic);
    }

    public Iterator<Topic> iterator()
    {
        Collections.sort(topics);
        return topics.iterator();
    }

    public void loadState(Element topicsElement)
    {
        topics = new ArrayList<>();
        for (Element topicElement : topicsElement.getChildren("topic")) {
            String name = topicElement.getAttributeValue("name");
            String updatedAtString = topicElement.getAttributeValue("updatedAt");

            Topic topic;
            try {
                topic = new Topic(project, name, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(updatedAtString));
            } catch (ParseException e) {
                Logger.getInstance(AppConstants.appName).error(e.getMessage());
                continue;
            }
            topic.setMemo(topicElement.getAttributeValue("memo"));

            Element topicLinesElement = topicElement.getChild("topicLines");
            ArrayList<TopicLine> topicLines = new ArrayList<>();
            for (Element topicLineElement : topicLinesElement.getChildren("topicLine")) {
                String lineString = topicLineElement.getAttributeValue("line");
                String inProject = topicLineElement.getAttributeValue("inProject");
                TopicLine topicLine = TopicLine.createByImport(
                    project,
                    topic,
                    topicLineElement.getAttributeValue("url"),
                    Integer.parseInt(lineString),
                    Integer.parseInt(topicLineElement.getAttributeValue("order")),
                    topicLineElement.getAttributeValue("memo"),
                    inProject.equals("true"),
                    topicLineElement.getAttributeValue("relativePath")
                );
                topicLines.add(topicLine);
            }
            topic.setLines(topicLines);
            topics.add(topic);
        }
        Collections.sort(topics);
    }

    public Element getState()
    {
        Element topicsElement = new Element("topics");
        for (Topic topic : topics) {
            Element topicElement = new Element("topic");
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
                topicLineElement.setAttribute("url", topicLine.url());
                topicLineElement.setAttribute("memo", topicLine.memo());
                topicLineElement.setAttribute("order", String.valueOf(topicLine.order()));
                topicLineElement.setAttribute("inProject", String.valueOf(topicLine.inProject()));
                topicLineElement.setAttribute("relativePath", topicLine.inProject() ? topicLine.relativePath() : "");
                topicLinesElement.addContent(topicLineElement);
            }

            topicElement.addContent(topicLinesElement);
        }
        return topicsElement;
    }
}
