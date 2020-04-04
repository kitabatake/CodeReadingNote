package jp.kitabatakep.intellij.plugins.codereadingnote;

import org.jdom.Element;

import java.text.SimpleDateFormat;
import java.util.Iterator;

public class TopicListExporter
{
    public static Element export(Iterator<Topic> iterator)
    {
        Element topicsElement = new Element("topics");
        while (iterator.hasNext()) {
            Topic topic = iterator.next();
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
