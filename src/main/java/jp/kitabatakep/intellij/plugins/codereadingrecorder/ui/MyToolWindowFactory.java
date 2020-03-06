package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class MyToolWindowFactory implements ToolWindowFactory, DumbAware
{
    public static final String ID = "CodeReadingRecorder";

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ManagementComponent managementComponent = new ManagementComponent(project, toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(managementComponent, "", false);
        toolWindow.getContentManager().addContent(content);

//        if (PlantUmlSettings.getInstance().isAutoRender()) {
//            plantUmlToolWindow.renderLater(LazyApplicationPoolExecutor.Delay.POST_DELAY, RenderCommand.Reason.FILE_SWITCHED);
//        }
    }
}

