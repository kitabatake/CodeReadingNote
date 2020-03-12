package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import java.util.ArrayList;
import java.util.Iterator;

public class Topic
{
    private int id;
    private String name;
    private ArrayList<TopicLine> lines = new ArrayList<>();

    public Topic(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int id()
    {
        return id;
    }

    public String name()
    {
        return name;
    }

    public void addLine(TopicLine line)
    {
        lines.add(line);
    }

    public Iterator<TopicLine> getLinesIterator()
    {
        return lines.iterator();
    }
}
