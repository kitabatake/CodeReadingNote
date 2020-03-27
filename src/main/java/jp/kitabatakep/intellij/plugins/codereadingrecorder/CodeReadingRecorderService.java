package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import com.github.hypfvieh.util.StringUtil;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.libraries.LibraryUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.openapi.roots.libraries.LibraryUtil.getLibraryRoots;

@State(
    name = AppConstants.appName,
    storages = {
        @Storage(AppConstants.appName + ".xml"),
    }
)
public class CodeReadingRecorderService implements PersistentStateComponent<Element>
{
    Project project;
    TopicList topicList;

    public CodeReadingRecorderService(@NotNull Project project)
    {
        this.project = project;
        topicList = new TopicList(project);

        Module[] modules = ModuleManager.getInstance(project).getModules();
        String libNames = "";
        for (Module module : modules) {
           String moduleName = module.getName();

            final List<String> libraryNames = new ArrayList<String>();
            ModuleRootManager.getInstance(module).orderEntries().forEachLibrary(library -> {
                libraryNames.add(library.getName());
                return true;
            });

            libNames = StringUtil.join("\n", libraryNames);
        }

        VirtualFile[] libroots = LibraryUtil.getLibraryRoots(project);

        Sdk projectSdk = ProjectRootManager.getInstance(project).getProjectSdk();
        String projectSDKName = ProjectRootManager.getInstance(project).getProjectSdkName();
        System.out.println(libNames);
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
