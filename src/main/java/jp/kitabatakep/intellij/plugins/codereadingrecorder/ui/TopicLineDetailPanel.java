package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.popup.util.DetailView;
import com.intellij.ui.popup.util.DetailViewImpl;
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

    private MyEditorTextField noteArea;
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

        noteArea = new MyEditorTextField(project, MarkdownFileType.INSTANCE);
        noteArea.setOneLineMode(false);
        noteArea.setEnabled(false);
        contentPane.setSecondComponent(noteArea);

        add(contentPane);
    }

    public void clear()
    {
        topicLine = null;
        label.setText("");
        detailView.clearEditor();
        noteArea.setDocument(EditorFactory.getInstance().createDocument(""));
        noteArea.setEnabled(false);
    }

    public void setTopicLine(TopicLine topicLine)
    {
        this.topicLine = topicLine;

        if (topicLine.isValid()) {
            detailView.navigateInPreviewEditor(DetailView.PreviewEditorState.create(topicLine.file(), topicLine.line()));
        } else {
            detailView.clearEditor();
        }

        label.setText(topicLine.pathForDisplay());
        noteArea.setEnabled(true);
        if (topicLine.note().equals("")) {
            noteArea.setPlaceholder("code note input area");
        }
        noteArea.setDocument(EditorFactory.getInstance().createDocument(topicLine.note()));
        noteArea.getDocument().addDocumentListener(new NoteAreaListener(this));
    }


    private static class NoteAreaListener implements DocumentListener
    {
        TopicLineDetailPanel topicLineDetailPanel;

        private NoteAreaListener(TopicLineDetailPanel topicLineDetailPanel)
        {
            this.topicLineDetailPanel = topicLineDetailPanel;
        }

        public void documentChanged(com.intellij.openapi.editor.event.DocumentEvent e)
        {
            Document doc = e.getDocument();
            topicLineDetailPanel.topicLine.setNote(doc.getText());
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
