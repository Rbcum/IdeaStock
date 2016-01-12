package org.bitchouse.ideastock.setting;

import com.intellij.openapi.actionSystem.ActionToolbarPosition;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.bitchouse.ideastock.Ticker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by bitchouse
 */
public class SettingDialog extends DialogWrapper {

  private final Setting mSetting;
  private final CollectionListModel<Ticker> mModel;
  private final JBTextField mInputField;
  private final ArrayList<Ticker> mWatchList;
  private JBList mWatchListView;
  private int mSelectIndex = -1;

  public SettingDialog(Component component) {
    super((Project) null, true);
    mSetting = Setting.getInstance();
    mWatchList = mSetting.getWatchList();
    mModel = new CollectionListModel<>(mWatchList);
    mModel.addListDataListener(new ListDataListener() {
      @Override
      public void intervalAdded(ListDataEvent e) {

      }

      @Override
      public void intervalRemoved(ListDataEvent e) {

      }

      @Override
      public void contentsChanged(ListDataEvent e) {
      }
    });
    mWatchListView = new JBList(mModel);
    mWatchListView.setCellRenderer(new ExtensionRenderer());
    mWatchListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    mWatchListView.addListSelectionListener(e ->
        mSelectIndex = mWatchListView.getSelectedIndex());

    mInputField = new JBTextField();
    mInputField.setToolTipText("Enter ticker code here");
    setTitle("Settings");
    init();
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    JBPanel container = new JBPanel(new BorderLayout());
    container.add(mInputField, BorderLayout.NORTH);
    ToolbarDecorator listDecorator = ToolbarDecorator.createDecorator
        (mWatchListView)
        .setAddAction(button -> {
          String text = mInputField.getText();
          if (StringUtil.isEmpty(text)) {
            return;
          }
          Ticker ticker = new Ticker(text, text);
          mModel.add(ticker);
          mWatchList.add(ticker);
        })
        .setRemoveAction(button -> {
          if (mSelectIndex != -1) {
            int index = mSelectIndex;
            mModel.remove(index);
            mWatchList.remove(index);
          }
        })
        .setMoveUpAction(anActionButton -> {
          swapTicker(mSelectIndex, mSelectIndex - 1);
        })
        .setMoveDownAction(anActionButton -> {
          swapTicker(mSelectIndex, mSelectIndex + 1);
        })
        .setToolbarPosition(ActionToolbarPosition.RIGHT);
    container.add(listDecorator.createPanel(), BorderLayout.CENTER);
    container.setBorder(IdeBorderFactory.createTitledBorder("Watch List",
        false));
    return container;
  }

  private void swapTicker(int oldIndex, int newIndex) {
    if (oldIndex == newIndex) {
      return;
    }
    if (oldIndex < 0 || oldIndex > mWatchList.size() || newIndex < 0 ||
        newIndex > mWatchList.size()) {
      return;
    }
    Collections.swap(mWatchList, oldIndex, newIndex);
    mModel.setElementAt(mWatchList.get(oldIndex), oldIndex);
    mModel.setElementAt(mWatchList.get(newIndex), newIndex);
    mWatchListView.setSelectedIndex(newIndex);
  }

  private static class ExtensionRenderer extends DefaultListCellRenderer {
    @NotNull
    @Override
    public Component getListCellRendererComponent(@NotNull JList list,
        Object value, int index, boolean isSelected,
        boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index,
          isSelected, cellHasFocus);
      setText(" " + ((Ticker) value).getCode());
      return this;
    }

    @Override
    public Dimension getPreferredSize() {
      return new Dimension(0, 20);
    }
  }

  @Nullable
  @Override
  protected String getDimensionServiceKey() {
    return getClass().getName();
  }

  @NotNull
  @Override
  protected Action[] createActions() {
    ArrayList<Action> actions = new ArrayList<Action>();
    actions.add(getOKAction());
    actions.add(getCancelAction());
    return actions.toArray(new Action[actions.size()]);
  }

  @Override
  protected void doOKAction() {
    super.doOKAction();
    mSetting.updateList(mWatchList);
  }

  @Override
  public void doCancelAction() {
    super.doCancelAction();
  }

  private void createUIComponents() {
    mWatchListView = new JBList();
  }
}
