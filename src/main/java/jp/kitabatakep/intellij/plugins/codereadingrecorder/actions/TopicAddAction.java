package jp.kitabatakep.intellij.plugins.codereadingrecorder.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.CodeReadingRecorderService;
import org.jetbrains.annotations.NotNull;

public class TopicAddAction extends AnAction
{
    public TopicAddAction() {
        super("New Topic", "NewTopic", AllIcons.General.Add);
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
            new InputValidator()
            {
                @Override
                public boolean checkInput(String inputString) { return !inputString.trim().equals(""); }

                @Override
                public boolean canClose(String inputString) { return true; }
            }
        );

        CodeReadingRecorderService service = CodeReadingRecorderService.getInstance(e.getProject());
        service.getTopicList().addTopic(newTopicName);
    }
}
