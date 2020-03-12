package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.AppConstants;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ManagementPopup
{
    private static final String DIMENSION_SERVICE_KEY = AppConstants.appName;

    private JBPopup popup;

    public void buildPopup()
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

        popup = builder.createPopup();
    }

    public void open(@NotNull AnActionEvent e)
    {
        popup.showInBestPositionFor(e.getDataContext());
    }
}
