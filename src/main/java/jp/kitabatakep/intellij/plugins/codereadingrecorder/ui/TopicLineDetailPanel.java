package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.popup.util.DetailView;
import com.intellij.ui.popup.util.DetailViewImpl;
import com.intellij.util.ui.JBUI;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicLine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class TopicLineDetailPanel extends JPanel
{
    private Project project;

    private MyDetailView detailView;

    public TopicLineDetailPanel(Project project)
    {
        super(new BorderLayout());

        this.project = project;
        detailView = new MyDetailView(project);
        add(detailView);
    }

    public void clear()
    {
        detailView.clearEditor();
    }

    public void setTopicLine(TopicLine topicLine)
    {
        detailView.navigateInPreviewEditor(DetailView.PreviewEditorState.create(topicLine.file(), topicLine.line()));
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
