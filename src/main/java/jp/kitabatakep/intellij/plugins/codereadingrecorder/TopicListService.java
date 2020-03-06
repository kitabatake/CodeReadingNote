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

    public static TopicListService getInstance(@NotNull Project project)
    {
        return ServiceManager.getService(project, TopicListService.class);
    }

    public void addTopic(Topic topic) {
        topicList.add(topic);
    }

    public Iterator<Topic> getTopicListIterator()
    {
        return topicList.iterator();
    }

    @Override
    public Element getState()
    {
        Element container = new Element(AppConstants.appName);
        for (Topic topic : topicList) {
            Element topicElement = new Element("topic");
            topicElement.setAttribute("name", topic.getName());
            container.addContent(topicElement);
        }

        return container;
    }

    @Override
    public void loadState(@NotNull Element element)
    {
        for (Element bookmarkElement : element.getChildren("topic")) {
            String name = bookmarkElement.getAttributeValue("name");
            topicList.add(new Topic(name));
        }
    }
}
