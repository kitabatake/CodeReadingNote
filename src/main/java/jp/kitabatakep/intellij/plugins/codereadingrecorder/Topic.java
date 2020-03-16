package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Topic implements Comparable<Topic>
{
    private int id;
    private String name;
    private Date createdAt;
    private ArrayList<TopicLine> lines = new ArrayList<>();
    private Project project;

    public Topic(Project project, int id, String name, Date createdAt) {
        this.project = project;
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

    @Override
    public int compareTo(@NotNull Topic topic)
    {
        return topic.createdAt().compareTo(createdAt);
    }

    public void addLine(TopicLine line)
    {
        lines.add(line);
    }

    public void deleteLine(TopicLine line)
    {
        lines.remove(line);

        MessageBus messageBus = project.getMessageBus();
        TopicNotifier publisher = messageBus.syncPublisher(TopicNotifier.TOPIC_NOTIFIER_TOPIC);
        publisher.lineDeleted(this, line);


    }

    public Iterator<TopicLine> linesIterator()
    {
        return lines.iterator();
    }
}
