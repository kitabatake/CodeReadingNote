package jp.kitabatakep.intellij.plugins.codereadingrecorder.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.Topic;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicLine;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class TopicLineDeleteAction extends AnAction
{
    Function<Void, Pair<Topic, TopicLine>> fetcher;

    Project project;

    public TopicLineDeleteAction(Project project, Function<Void, Pair<Topic, TopicLine>> fetcher)
    {
        super("DeleteTopicLine", "DeleteTopicLine", AllIcons.General.Remove);
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
        payload.getFirst().deleteLine(payload.getSecond());
    }
}
