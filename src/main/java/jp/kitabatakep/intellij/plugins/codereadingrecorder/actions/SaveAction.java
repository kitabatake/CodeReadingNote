package jp.kitabatakep.intellij.plugins.codereadingrecorder.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.AppConstants;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.CodeReadingRecorderService;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class SaveAction extends AnAction
{
    public SaveAction() {
        super("Save", "Save", AllIcons.General.ArrowDown);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(e.getProject() != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        Project project = e.getProject();
        FileSaverDescriptor fsd = new FileSaverDescriptor("Save", "Please choose where to save", "xml");

        VirtualFile homeDir = LocalFileSystem.getInstance().findFileByPath(System.getProperty("user.home"));
        final VirtualFileWrapper wrapper = FileChooserFactory.getInstance().createSaveFileDialog(
            fsd, project).save(homeDir, AppConstants.appName + ".xml");

        if (wrapper == null) {
            return;
        }

        File file = wrapper.getFile();

        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
        }

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return;
        }

        CodeReadingRecorderService service = CodeReadingRecorderService.getInstance(e.getProject());

        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());

        Element state = service.getTopicList().getState();

        try {
            xmlOutput.output(new Document(state), fileOutputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
