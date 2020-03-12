package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ManagementViewService
{
    private static Project project;
    private static final String DIMENSION_SERVICE_KEY = AppConstants.appName;
    private JBPopup popup;

    public static ManagementViewService getInstance(@NotNull Project project)
    {
        ManagementViewService.project = project;
        return ServiceManager.getService(project, ManagementViewService.class);
    }

    public void open(@NotNull AnActionEvent e)
    {
        if (popup == null) {
            popup = createPopup();
        }
        popup.showInBestPositionFor(e.getDataContext());
    }

    private JBPopup createPopup()
    {
        JPanel contentPane = new JPanel(new BorderLayout());
        JLabel label = new JLabel(AppConstants.appName);
        label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(label, BorderLayout.NORTH);

        ComponentPopupBuilder builder = JBPopupFactory.getInstance().createComponentPopupBuilder(contentPane, null);
        builder
            .setDimensionServiceKey(null, DIMENSION_SERVICE_KEY, true)
            .setResizable(true)
            .setMovable(true);

        return builder.createPopup();
    }
}
