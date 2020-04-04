package jp.kitabatakep.intellij.plugins.codereadingnote.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import jp.kitabatakep.intellij.plugins.codereadingnote.CodeReadingNoteService;
import jp.kitabatakep.intellij.plugins.codereadingnote.Topic;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class TopicRemoveAction extends AnAction
{
    Function<Void, Topic> topicFetcher;
    Project project;

    public TopicRemoveAction(Project project, Function<Void, Topic> topicFetcher)
    {
        super("Remove Topic", "RemoveTopic", AllIcons.General.Remove);
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
        CodeReadingNoteService service = CodeReadingNoteService.getInstance(project);
        Topic topic = topicFetcher.apply(null);
        if (topic == null) { return; }

        int confirmationResult = Messages.showYesNoCancelDialog(
            "Are you sure you want to remove `" + topic.name() + "`",
            "TopicRemoveConfirmation",
            Messages.getQuestionIcon()
        );

        if (confirmationResult == Messages.YES) {
            service.getTopicList().removeTopic(topic);
        }
    }
}
