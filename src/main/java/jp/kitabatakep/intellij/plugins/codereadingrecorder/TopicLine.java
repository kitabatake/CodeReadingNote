package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.NotNull;

public class TopicLine implements Comparable<TopicLine>, Navigatable
{
    private int line;
    private VirtualFile file;
    private int order;
    private Project project;

    public TopicLine(Project project, VirtualFile file, int line)
    {
        this.project = project;
        this.file = file;
        this.line = line;
    }

    public VirtualFile file()
    {
        return file;
    }

    public int line() { return line; }

    public int order() { return order; }
    public void setOrder(int order)
    {
        this.order = order;
    }

    private OpenFileDescriptor openFileDescriptor()
    {
        return new OpenFileDescriptor(project, file, line, -1, true);
    }

    @Override
    public int compareTo(@NotNull TopicLine topicLine)
    {
        return order <= topicLine.order() ? -1 : 1;
    }

    @Override
    public boolean canNavigate() {
        return openFileDescriptor().canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return openFileDescriptor().canNavigateToSource();
    }

    @Override
    public void navigate(boolean requestFocus) {
        openFileDescriptor().navigate(requestFocus);
    }
}
