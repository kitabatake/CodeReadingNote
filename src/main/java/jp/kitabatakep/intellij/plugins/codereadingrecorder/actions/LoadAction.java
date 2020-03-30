package jp.kitabatakep.intellij.plugins.codereadingrecorder.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.util.messages.MessageBus;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.AppConstants;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.CodeReadingRecorderService;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicListNotifier;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class LoadAction extends AnAction
{
    public LoadAction() {
        super("Import", "Import", AllIcons.ToolbarDecorator.Import);
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

        FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor("xml");
        VirtualFile[] files = FileChooserFactory.getInstance().
            createFileChooser(fileChooserDescriptor, project, null).
            choose(project, homeDir);

        if (files.length == 0) {
            return;
        }

        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        try {
             document = builder.build(new File(files[0].getPath()));
        } catch (JDOMException ex) {
            Messages.showErrorDialog(project, "Fail to load action caused by specified illegal format file.", AppConstants.appName + "Load");
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Fail to load action. Please try again.", AppConstants.appName + "Load");
            return;
        }

        CodeReadingRecorderService service = CodeReadingRecorderService.getInstance(project);
        service.getTopicList().loadState(document.getRootElement());

        MessageBus messageBus = project.getMessageBus();
        TopicListNotifier publisher = messageBus.syncPublisher(TopicListNotifier.TOPIC_LIST_NOTIFIER_TOPIC);
        publisher.topicsLoaded();
    }
}
