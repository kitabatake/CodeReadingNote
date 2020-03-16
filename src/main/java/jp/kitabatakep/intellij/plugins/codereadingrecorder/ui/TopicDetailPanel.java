package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBList;
import com.intellij.ui.popup.util.DetailView;
import com.intellij.ui.popup.util.DetailViewImpl;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.AppConstants;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.Topic;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicLine;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicNotifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;

class TopicDetailPanel extends JPanel
{
    private Project project;
    private final JLabel myLabel = new JLabel();

    private JBList<TopicLine> topicLineList;
    private DefaultListModel<TopicLine> topicLineListModel;

    private MyDetailView detailView;

    private Topic topic;
    private TopicLine selectedTopicLine;

    public TopicDetailPanel(Project project)
    {
        super(new BorderLayout());
        this.project = project;

        initTopicLineList();
        detailView = new MyDetailView(project);

        JBSplitter splitPane = new JBSplitter(0.3f);
        splitPane.setSplitterProportionKey(AppConstants.appName + "TopicDetailPanel.splitter");
        splitPane.setFirstComponent(topicLineList);
        splitPane.setSecondComponent(detailView);

        add(myLabel);
        add(splitPane);

        MessageBus messageBus = project.getMessageBus();
        messageBus.connect().subscribe(TopicNotifier.TOPIC_NOTIFIER_TOPIC, new TopicNotifier(){
            @Override
            public void lineDeleted(Topic _topic, TopicLine _topicLine)
            {
                if (_topic == topic) {
                    topicLineListModel.removeElement(_topicLine);
                }
            }
        });
    }

    private void initTopicLineList()
    {
        topicLineList = new JBList<>();
        topicLineList.setCellRenderer(new TopicLineListCellRenderer<>(project));
        topicLineList.addListSelectionListener(e -> {
            TopicLine topicLine = topicLineList.getSelectedValue();
            if (topicLine == null) {
                detailView.clearEditor();
            } else if (selectedTopicLine == null || topicLine != selectedTopicLine) {
                selectedTopicLine = topicLine;
                detailView.navigateInPreviewEditor(DetailView.PreviewEditorState.create(topicLine.file(), topicLine.line()));
            }
        });

        topicLineList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    TopicLine topicLine = topicLineList.getSelectedValue();
                    topic.deleteLine(topicLine);

                    Notifications.Bus.notify(
                        new Notification(
                            "hoge",
                            "DeleteKey",
                            topicLine.file().getName(),
                            NotificationType.INFORMATION
                        )
                    );
                }
            }
        });
    }

    private static class MyDetailView extends DetailViewImpl
    {
        MyDetailView(Project project) {
            super(project);
        }

        @NotNull
        @Override
        protected Editor createEditor(@Nullable Project project, Document document, VirtualFile file) {
            Editor editor = super.createEditor(project, document, file);
            editor.setBorder(JBUI.Borders.empty());
            return editor;
        }
    }

    void clear()
    {
        myLabel.setText("");
        topicLineListModel.clear();
    }

    void setTopic(Topic topic)
    {
        this.topic = topic;
        myLabel.setText(topic.name());

        topicLineListModel = new DefaultListModel<>();
        Iterator<TopicLine> iterator = topic.linesIterator();
        while (iterator.hasNext()) {
            topicLineListModel.addElement(iterator.next());
        }

        topicLineList.setModel(topicLineListModel);
    }

    private static class TopicLineListCellRenderer<T> extends JLabel implements ListCellRenderer<T>
    {
        private Project project;

        private TopicLineListCellRenderer(Project project)
        {
            this.project = project;
            setOpaque(true);
        }

        public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
        {
            TopicLine topicLine = (TopicLine) value;
            VirtualFile file = topicLine.file();

            PsiElement fileOrDir = PsiUtilCore.findFileSystemItem(project, file);
            if (fileOrDir != null) {
                setIcon(fileOrDir.getIcon(0));
            }



            setText(file.getName() + ":" + topicLine.line());

            setForeground(UIUtil.getListSelectionForeground(isSelected));
            setBackground(UIUtil.getListSelectionBackground(isSelected));
            return this;
        }
    }
}
