package jp.kitabatakep.intellij.plugins.codereadingnote.actions;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import jp.kitabatakep.intellij.plugins.codereadingnote.CodeReadingNoteService;
import jp.kitabatakep.intellij.plugins.codereadingnote.Topic;
import jp.kitabatakep.intellij.plugins.codereadingnote.TopicLine;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public class TopicLineMoveToGroupAction extends ActionGroup
{
    private final TopicLine topicLine;

    public TopicLineMoveToGroupAction(TopicLine topicLine)
    {
        super("Move to", true);
        this.topicLine = topicLine;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(e.getProject() != null);
    }

    @NotNull
    @Override
    public AnAction[] getChildren(AnActionEvent e) {
        CodeReadingNoteService service = CodeReadingNoteService.getInstance(e.getProject());
        Iterator<Topic> iterator = service.getTopicList().iterator();

        ArrayList<AnAction> actions = new ArrayList<>();
        while (iterator.hasNext()) {
            actions.add(new TopicLineMoveToAction(topicLine, iterator.next()));
        }
        return actions.toArray(new AnAction[0]);
    }
}
