package jp.kitabatakep.intellij.plugins.codereadingrecorder.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
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

        TopicListService service = TopicListService.getInstance(project);
        String[] topicStrings = service.getTopicStrings();
        int index = Messages.showChooseDialog(
            "Choose Topic",
            "Choose Topic",
            topicStrings,
            topicStrings[topicStrings.length - 1],
            Messages.getQuestionIcon()
        );

        Logger.getInstance("hoge").info(topicStrings[index]);
    }
}
