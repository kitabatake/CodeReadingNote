package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class TopicLine implements Comparable<TopicLine>
{
    private int line;
    private VirtualFile file;
    private int order;

    public TopicLine(VirtualFile file, int line)
    {
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

    @Override
    public int compareTo(@NotNull TopicLine topicLine)
    {
        return order <= topicLine.order() ? -1 : 1;
    }
}
