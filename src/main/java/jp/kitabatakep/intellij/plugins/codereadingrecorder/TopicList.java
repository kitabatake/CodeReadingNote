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
            String name = topicElement.getChild("name").getText();
            String updatedAtString = topicElement.getChild("updatedAt").getText();

            Topic topic;
            try {
                topic = new Topic(project, name, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(updatedAtString));
            } catch (ParseException e) {
                Logger.getInstance(AppConstants.appName).error(e.getMessage());
                continue;
            }
            topic.setNote(topicElement.getChild("note").getText());

            Element topicLinesElement = topicElement.getChild("topicLines");
            ArrayList<TopicLine> topicLines = new ArrayList<>();
            for (Element topicLineElement : topicLinesElement.getChildren("topicLine")) {
                String lineString = topicLineElement.getChild("line").getText();
                String inProject = topicLineElement.getChild("inProject").getText();
                TopicLine topicLine = TopicLine.createByImport(
                    project,
                    topic,
                    topicLineElement.getChild("url").getText(),
                    Integer.parseInt(lineString),
                    Integer.parseInt(topicLineElement.getChild("order").getText()),
                    topicLineElement.getChild("note").getText(),
                    inProject.equals("true"),
                    topicLineElement.getChild("relativePath").getText()
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
            topicElement.addContent(new Element("name").addContent(topic.name()));
            topicElement.addContent(new Element("note").addContent(topic.note()));
            topicElement.addContent(
                new Element("updatedAt").
                    addContent(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(topic.updatedAt()))
            );

            topicsElement.addContent(topicElement);

            Element topicLinesElement = new Element("topicLines");
            Iterator<TopicLine> linesIterator = topic.linesIterator();
            while (linesIterator.hasNext()) {
                TopicLine topicLine = linesIterator.next();
                Element topicLineElement = new Element("topicLine");
                topicLineElement.addContent(new Element("line").addContent(String.valueOf(topicLine.line())));
                topicLineElement.addContent(new Element("order").addContent(String.valueOf(topicLine.order())));
                topicLineElement.addContent(new Element("inProject").addContent(String.valueOf(topicLine.inProject())));
                topicLineElement.addContent(new Element("url").addContent(topicLine.url()));
                topicLineElement.addContent(new Element("note").addContent(topicLine.note()));
                topicLineElement.addContent(
                    new Element("relativePath").addContent(topicLine.inProject() ? topicLine.relativePath() : "")
                );
                topicLinesElement.addContent(topicLineElement);
            }

            topicElement.addContent(topicLinesElement);
        }
        return topicsElement;
    }
}
