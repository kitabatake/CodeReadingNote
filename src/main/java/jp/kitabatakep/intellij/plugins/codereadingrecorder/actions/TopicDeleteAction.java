package jp.kitabatakep.intellij.plugins.codereadingrecorder.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.CodeReadingRecorderService;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.Topic;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class TopicDeleteAction extends AnAction
{
    Function<Void, Topic> topicFetcher;
    Project project;

    public TopicDeleteAction(Project project, Function<Void, Topic> topicFetcher)
    {
        super("DeleteTopic", "DeleteTopic", AllIcons.General.Remove);
        this.project = project;
        this.topicFetcher = topicFetcher;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(e.getProject() != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        CodeReadingRecorderService service = CodeReadingRecorderService.getInstance(project);
        Topic topic = topicFetcher.apply(null);

        if (topic == null) { return; }
        service.getTopicList().deleteTopic(topic);
    }
}
