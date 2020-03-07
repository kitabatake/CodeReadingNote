package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.ui.components.JBList;
import com.intellij.ui.popup.util.ItemWrapper;
import com.intellij.ui.popup.util.MasterDetailPopupBuilder;
import com.intellij.util.ArrayUtilRt;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.actions.TopicAddAction;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.actions.TopicClearAction;
import jp.kitabatakep.intellij.plugins.codereadingrecorder.ui.TopicItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;

public class ManagementViewService implements MasterDetailPopupBuilder.Delegate
{
    private static Project project;
    private static final String DIMENSION_SERVICE_KEY = AppConstants.appName;
    private JBPopup myPopup;

    public static ManagementViewService getInstance(@NotNull Project project)
    {
        ManagementViewService.project = project;
        return ServiceManager.getService(project, ManagementViewService.class);
    }

    public void open(@NotNull AnActionEvent e)
    {
        if (myPopup != null && myPopup.isVisible()) {
            myPopup.cancel();
        }

        DefaultActionGroup actions = new DefaultActionGroup();
        actions.add(new TopicAddAction());
        actions.add(new TopicClearAction());

        DefaultListModel<TopicItem> model = buildModel(project);
        JBList<TopicItem> list = new JBList<>(model);

        JBPopup popup = new MasterDetailPopupBuilder(project).
            setList(list).
            setDelegate(this).
            setDimensionServiceKey(DIMENSION_SERVICE_KEY).
            setAddDetailViewToEast(true).
            setActionsGroup(actions).
            setPopupTuner(builder -> builder.setCloseOnEnter(false).setCancelOnClickOutside(false)).
            setDoneRunnable(() -> { if (myPopup != null) myPopup.cancel(); }).
            createMasterDetailPopup();

        myPopup = popup;

        popup.showInBestPositionFor(e.getDataContext());

        list.getEmptyText().setText("No Topic");
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    private static DefaultListModel<TopicItem> buildModel(Project project)
    {
        DefaultListModel<TopicItem> model = new DefaultListModel<>();
        TopicListService topicListService = TopicListService.getInstance(project);
        topicListService.topicsStream().forEach(topic -> {
                model.addElement(new TopicItem(topic));
            });
        return model;
    }

    @Override
    public String getTitle() {
        return AppConstants.appName;
    }

    @Override
    public void handleMnemonic(KeyEvent e, Project project, JBPopup popup) {

    }

    @Override
    @Nullable
    public JComponent createAccessoryView(Project project) {
        return null;
    }

    @Override
    public Object[] getSelectedItemsInTree() {
        return ArrayUtilRt.EMPTY_OBJECT_ARRAY;
    }

    @Override
    public void itemChosen(ItemWrapper item, Project project, JBPopup popup, boolean withEnterOrDoubleClick) {

    }

    @Override
    public void removeSelectedItemsInTree() { }
}
