package jp.kitabatakep.intellij.plugins.codereadingrecorder.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.Topic;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicLine;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicListService;
import org.jetbrains.annotations.NotNull;

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
        TopicListService service = TopicListService.getInstance(project);
        Topic[] topics = service.topicsStream().toArray(Topic[]::new);
        String[] topicStrings = service.topicsStream().map(Topic::getName).toArray(String[]::new);
        int index = Messages.showChooseDialog(
            "Choose Topic",
            "Choose Topic",
            topicStrings,
            topicStrings[0],
            Messages.getQuestionIcon()
        );

        if (index == -1) {
            return null;
        } else {
            return topics[index];
        }
    }
}
