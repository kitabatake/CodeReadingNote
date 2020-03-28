package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.popup.util.DetailView;
import com.intellij.ui.popup.util.DetailViewImpl;
import com.intellij.util.messages.Topic;
import com.intellij.util.ui.JBUI;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.AppConstants;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicLine;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TopicLineDetailPanel extends JPanel
{
    private Project project;

    private EditorTextField memoArea;
    private MyDetailView detailView;

    private TopicLine topicLine;

    JTextArea tmpTextField;

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

//        JBScrollPane tmpPane =  new JBScrollPane();
//        tmpPane.add(memoArea);


//        JButton jButton = new JButton("inspect");
//        tmpPanel.add(jButton);

        tmpTextField = new JTextArea(20, 50);
        JBScrollPane tmpPanel = new JBScrollPane(tmpTextField);
//        tmpPanel.add(tmpTextField);

        contentPane.setSecondComponent(tmpPanel);
//        contentPane.setSecondComponent(new JBScrollPane(memoArea));

        add(contentPane);
    }

    private String tmpInspection()
    {
        ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(project).getFileIndex();
        VirtualFile file = topicLine.file();

        ArrayList<String> rows = new ArrayList<>();
        rows.add("path: " + file.getPath());

        boolean isInLibrary = projectFileIndex.isInLibrary(file);
        rows.add("isInLibrary: " + isInLibrary);
        if (isInLibrary) {
            OrderEntry orderEntry = LibraryUtil.findLibraryEntry(file, project);
            if (orderEntry instanceof LibraryOrderEntry) {
                LibraryOrderEntry libraryOrderEntry = (LibraryOrderEntry)orderEntry;
                Library lib = libraryOrderEntry.getLibrary();
                rows.add("libName: " + libraryOrderEntry.getLibraryName());
                rows.add("libLevel: " + libraryOrderEntry.getLibraryLevel());
            } else {
                JdkOrderEntry jdkOrderEntry = (JdkOrderEntry)orderEntry;
                rows.add("sdkName: " + jdkOrderEntry.getJdkName());
                rows.add("sdkHomePath: " + jdkOrderEntry.getJdk().getHomePath());
            }
        }

        return StringUtils.join(rows, "\n");
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

        VirtualFile file = topicLine.file();
        ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(project).getFileIndex();
        OrderEntry orderEntry = LibraryUtil.findLibraryEntry(file, project);
//        orderEntry.getPresentableName();

        tmpTextField.setText(tmpInspection());
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
