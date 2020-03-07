package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

@State(
    name = AppConstants.appName,
    storages = {
        @Storage(AppConstants.appName + ".xml"),
    }
)
public class TopicListService implements PersistentStateComponent<Element>
{
    private ArrayList<Topic> topics = new ArrayList<>();
    private Integer nextTopicId = 1;

    public static TopicListService getInstance(@NotNull Project project)
    {
        return ServiceManager.getService(project, TopicListService.class);
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

    public void clearTopicList()
    {
        topics.clear();
    }

    @Override
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

    @Override
    public void loadState(@NotNull Element element)
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
}
