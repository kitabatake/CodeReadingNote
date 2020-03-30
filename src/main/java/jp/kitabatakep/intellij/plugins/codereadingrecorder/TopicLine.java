package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.NotNull;

public class TopicLine implements Comparable<TopicLine>, Navigatable
{
    private int line;
    private VirtualFile file;
    private String url;
    private int order;
    private String memo;
    private Project project;
    private Topic topic;

    public TopicLine(Project project, Topic topic, String url, int line, int order, String memo)
    {
        this.project = project;
        this.topic = topic;
        this.line = line;
        this.order = order;
        this.memo = memo;
        this.url = url;
        this.file = VirtualFileManager.getInstance().findFileByUrl(url);
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
        topic.touch();
    }

    public String memo() { return memo != null ? memo : ""; }

    public void setMemo(String memo)
    {
        this.memo = memo;
        topic.touch();
    }

    public String url()
    {
        if (isValid()) {
            return file.getUrl();
        } else {
            return url;
        }
    }

    public boolean isValid() {
        return file != null && file.isValid();
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
