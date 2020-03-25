package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.popup.util.DetailView;
import com.intellij.ui.popup.util.DetailViewImpl;
import com.intellij.util.messages.Topic;
import com.intellij.util.ui.JBUI;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.AppConstants;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicLine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class TopicLineDetailPanel extends JPanel
{
    private Project project;

    private EditorTextField memoArea;
    private MyDetailView detailView;

    private TopicLine topicLine;

    public TopicLineDetailPanel(Project project)
    {
        super(new BorderLayout());

        this.project = project;

        JBSplitter contentPane = new JBSplitter(true, 0.8f);
        contentPane.setSplitterProportionKey(AppConstants.appName + "TopicLineDetailPanelContentPane.splitter");

        detailView = new MyDetailView(project);
        contentPane.setFirstComponent(detailView);

        memoArea = new EditorTextField(project, FileTypes.PLAIN_TEXT);
        memoArea.setOneLineMode(false);
        contentPane.setSecondComponent(memoArea);

        add(contentPane);
    }

    public void clear()
    {
        topicLine = null;
        detailView.clearEditor();
        memoArea.setDocument(EditorFactory.getInstance().createDocument(""));
    }

    public void setTopicLine(TopicLine topicLine)
    {
        this.topicLine = topicLine;
        detailView.navigateInPreviewEditor(DetailView.PreviewEditorState.create(topicLine.file(), topicLine.line()));

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
