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
    private Date updatedAt;
    private ArrayList<TopicLine> lines = new ArrayList<>();
    private Project project;

    public Topic(Project project, int id, String name, Date updatedAt) {
        this.project = project;
        this.id = id;
        this.name = name;
        this.updatedAt = updatedAt;
    }

    public int id()
    {
        return id;
    }

    public String name()
    {
        return name;
    }

    public Date updatedAt() { return updatedAt; }

    @Override
    public int compareTo(@NotNull Topic topic)
    {
        return topic.updatedAt().compareTo(updatedAt);
    }

    public void addLine(TopicLine line)
    {
        lines.add(line);
        updatedAt = new Date();

        MessageBus messageBus = project.getMessageBus();
        TopicNotifier publisher = messageBus.syncPublisher(TopicNotifier.TOPIC_NOTIFIER_TOPIC);
        publisher.lineAdded(this, line);
    }

    public void deleteLine(TopicLine line)
    {
        lines.remove(line);
        updatedAt = new Date();

        MessageBus messageBus = project.getMessageBus();
        TopicNotifier publisher = messageBus.syncPublisher(TopicNotifier.TOPIC_NOTIFIER_TOPIC);
        publisher.lineDeleted(this, line);
    }

    public Iterator<TopicLine> linesIterator()
    {
        return lines.iterator();
    }
}
