package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.JBUI;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.AppConstants;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.CodeReadingRecorderService;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.Topic;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.actions.TopicAddAction;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ManagementPanel extends JPanel
{
    private ToolWindow toolWindow;
    private Project project;

    private CodeReadingRecorderService service;

    public ManagementPanel(Project project, final ToolWindow toolWindow)
    {
        super(new BorderLayout());
        this.project = project;
        this.toolWindow = toolWindow;
        service = CodeReadingRecorderService.getInstance(project);

        JBSplitter splitPane = new JBSplitter(0.3f);
        splitPane.setSplitterProportionKey(AppConstants.appName + ".splitter");

        splitPane.setFirstComponent(topicList());
        splitPane.setSecondComponent(new JLabel(AppConstants.appName));

        add(actionToolBar(), BorderLayout.PAGE_START);
        add(splitPane);
    }

    private JBList topicList()
    {
        Iterator<Topic> iterator = service.getTopicList().iterator();
        ArrayList<Topic> topics = new ArrayList<>();
        while (iterator.hasNext()) {
            topics.add(iterator.next());
        }

        return new JBList<>(topics);
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
}
