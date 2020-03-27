package jp.kitabatakep.intellij.plugins.codereadingrecorder.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.AppConstants;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.CodeReadingRecorderService;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class LoadAction extends AnAction
{
    public LoadAction() {
        super("Load", "Load", AllIcons.General.ArrowUp);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(e.getProject() != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        Project project = e.getProject();

        VirtualFile homeDir = LocalFileSystem.getInstance().findFileByPath(System.getProperty("user.home"));

        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(true, false, false, false, false, false);

        VirtualFile[] files = FileChooserFactory.getInstance().
            createFileChooser(fileChooserDescriptor, project, null).choose(project, homeDir);


        File file = new File(files[0].getPath());
        SAXBuilder builder = new SAXBuilder();

        Document document = null;
        try {
             document = builder.build((file));
        } catch (JDOMException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        CodeReadingRecorderService service = CodeReadingRecorderService.getInstance(e.getProject());
        service.getTopicList().loadState(document.getRootElement());
    }
}
