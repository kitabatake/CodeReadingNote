package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBList;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.AppConstants;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.Topic;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.TopicLine;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

class TopicDetailPanel extends JPanel
{
    private final JLabel myLabel = new JLabel();

    private JBList<TopicLine> topicLineList;
    private DefaultListModel<TopicLine> topicLineListModel;

    public TopicDetailPanel()
    {
        super(new BorderLayout());
        topicLineList = new JBList<>();

        JBSplitter splitPane = new JBSplitter(0.3f);
        splitPane.setSplitterProportionKey(AppConstants.appName + "TopicDetailPanel.splitter");
        splitPane.setFirstComponent(topicLineList);
        splitPane.setSecondComponent(myLabel);
        add(splitPane);
    }

    void setTopic(Topic topic)
    {
        myLabel.setText(topic.name());

        topicLineListModel = new DefaultListModel<>();
        Iterator<TopicLine> iterator = topic.getLinesIterator();
        while (iterator.hasNext()) {
            topicLineListModel.addElement(iterator.next());
        }

        topicLineList.setModel(topicLineListModel);
    }
}
