package jp.kitabatakep.intellij.plugins.codereadingnote;

import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

public class Topic implements Comparable<Topic>
{
    private String name;
    private String note;
    private Date updatedAt;
    private ArrayList<TopicLine> lines = new ArrayList<>();
    private Project project;

    public Topic(Project project, String name, Date updatedAt) {
        this.project = project;
        this.name = name;
        this.updatedAt = updatedAt;
    }

    public String name()
    {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public String note() {
        return note != null ? note : "";
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public Date updatedAt() { return updatedAt; }

    public void touch()
    {
        updatedAt = new Date();
    }

    @Override
    public int compareTo(@NotNull Topic topic)
    {
        return topic.updatedAt().compareTo(updatedAt);
    }

    public void setLines(ArrayList<TopicLine> lines)
    {
        this.lines = lines;
    }

    public void addLine(TopicLine line)
    {
        lines.add(line);
        updatedAt = new Date();

        MessageBus messageBus = project.getMessageBus();
        TopicNotifier publisher = messageBus.syncPublisher(TopicNotifier.TOPIC_NOTIFIER_TOPIC);
        publisher.lineAdded(this, line);
    }

    public void removeLine(TopicLine line)
    {
        lines.remove(line);
        updatedAt = new Date();

        MessageBus messageBus = project.getMessageBus();
        TopicNotifier publisher = messageBus.syncPublisher(TopicNotifier.TOPIC_NOTIFIER_TOPIC);
        publisher.lineRemoved(this, line);
    }

    public Iterator<TopicLine> linesIterator()
    {
        return lines.iterator();
    }

    public void changeLineOrder(TopicLine line, int index)
    {
        lines.remove(line);
        lines.add(index, line);
    }
}
