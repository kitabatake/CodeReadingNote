package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.popup.util.DetailView;
import com.intellij.ui.popup.util.DetailViewImpl;
import com.intellij.util.messages.Topic;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.AppConstants;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicLine;
import org.intellij.plugins.markdown.lang.MarkdownFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class TopicLineDetailPanel extends JPanel
{
    private Project project;

    private EditorTextField memoArea;
    private MyDetailView detailView;
    private JBLabel label;

    private TopicLine topicLine;

    public TopicLineDetailPanel(Project project)
    {
        super(new BorderLayout());
        this.project = project;

        JBSplitter contentPane = new JBSplitter(true, 0.8f);
        contentPane.setSplitterProportionKey(AppConstants.appName + "TopicLineDetailPanelContentPane.splitter");

        JPanel firstPanel = new JPanel(new BorderLayout());
        detailView = new MyDetailView(project);
        firstPanel.add(detailView);

        label = new JBLabel(UIUtil.ComponentStyle.SMALL);
        firstPanel.add("South", label);

        contentPane.setFirstComponent(firstPanel);

        memoArea = new EditorTextField(project, MarkdownFileType.INSTANCE);
        memoArea.setOneLineMode(false);
        memoArea.setEnabled(false);
        contentPane.setSecondComponent(new JBScrollPane(memoArea));

        add(contentPane);
    }

    public void clear()
    {
        topicLine = null;
        label.setText("");
        detailView.clearEditor();
        memoArea.setDocument(EditorFactory.getInstance().createDocument(""));
        memoArea.setEnabled(false);
    }

    public void setTopicLine(TopicLine topicLine)
    {
        this.topicLine = topicLine;

        if (topicLine.isValid()) {
            detailView.navigateInPreviewEditor(DetailView.PreviewEditorState.create(topicLine.file(), topicLine.line()));
        } else {
            detailView.clearEditor();
        }

        label.setText(topicLine.label());
        memoArea.setEnabled(true);
        if (topicLine.memo().equals("")) {
            memoArea.setPlaceholder("code note input area");
        }
        memoArea.setDocument(EditorFactory.getInstance().createDocument(topicLine.memo()));
        memoArea.getDocument().addDocumentListener(new MemoAreaListener(this));
    }


    private static class MemoAreaListener implements DocumentListener
    {
        TopicLineDetailPanel topicLineDetailPanel;

        private MemoAreaListener(TopicLineDetailPanel topicLineDetailPanel)
        {
            this.topicLineDetailPanel = topicLineDetailPanel;
        }

        public void documentChanged(com.intellij.openapi.editor.event.DocumentEvent e)
        {
            Document doc = e.getDocument();
            topicLineDetailPanel.topicLine.setMemo(doc.getText());
        }
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
}
