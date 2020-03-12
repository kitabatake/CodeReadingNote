package jp.kitabatakep.intellij.plugins.codereadingrecorder.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.CodeReadingRecorderService;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicList;
import org.jetbrains.annotations.NotNull;

public class TopicClearAction extends AnAction
{
    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(e.getProject() != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        CodeReadingRecorderService service = CodeReadingRecorderService.getInstance(e.getProject());
        service.getTopicList().clearTopicList();
    }
}
