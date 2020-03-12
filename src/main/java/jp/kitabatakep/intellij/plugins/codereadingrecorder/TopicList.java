package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import org.jdom.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

public class TopicList
{
    private ArrayList<Topic> topics = new ArrayList<>();
    private Integer nextTopicId = 1;

    public void loadState(Element element)
    {
        Element topicsElement = element.getChild("topics");
        for (Element bookmarkElement : topicsElement.getChildren("topic")) {
            int id = Integer.valueOf(bookmarkElement.getAttributeValue("id")).intValue();
            String name = bookmarkElement.getAttributeValue("name");
            topics.add(new Topic(id, name));
        }

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
            topicElement.setAttribute("id", Integer.toString(topic.getId()));
            topicElement.setAttribute("name", topic.getName());
            topicsElement.addContent(topicElement);

            Element topicLinesElement = new Element("topicLines");
            Iterator<TopicLine> linesIterator = topic.getLinesIterator();
            while (linesIterator.hasNext()) {
                TopicLine topicLine = linesIterator.next();

                Element topicLineElement = new Element("topicLine");
                topicLineElement.setAttribute("line", String.valueOf(topicLine.getLine()));
                topicLineElement.setAttribute("url", topicLine.getFile().getUrl());
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

    public void addTopic(String name)
    {
        Topic topic = new Topic(nextTopicId, name);
        topics.add(topic);
        nextTopicId++;
    }

    public Stream<Topic> topicsStream()
    {
        return topics.stream();
    }

    public Iterator<Topic> iterator()
    {
        return topics.iterator();
    }

    public void clearTopicList()
    {
        topics.clear();
    }


}
