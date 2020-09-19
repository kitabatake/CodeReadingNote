package jp.kitabatakep.intellij.plugins.codereadingnote.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import jp.kitabatakep.intellij.plugins.codereadingnote.Topic;
import org.jetbrains.annotations.NotNull;

public class TopicLineMoveToAction extends AnAction
{
    Topic moveTo;

    public TopicLineMoveToAction(Topic moveTo)
    {
        super(moveTo.name(), moveTo.name(), null);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(e.getProject() != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
    }
}
