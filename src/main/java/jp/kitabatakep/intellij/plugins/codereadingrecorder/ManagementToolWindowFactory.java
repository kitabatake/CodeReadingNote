package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.ui.ManagementPanel;
import org.jetbrains.annotations.NotNull;

public class ManagementToolWindowFactory implements ToolWindowFactory
{
    public static final String ID = AppConstants.appName;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow)
    {
        ManagementPanel panel = new ManagementPanel(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(panel, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
