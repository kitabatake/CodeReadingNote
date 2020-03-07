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

@State(
    name = AppConstants.appName,
    storages = {
        @Storage(AppConstants.appName + ".xml"),
    }
)
public class TopicListService implements PersistentStateComponent<Element>
{
    private ArrayList<Topic> topicList = new ArrayList<>();
    private Integer nextTopicId = 1;

    public static TopicListService getInstance(@NotNull Project project)
    {
        return ServiceManager.getService(project, TopicListService.class);
    }

    public void addTopic(String name)
    {
        Topic topic = new Topic(nextTopicId, name);
        topicList.add(topic);
        nextTopicId++;
    }

    public Iterator<Topic> getTopicListIterator()
    {
        return topicList.iterator();
    }

    public String[] getTopicStrings()
    {
        Iterator<Topic> iterator = getTopicListIterator();
        ArrayList<String> ret = new ArrayList<>();
        while (iterator.hasNext()) {
            Topic topic = iterator.next();
            ret.add(topic.getName());
        }
        return ret.toArray(new String[0]);
    }

    public void clearTopicList()
    {
        topicList.clear();
    }

    @Override
    public Element getState()
    {
        Element container = new Element(AppConstants.appName);
        Element topicsElement = new Element("topics");
        for (Topic topic : topicList) {
            Element topicElement = new Element("topic");
            topicElement.setAttribute("id", Integer.toString(topic.getId()));
            topicElement.setAttribute("name", topic.getName());
            topicsElement.addContent(topicElement);
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
            topicList.add(new Topic(id, name));
        }

        Element stateElement = element.getChild("state");
        if (stateElement != null) {
            nextTopicId = Integer.valueOf(stateElement.getAttributeValue("nextTopicId"));
        } else {
            nextTopicId = 1;
        }
    }
}
