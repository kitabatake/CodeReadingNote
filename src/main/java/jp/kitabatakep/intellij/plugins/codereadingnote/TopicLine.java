package jp.kitabatakep.intellij.plugins.codereadingnote;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.pom.Navigatable;

import java.io.File;

public class TopicLine implements Navigatable
{
    private int line;
    private VirtualFile file;
    private String note;
    private Project project;
    private Topic topic;
    private boolean inProject;
    private String relativePath;
    private String url;

    public static TopicLine createByAction(Project project, Topic topic, VirtualFile file, int line, String note)
    {
        VirtualFile projectBase = LocalFileSystem.getInstance().findFileByPath(project.getBasePath());
        boolean inProject = VfsUtilCore.isAncestor(projectBase, file, true);

        return new TopicLine(project, topic, file, line, note, inProject,
            VfsUtilCore.getRelativePath(file, projectBase), file.getUrl());
    }

    public static TopicLine createByImport(Project project, Topic topic, String url, int line, String note, boolean inProject, String relativePath)
    {
        VirtualFile file;
        String projectBase = project.getBasePath();
        if (inProject) {
            file = LocalFileSystem.getInstance().findFileByPath(projectBase + File.separator + relativePath);
        } else {
            file = VirtualFileManager.getInstance().findFileByUrl(url);
        }
        return new TopicLine(project, topic, file, line, note, inProject, relativePath, url);
    }

    private TopicLine(Project project, Topic topic, VirtualFile file, int line, String note, boolean inProject, String relativePath, String url)
    {
        this.project = project;
        this.topic = topic;
        this.line = line;
        this.note = note;
        this.file = file;
        this.inProject = inProject;
        this.relativePath = relativePath;
        this.url = url;
    }

    public VirtualFile file()
    {
        return file;
    }

    public int line() { return line; }

    public String relativePath() { return relativePath; }

    public String note() { return note != null ? note : ""; }

    public void setNote(String note)
    {
        this.note = note;
        topic.touch();
    }

    public String url() { return url; }

    public String pathForDisplay()
    {
        if (inProject) {
            return relativePath;
        } else if (isValid()) {
            return file.getPath();
        } else {
            return url;
        }
    }

    public boolean inProject() { return inProject; }

    public boolean isValid() {
        return file != null && file.isValid();
    }

    private OpenFileDescriptor openFileDescriptor()
    {
        return new OpenFileDescriptor(project, file, line, -1, true);
    }

    @Override
    public boolean canNavigate() {
        return isValid() && openFileDescriptor().canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return openFileDescriptor().canNavigateToSource();
    }

    @Override
    public void navigate(boolean requestFocus) {
        if (canNavigate()) {
            openFileDescriptor().navigate(requestFocus);
        }
    }
}
