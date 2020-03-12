package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.InplaceButton;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.JBUI;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.AppConstants;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.Topic;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.actions.TopicAddAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

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
            .setMovable(true)
            .setSettingButtons(actionToolBar());

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

    private JComponent actionToolBar()
    {
        DefaultActionGroup actions = new DefaultActionGroup();
        actions.add(new TopicAddAction());

        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(AppConstants.appName, actions, true);
        actionToolbar.setReservePlaceAutoPopupIcon(false);
        actionToolbar.setMinimumButtonSize(new Dimension(20, 20));

        JComponent toolBar = actionToolbar.getComponent();
        toolBar.setBorder(JBUI.Borders.merge(toolBar.getBorder(), JBUI.Borders.emptyLeft(12), true));
        toolBar.setOpaque(false);
        return toolBar;
    }

    public void open(@NotNull AnActionEvent e)
    {
        popup.showInBestPositionFor(e.getDataContext());
    }
}
