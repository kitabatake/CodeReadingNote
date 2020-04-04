package jp.kitabatakep.intellij.plugins.codereadingnote.ui;

import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.ui.EditableModel;
import com.intellij.util.ui.UIUtil;
import jp.kitabatakep.intellij.plugins.codereadingnote.AppConstants;
import jp.kitabatakep.intellij.plugins.codereadingnote.Topic;
import jp.kitabatakep.intellij.plugins.codereadingnote.TopicLine;
import jp.kitabatakep.intellij.plugins.codereadingnote.TopicNotifier;
import jp.kitabatakep.intellij.plugins.codereadingnote.actions.TopicLineRemoveAction;
import javax.swing.*;
import com.intellij.openapi.editor.event.DocumentListener;
import org.intellij.plugins.markdown.lang.MarkdownFileType;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

class TopicDetailPanel extends JPanel
{
    private Project project;
    private MyEditorTextField noteArea;
    private TopicLineDetailPanel topicLineDetailPanel;

    private JBList<TopicLine> topicLineList;
    private TopicLineListModel<TopicLine> topicLineListModel = new TopicLineListModel<>();

    private Topic topic;
    private TopicLine selectedTopicLine;

    public TopicDetailPanel(Project project)
    {
        super(new BorderLayout());

        this.project = project;

        JBSplitter contentPane = new JBSplitter(true, 0.2f);
        contentPane.setSplitterProportionKey(AppConstants.appName + "TopicDetailPanelContentPane.splitter");

        noteArea = new MyEditorTextField(project, MarkdownFileType.INSTANCE);
        noteArea.setOneLineMode(false);
        noteArea.setEnabled(false);

        contentPane.setFirstComponent(noteArea);

        initTopicLineList();
        topicLineDetailPanel = new TopicLineDetailPanel(project);

        JBSplitter topicLinePane = new JBSplitter(0.2f);
        topicLinePane.setSplitterProportionKey(AppConstants.appName + "TopicDetailPanelTopicLinePane.splitter");
        topicLinePane.setFirstComponent(topicLineList);
        topicLinePane.setSecondComponent(topicLineDetailPanel);
        topicLinePane.setHonorComponentsMinimumSize(false);

        contentPane.setSecondComponent(topicLinePane);
        add(contentPane);

        MessageBus messageBus = project.getMessageBus();
        messageBus.connect().subscribe(TopicNotifier.TOPIC_NOTIFIER_TOPIC, new TopicNotifier(){
            @Override
            public void lineRemoved(Topic _topic, TopicLine _topicLine)
            {
                if (_topic == topic) {
                    topicLineListModel.removeElement(_topicLine);
                }
            }

            @Override
            public void lineAdded(Topic _topic, TopicLine _topicLine)
            {
                if (_topic == topic) {
                    topicLineListModel.addElement(_topicLine);
                }
            }
        });
    }

    @Override
    public void removeNotify()
    {
        super.removeNotify();
        if (noteArea.getEditor() != null) {
            EditorFactory.getInstance().releaseEditor(noteArea.getEditor());
        }
    }

    private static class NoteAreaListener implements DocumentListener
    {
        TopicDetailPanel topicDetailPanel;

        private NoteAreaListener(TopicDetailPanel topicDetailPanel)
        {
            this.topicDetailPanel = topicDetailPanel;
        }

        public void documentChanged(com.intellij.openapi.editor.event.DocumentEvent e)
        {
            Document doc = e.getDocument();
            topicDetailPanel.topic.setNote(doc.getText());
        }
    }

    private void initTopicLineList()
    {
        topicLineList = new JBList<>();
        topicLineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        topicLineList.setCellRenderer(new TopicLineListCellRenderer<>(project));
        topicLineList.addListSelectionListener(e -> {
            TopicLine topicLine = topicLineList.getSelectedValue();
            if (topicLine == null) {
                topicLineDetailPanel.clear();
            } else if (selectedTopicLine == null || topicLine != selectedTopicLine) {
                selectedTopicLine = topicLine;
                topicLineDetailPanel.setTopicLine(topicLine);
            }
        });

        topicLineList.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() >= 2) {
                    int index = topicLineList.locationToIndex(e.getPoint());
                    TopicLine topicLine = topicLineListModel.get(index);
                    topicLine.navigate(true);
                }
            }
        });

        topicLineList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    TopicLine topicLine = topicLineList.getSelectedValue();
                    ActionUtil.performActionDumbAware(
                        new TopicLineRemoveAction(project, (v) -> { return new Pair<>(topic, topicLine); }),
                        ActionUtil.createEmptyEvent()
                    );
                }
            }
        });

        topicLineList.setDragEnabled(true);
        RowsDnDSupport.install(topicLineList, topicLineListModel);
    }

    void clear()
    {
        noteArea.setDocument(EditorFactory.getInstance().createDocument(""));
        noteArea.setEnabled(false);
        topicLineListModel.clear();
        topicLineDetailPanel.clear();
        selectedTopicLine = null;
    }

    void setTopic(Topic topic)
    {
        this.topic = topic;
        selectedTopicLine = null;

        noteArea.setEnabled(true);
        if (topic.note().equals("")) {
            noteArea.setPlaceholder("topic note input area");
        }
        noteArea.setDocument(EditorFactory.getInstance().createDocument(topic.note()));
        noteArea.getDocument().addDocumentListener(new NoteAreaListener(this));

        topicLineListModel.clear();
        Iterator<TopicLine> iterator = topic.linesIterator();
        while (iterator.hasNext()) {
            topicLineListModel.addElement(iterator.next());
        }

        topicLineList.setModel(topicLineListModel);
    }

    private static class TopicLineListCellRenderer<T> extends SimpleColoredComponent implements ListCellRenderer<T>
    {
        private Project project;

        private TopicLineListCellRenderer(Project project)
        {
            this.project = project;
            setOpaque(true);
        }

        public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
        {
            clear();
            TopicLine topicLine = (TopicLine) value;
            VirtualFile file = topicLine.file();

            PsiElement fileOrDir = PsiUtilCore.findFileSystemItem(project, file);
            if (fileOrDir != null) {
                setIcon(fileOrDir.getIcon(0));
            }

            if (topicLine.isValid()) {
                append(file.getName() + ":" + (topicLine.line()+1));
                append(" (" + topicLine.pathForDisplay() + ")", SimpleTextAttributes.GRAY_ATTRIBUTES);
            } else {
                append(topicLine.pathForDisplay() + ":" + (topicLine.line()+1), SimpleTextAttributes.ERROR_ATTRIBUTES);
            }

            setForeground(UIUtil.getListSelectionForeground(isSelected));
            setBackground(UIUtil.getListSelectionBackground(isSelected));
            return this;
        }
    }

    private static class TopicLineListModel<T> extends DefaultListModel<T> implements EditableModel
    {
        public void addRow() {}
        public void removeRow(int i) {}
        public boolean canExchangeRows(int oldIndex, int newIndex) { return true; }

        public void exchangeRows(int oldIndex, int newIndex)
        {
            TopicLine a = (TopicLine) get(oldIndex);
            a.setOrder(newIndex);

            TopicLine b = (TopicLine) get(newIndex);
            b.setOrder(oldIndex);

            set(newIndex, (T)a);
            set(oldIndex, (T)b);
        }
    }
}
