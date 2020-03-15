package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBList;
import com.intellij.ui.popup.util.DetailView;
import com.intellij.ui.popup.util.DetailViewImpl;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.AppConstants;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.Topic;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicLine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

class TopicDetailPanel extends JPanel
{
    private final JLabel myLabel = new JLabel();

    private JBList<TopicLine> topicLineList;
    private DefaultListModel<TopicLine> topicLineListModel;

    private MyDetailView detailView;

    private TopicLine selectedTopicLine;

    public TopicDetailPanel(Project project)
    {
        super(new BorderLayout());
        initTopicLineList();

        detailView = new MyDetailView(project);

        JBSplitter splitPane = new JBSplitter(0.3f);
        splitPane.setSplitterProportionKey(AppConstants.appName + "TopicDetailPanel.splitter");
        splitPane.setFirstComponent(topicLineList);
        splitPane.setSecondComponent(detailView);

        add(myLabel);
        add(splitPane);
    }

    private void initTopicLineList()
    {
        topicLineList = new JBList<>();
        topicLineList.setCellRenderer(new TopicLineListCellRenderer<>());
        topicLineList.addListSelectionListener(e -> {
            TopicLine topicLine = topicLineList.getSelectedValue();
            if (selectedTopicLine == null || topicLine != selectedTopicLine) {
                selectedTopicLine = topicLine;
                detailView.navigateInPreviewEditor(DetailView.PreviewEditorState.create(topicLine.file(), topicLine.line()));
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

    void setTopic(Topic topic)
    {
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
        private TopicLineListCellRenderer()
        {
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

            setText(file.getName() + ":" + topicLine.line());

            setForeground(UIUtil.getListSelectionForeground(isSelected));
            setBackground(UIUtil.getListSelectionBackground(isSelected));
            return this;
        }
    }
}
