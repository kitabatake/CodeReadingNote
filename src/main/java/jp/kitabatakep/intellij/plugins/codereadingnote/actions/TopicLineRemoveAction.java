package jp.kitabatakep.intellij.plugins.codereadingnote.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import jp.kitabatakep.intellij.plugins.codereadingnote.Topic;
import jp.kitabatakep.intellij.plugins.codereadingnote.TopicLine;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class TopicLineRemoveAction extends AnAction
{
    Function<Void, Pair<Topic, TopicLine>> fetcher;

    Project project;

    public TopicLineRemoveAction(Project project, Function<Void, Pair<Topic, TopicLine>> fetcher)
    {
        super("Remove TopicLine", "RemoveTopicLine", AllIcons.General.Remove);
        this.project = project;
        this.fetcher = fetcher;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(e.getProject() != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        Pair<Topic, TopicLine> payload = fetcher.apply(null);

        if (payload.getSecond() == null) { return; }
        payload.getFirst().removeLine(payload.getSecond());
    }
}
