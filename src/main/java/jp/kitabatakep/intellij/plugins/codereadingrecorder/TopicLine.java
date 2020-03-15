package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import com.intellij.openapi.vfs.VirtualFile;

public class TopicLine
{
    private int line;
    private VirtualFile file;

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


}
