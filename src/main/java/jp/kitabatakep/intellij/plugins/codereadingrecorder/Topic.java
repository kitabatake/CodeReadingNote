package jp.kitabatakep.intellij.plugins.codereadingrecorder;

public class Topic
{
    private int id;
    private String name;

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
}
