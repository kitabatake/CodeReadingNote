package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.util.ui.JBUI;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.Topic;

import javax.swing.*;
import java.awt.*;

public class TopicDetailPanel extends JPanel
{
    private final JLabel myLabel = new JLabel("hoge", SwingConstants.CENTER);

    public TopicDetailPanel() {
        super(new BorderLayout());
        setPreferredSize(JBUI.size(600, 300));
        myLabel.setVerticalAlignment(SwingConstants.CENTER);
//        myLabel.setText(topic.getName());
    }
}
