package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.popup.util.DetailView;
import com.intellij.ui.popup.util.ItemWrapper;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.Topic;

import javax.swing.*;

public class TopicItem extends ItemWrapper
{
    private final Topic myTopic;
    public TopicItem(Topic bookmark) {
        myTopic = bookmark;
    }

    public Topic getTopic() {
        return myTopic;
    }

    @Override
    public void setupRenderer(ColoredListCellRenderer renderer, Project project, boolean selected) {
        setupRenderer(renderer, project, myTopic, selected);
    }

    @Override
    public void setupRenderer(ColoredTreeCellRenderer renderer, Project project, boolean selected) {
        setupRenderer(renderer, project, myTopic, selected);
    }

    public static void setupRenderer(SimpleColoredComponent renderer, Project project, Topic topic, boolean selected)
    {
        renderer.append(String.format("%s(%d)", topic.name(), topic.id()));
    }

    @Override
    public void updateAccessoryView(JComponent component) {}

    @Override
    public String speedSearchText() {
        return myTopic.name();
    }

    @Override
    public String footerText() {
        return myTopic.name();
    }

    @Override
    protected void doUpdateDetailView(DetailView panel, boolean editorOnly) {}

    @Override
    public boolean allowedToRemove() {
        return true;
    }

    @Override
    public void removed(Project project) {

    }
}
