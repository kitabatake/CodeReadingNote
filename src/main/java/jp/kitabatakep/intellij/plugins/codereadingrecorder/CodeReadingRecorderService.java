package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.ui.ManagementPopup;
import org.jetbrains.annotations.NotNull;

import org.jdom.Element;
import java.util.Iterator;

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
    private ManagementPopup popup;

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

    public void openPopup(@NotNull AnActionEvent e)
    {
        if (popup == null) {
            popup = new ManagementPopup();
            popup.buildPopup();
        }
        popup.open(e);
    }
}
