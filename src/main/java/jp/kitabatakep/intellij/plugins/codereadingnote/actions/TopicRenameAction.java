package jp.kitabatakep.intellij.plugins.codereadingnote.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import jp.kitabatakep.intellij.plugins.codereadingnote.Topic;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class TopicRenameAction extends AnAction
{
    Function<Void, Topic> topicFetcher;

    public TopicRenameAction(Function<Void, Topic> topicFetcher) {
        super("Rename Topic", "RenameTopic", AllIcons.Actions.Edit);
        this.topicFetcher = topicFetcher;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(
            e.getProject() != null &&
                topicFetcher.apply(null) != null
        );
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        Topic topic = topicFetcher.apply(null);
        if (topic == null) { return; }

        String newTopicName =  Messages.showInputDialog(
            "Enter Topic name",
            "Edit Topic Name",
            Messages.getQuestionIcon(),
            topic.name(),
            new InputValidator()
            {
                @Override
                public boolean checkInput(String inputString) { return !inputString.trim().equals(""); }

                @Override
                public boolean canClose(String inputString) { return true; }
            }
        );

        topic.setName(newTopicName);
    }
}
