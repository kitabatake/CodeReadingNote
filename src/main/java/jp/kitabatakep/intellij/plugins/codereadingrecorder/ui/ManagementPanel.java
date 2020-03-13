package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBList;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.ui.JBUI;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.AppConstants;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.CodeReadingRecorderService;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.Topic;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicListNotifier;
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
    private JBList topicList;
    private DefaultListModel<Topic> topicListModel;

    public ManagementPanel(Project project, final ToolWindow toolWindow)
    {
        super(new BorderLayout());
        this.project = project;
        this.toolWindow = toolWindow;
        service = CodeReadingRecorderService.getInstance(project);
        initTopicList();

        JBSplitter splitPane = new JBSplitter(0.3f);
        splitPane.setSplitterProportionKey(AppConstants.appName + ".splitter");

        splitPane.setFirstComponent(topicList);
        splitPane.setSecondComponent(new JLabel(AppConstants.appName));

        add(actionToolBar(), BorderLayout.PAGE_START);
        add(splitPane);

        MessageBus messageBus = project.getMessageBus();
        messageBus.connect().subscribe(TopicListNotifier.TOPIC_LIST_NOTIFIER_TOPIC, new TopicListNotifier()
        {
            @Override
            public void topicAdded(Topic topic)
            {
                topicListModel.addElement(topic);
            }

            @Override
            public void topicDeleted(Topic topic)
            {

            }
        });
    }

    private void initTopicList()
    {
        Iterator<Topic> iterator = service.getTopicList().iterator();
        topicListModel = new DefaultListModel<>();
        while (iterator.hasNext()) {
            topicListModel.addElement(iterator.next());
        }

        topicList = new JBList<>(topicListModel);
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
