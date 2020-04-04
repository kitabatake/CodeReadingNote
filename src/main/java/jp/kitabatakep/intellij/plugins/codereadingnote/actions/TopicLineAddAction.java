package jp.kitabatakep.intellij.plugins.codereadingnote.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import jp.kitabatakep.intellij.plugins.codereadingnote.CodeReadingNoteService;
import jp.kitabatakep.intellij.plugins.codereadingnote.Topic;
import jp.kitabatakep.intellij.plugins.codereadingnote.TopicLine;
import jp.kitabatakep.intellij.plugins.codereadingnote.TopicList;
import org.jetbrains.annotations.NotNull;

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

        VirtualFile file = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        int line = editor.getCaretModel().getLogicalPosition().line;

        Topic topic = topicSelectDialog(project,file.getName() + ":" + (line+1));
        if (topic != null) {
            topic.addLine(TopicLine.createByAction(project, topic, file, line));
        }
    }

    private Topic topicSelectDialog(Project project, String message)
    {
        CodeReadingNoteService service = CodeReadingNoteService.getInstance(project);
        TopicList topicList = service.getTopicList();

        Iterator<Topic> iterator = topicList.iterator();
        ArrayList<Topic> topics = new ArrayList<>();
        ArrayList<String> topicStrings = new ArrayList<>();
        while (iterator.hasNext()) {
            Topic topic = iterator.next();
            topics.add(topic);
            topicStrings.add(topic.name());
        }
        int index = Messages.showChooseDialog(
            message,
            "Select Topic",
            topicStrings.toArray(new String[0]),
            topicStrings.get(0),
            Messages.getQuestionIcon()
        );

        if (index == -1) {
            return null;
        } else {
            return topics.get(index);
        }
    }
}
