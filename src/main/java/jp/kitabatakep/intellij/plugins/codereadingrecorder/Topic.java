package jp.kitabatakep.intellij.plugins.codereadingrecorder;

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
        Collections.sort(lines);
    }

    public void addLine(TopicLine line)
    {
        if (lines.size() > 0) {
            line.setOrder(lines.get(lines.size()-1).order() + 1);
        } else {
            line.setOrder(0);
        }

        lines.add(line);
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
        Collections.sort(lines);
        return lines.iterator();
    }
}
