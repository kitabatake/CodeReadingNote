package jp.kitabatakep.intellij.plugins.codereadingrecorder.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.CodeReadingRecorderService;

import org.jetbrains.annotations.NotNull;

public class ManagementViewOpenAction extends AnAction
{
    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(e.getProject() != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        Project project = e.getProject();
        if (project == null) return;

        CodeReadingRecorderService.getInstance(project).openPopup(e);
    }
}
