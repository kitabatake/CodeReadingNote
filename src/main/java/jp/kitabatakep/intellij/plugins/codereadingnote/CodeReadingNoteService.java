package jp.kitabatakep.intellij.plugins.codereadingnote;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import org.jdom.Element;

import java.util.ArrayList;

@State(
    name = AppConstants.appName,
    storages = {
        @Storage(AppConstants.appName + ".xml"),
    }
)
public class CodeReadingNoteService implements PersistentStateComponent<Element>
{
    Project project;
    TopicList topicList;

    String lastExportDir = "";
    String lastImportDir = "";

    public CodeReadingNoteService(@NotNull Project project)
    {
        this.project = project;
        topicList = new TopicList(project);
    }

    public static CodeReadingNoteService getInstance(@NotNull Project project)
    {
        return ServiceManager.getService(project, CodeReadingNoteService.class);
    }

    @Override
    public Element getState()
    {
        Element container = new Element(AppConstants.appName);
        container.addContent(TopicListExporter.export(getTopicList().iterator()));
        Element state = new Element("state");
        state.setAttribute("lastExportDir", lastExportDir());
        state.setAttribute("lastImportDir", lastImportDir());
        container.addContent(state);
        return container;
    }

    @Override
    public void loadState(@NotNull Element element)
    {
        try {
            topicList.setTopics(TopicListImporter.importElement(project, element.getChild("topics")));
        } catch (TopicListImporter.FormatException e) {
            topicList.setTopics(new ArrayList<>());
        }

        Element stateElement = element.getChild("state");
        lastExportDir = stateElement.getAttributeValue("lastExportDir");
        lastImportDir = stateElement.getAttributeValue("lastImportDir");
    }

    public TopicList getTopicList()
    {
        return this.topicList;
    }

    public String lastExportDir() { return lastExportDir != null ? lastExportDir : ""; }
    public void setLastExportDir(String lastExportDir) { this.lastExportDir = lastExportDir; }

    public String lastImportDir() { return lastImportDir != null ? lastImportDir : ""; }
    public void setLastImportDir(String lastImportDir) { this.lastImportDir = lastImportDir; }
}
