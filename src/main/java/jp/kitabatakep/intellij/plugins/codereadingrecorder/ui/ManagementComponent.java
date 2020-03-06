package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import java.awt.*;

public class ManagementComponent extends JPanel implements Disposable
{
    private static Logger logger = Logger.getInstance(ManagementComponent.class);

    private ToolWindow toolWindow;
    private Project project;

    public ManagementComponent(Project project, final ToolWindow toolWindow)
    {
        super(new BorderLayout());
        this.project = project;
        this.toolWindow = toolWindow;
    }

    @Override
    public void dispose()
    {
        logger.debug("dispose");
    }
}
