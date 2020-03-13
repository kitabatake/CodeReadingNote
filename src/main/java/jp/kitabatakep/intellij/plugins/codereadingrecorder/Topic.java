package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Topic
{
    private int id;
    private String name;
    private Date createdAt;
    private ArrayList<TopicLine> lines = new ArrayList<>();

    public Topic(int id, String name, Date createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public int id()
    {
        return id;
    }

    public String name()
    {
        return name;
    }

    public Date createdAt() { return createdAt; }

    public void addLine(TopicLine line)
    {
        lines.add(line);
    }

    public Iterator<TopicLine> getLinesIterator()
    {
        return lines.iterator();
    }
}
