package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import jp.kitabatakep.intellij.plugins.codereadingrecorder.Topic;

import javax.swing.*;
import java.awt.*;

public class TopicDetailPanel extends JPanel
{
    private final JLabel myLabel = new JLabel();

    public TopicDetailPanel() {
        super(new BorderLayout());
        add(myLabel);
    }

    void setTopic(Topic topic)
    {
        myLabel.setText(topic.name());
    }
}
