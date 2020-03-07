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

    public int getId()
    {
        return id;
    }

    public String getName()
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
