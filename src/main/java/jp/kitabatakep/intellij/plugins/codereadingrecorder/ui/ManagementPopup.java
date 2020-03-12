package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.InplaceButton;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBList;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.AppConstants;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.Topic;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ManagementPopup
{
    private static final String DIMENSION_SERVICE_KEY = AppConstants.appName;

    private JBPopup popup;
    private JBList<Topic> topicList;

    public void buildPopup(Topic[] topics)
    {
        JBSplitter splitPane = new JBSplitter(0.3f);
        splitPane.setSplitterProportionKey(AppConstants.appName + ".splitter");

        topicList = new JBList<>(topics);
        splitPane.setFirstComponent(topicList);
        splitPane.setSecondComponent(new JLabel(AppConstants.appName));

        ComponentPopupBuilder builder = JBPopupFactory.getInstance().createComponentPopupBuilder(splitPane, null);
        builder
            .setDimensionServiceKey(null, DIMENSION_SERVICE_KEY, true)
            .setResizable(true)
            .setMovable(true);

        builder.setCommandButton(
            new InplaceButton(
                new IconButton("Close", AllIcons.Actions.Close, AllIcons.Actions.CloseHovered),
                event -> {
                    if (popup != null) popup.cancel();
                }
            )
        );

        popup = builder.createPopup();
    }

    public void open(@NotNull AnActionEvent e)
    {
        popup.showInBestPositionFor(e.getDataContext());
    }
}
