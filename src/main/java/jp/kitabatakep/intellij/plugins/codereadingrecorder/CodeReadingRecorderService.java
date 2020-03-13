package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import org.jdom.Element;

@State(
    name = AppConstants.appName,
    storages = {
        @Storage(AppConstants.appName + ".xml"),
    }
)
public class CodeReadingRecorderService implements PersistentStateComponent<Element>
{
    Project project;

    TopicList topicList = new TopicList();

    public CodeReadingRecorderService(@NotNull Project project)
    {
        this.project = project;
    }

    public static CodeReadingRecorderService getInstance(@NotNull Project project)
    {
        return ServiceManager.getService(project, CodeReadingRecorderService.class);
    }

    @Override
    public Element getState()
    {
        return topicList.getState();
    }

    @Override
    public void loadState(@NotNull Element element)
    {
        topicList.loadState(element);
    }

    public TopicList getTopicList()
    {
        return this.topicList;
    }
}
