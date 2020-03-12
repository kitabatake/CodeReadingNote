package jp.kitabatakep.intellij.plugins.codereadingrecorder.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.CodeReadingRecorderService;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.Topic;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicLine;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public class TopicLineAddAction extends AnAction
{
    @Override
    public void update(AnActionEvent event) {
        Project project = event.getProject();
        DataContext dataContext = event.getDataContext();
        event.getPresentation().setEnabled(project != null &&
            (CommonDataKeys.EDITOR.getData(dataContext) != null ||
                CommonDataKeys.VIRTUAL_FILE.getData(dataContext) != null));

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

        Topic topic = topicSelectDialog(project);
        if (topic != null) {
            topic.addLine(new TopicLine(file, line));
        }
    }

    private Topic topicSelectDialog(Project project)
    {
        CodeReadingRecorderService service = CodeReadingRecorderService.getInstance(project);
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
            "Choose Topic",
            "Choose Topic",
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
