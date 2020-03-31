package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBList;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.*;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.actions.ImportAction;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.actions.ExportAction;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.actions.TopicAddAction;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.actions.TopicRemoveAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Iterator;

public class ManagementPanel extends JPanel
{
    private Project project;

    private CodeReadingRecorderService service;
    private JBList<Topic> topicList;
    private DefaultListModel<Topic> topicListModel;

    private Topic selectedTopic;
    private TopicDetailPanel topicDetailPanel;

    public ManagementPanel(Project project)
    {
        super(new BorderLayout());
        this.project = project;
        service = CodeReadingRecorderService.getInstance(project);
        topicDetailPanel = new TopicDetailPanel(project);
        initTopicList();

        JBSplitter splitPane = new JBSplitter(0.3f);
        splitPane.setSplitterProportionKey(AppConstants.appName + "ManagementPanel.splitter");
        splitPane.setHonorComponentsMinimumSize(false);

        splitPane.setFirstComponent(topicList);
        splitPane.setSecondComponent(topicDetailPanel);

        add(actionToolBar(), BorderLayout.PAGE_START);
        add(splitPane);

        MessageBus messageBus = project.getMessageBus();
        messageBus.connect().subscribe(TopicListNotifier.TOPIC_LIST_NOTIFIER_TOPIC, new TopicListNotifier()
        {
            @Override
            public void topicAdded(Topic topic)
            {
                topicListModel.add(0, topic);
            }

            @Override
            public void topicRemoved(Topic topic)
            {
                topicListModel.removeElement(topic);
            }

            @Override
            public void topicsLoaded() {
                clear();
                Iterator<Topic> iterator = service.getTopicList().iterator();
                while (iterator.hasNext()) {
                    topicListModel.addElement(iterator.next());
                }
            }
        });
    }

    private JComponent actionToolBar()
    {
        DefaultActionGroup actions = new DefaultActionGroup();
        actions.add(new TopicAddAction());
        actions.add(new TopicRemoveAction(project, (v) -> {
            return topicList.getSelectedValue();
        }));
        actions.addSeparator();
        actions.add(new ExportAction());
        actions.add(new ImportAction());


        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(AppConstants.appName, actions, true);
        actionToolbar.setReservePlaceAutoPopupIcon(false);
        actionToolbar.setMinimumButtonSize(new Dimension(20, 20));

        JComponent toolBar = actionToolbar.getComponent();
        toolBar.setBorder(JBUI.Borders.merge(toolBar.getBorder(), JBUI.Borders.emptyLeft(12), true));
        toolBar.setOpaque(false);
        return toolBar;
    }

    private void clear()
    {
        selectedTopic = null;
        topicListModel.clear();
        topicDetailPanel.clear();
    }

    private void initTopicList()
    {
        Iterator<Topic> iterator = service.getTopicList().iterator();
        topicListModel = new DefaultListModel<>();
        while (iterator.hasNext()) {
            topicListModel.addElement(iterator.next());
        }

        topicList = new JBList<>(topicListModel);
        topicList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        topicList.setCellRenderer(new TopicListCellRenderer<Topic>());
        topicList.addListSelectionListener(e -> {
            Topic topic = topicList.getSelectedValue();
            if (topic == null) {
                topicDetailPanel.clear();
            } else if (selectedTopic == null || selectedTopic != topic) {
                selectedTopic = topic;
                topicDetailPanel.setTopic(topic);
            }
        });

        if (topicListModel.size() > 0) {
            topicList.setSelectedIndex(0);
        }

        topicList.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    Topic topic = topicList.getSelectedValue();
                    ActionUtil.performActionDumbAware(
                        new TopicRemoveAction(project, (v) -> { return topic; }),
                        ActionUtil.createEmptyEvent()
                    );
                }
            }
        });
    }

    private static class TopicListCellRenderer<T> extends SimpleColoredComponent implements ListCellRenderer<T>
    {
        private TopicListCellRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
        {
            clear();

            Topic topic = (Topic) value;
            append(topic.name());
            append(
                " (" + new SimpleDateFormat("yyyy/MM/dd HH:mm").format(topic.updatedAt()) + ")",
                SimpleTextAttributes.GRAY_ATTRIBUTES
            );

            setForeground(UIUtil.getListSelectionForeground(isSelected));
            setBackground(UIUtil.getListSelectionBackground(isSelected));
            return this;
        }
    }
}
