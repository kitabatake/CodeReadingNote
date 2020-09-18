package jp.kitabatakep.intellij.plugins.codereadingnote.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ui.JBDimension;
import com.intellij.util.ui.UIUtil;
import jp.kitabatakep.intellij.plugins.codereadingnote.CodeReadingNoteService;
import jp.kitabatakep.intellij.plugins.codereadingnote.Topic;
import jp.kitabatakep.intellij.plugins.codereadingnote.TopicLine;
import jp.kitabatakep.intellij.plugins.codereadingnote.TopicList;
import jp.kitabatakep.intellij.plugins.codereadingnote.ui.MyEditorTextField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class TopicLineAddAction extends AnAction
{
    @Override
    public void update(AnActionEvent event) {
        Project project = event.getProject();
        DataContext dataContext = event.getDataContext();

        if (project == null) {
            event.getPresentation().setEnabled(false);
        } else {
            CodeReadingNoteService service = CodeReadingNoteService.getInstance(project);
            event.getPresentation().setEnabled(
                service.getTopicList().iterator().hasNext() &&
                (CommonDataKeys.EDITOR.getData(dataContext) != null ||
                    CommonDataKeys.VIRTUAL_FILE.getData(dataContext) != null));
        }

        event.getPresentation().setText("Add to Topic");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event)
    {
        Project project = event.getProject();
        if (project == null) return;

        CodeReadingNoteService service = CodeReadingNoteService.getInstance(project);

        VirtualFile file = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        int line = editor.getCaretModel().getLogicalPosition().line;

        TopicList topicList = service.getTopicList();
        Iterator<Topic> iterator = topicList.iterator();
        ArrayList<Topic> topics = new ArrayList<>();
        while (iterator.hasNext()) {
            Topic topic = iterator.next();
            topics.add(topic);
        }

        MyEditorTextField noteInputField = new MyEditorTextField(project, FileTypeManager.getInstance().getStdFileType("Markdown"));

        PopupChooserBuilder<Topic> builder = new PopupChooserBuilder<Topic>(new JList<>(topics.toArray(new Topic[0])));
        builder
            .setTitle("Select Topic")
            .setRenderer(new MyCellRenderer<Topic>())
            .setResizable(true)
            .setItemChosenCallback((topic) -> {
                if (topic != null) {
                    topic.addLine(TopicLine.createByAction(project, topic, file, line, noteInputField.getText()));
                }
            })
            .createPopup();


        noteInputField.setOneLineMode(false);
        noteInputField.setPlaceholder(" Code line note input area");
        noteInputField.setPreferredSize(
            new JBDimension(
                (int)builder.getChooserComponent().getPreferredSize().getWidth(),
                100
            )
        );
        builder.setSouthComponent(noteInputField);

        builder.createPopup().showInBestPositionFor(editor);
    }

    private static class MyCellRenderer<T> extends SimpleColoredComponent implements ListCellRenderer<T>
    {
        private MyCellRenderer()
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
