package jp.kitabatakep.intellij.plugins.codereadingrecorder.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.Topic;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicListService;
import org.jetbrains.annotations.NotNull;

public class NewTopicDialogOpenAction extends AnAction
{
    public NewTopicDialogOpenAction() {
        super("add Topic", "add topic", AllIcons.General.Add);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(e.getProject() != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        String newTopicName =  Messages.showInputDialog(
            "Enter Topic name",
            "Create New Topic",
            Messages.getQuestionIcon(),
            "",
            null
        );

        TopicListService topicListService = TopicListService.getInstance(e.getProject());
        topicListService.addTopic(new Topic(newTopicName));
    }
}
