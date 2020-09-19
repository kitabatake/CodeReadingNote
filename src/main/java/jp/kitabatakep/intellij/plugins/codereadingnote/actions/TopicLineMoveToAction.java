package jp.kitabatakep.intellij.plugins.codereadingnote.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import jp.kitabatakep.intellij.plugins.codereadingnote.Topic;
import jp.kitabatakep.intellij.plugins.codereadingnote.TopicLine;
import org.jetbrains.annotations.NotNull;

public class TopicLineMoveToAction extends AnAction
{
    private final Topic moveTo;
    private final TopicLine topicLine;

    public TopicLineMoveToAction(TopicLine topicLine, Topic moveTo)
    {
        super(moveTo.name(), moveTo.name(), null);
        this.moveTo = moveTo;
        this.topicLine = topicLine;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(e.getProject() != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        topicLine.topic().removeLine(topicLine);
        moveTo.addLine(
            TopicLine.createByAction(
                e.getProject(),
                moveTo,
                topicLine.file(),
                topicLine.line(),
                topicLine.note()
            )
        );
    }
}
