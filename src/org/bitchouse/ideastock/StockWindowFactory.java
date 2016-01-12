package org.bitchouse.ideastock;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerAdapter;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.table.JBTable;
import java.util.ArrayList;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import org.bitchouse.ideastock.setting.SettingDialog;
import org.jetbrains.annotations.NotNull;

/**
 * Created by bitchouse
 */
public class StockWindowFactory implements ToolWindowFactory, DumbAware {

  @Override
  public void createToolWindowContent(Project project, final ToolWindow
      toolWindow) {
    ((ToolWindowManagerEx) ToolWindowManager.getInstance(project))
        .addToolWindowManagerListener(new ToolWindowManagerAdapter() {
          @Override
          public void stateChanged() {
          }
        });
    TickerTableModel model = new TickerTableModel();
    JBTable table = new JBTable(model);
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    renderer.setHorizontalAlignment(SwingConstants.CENTER);
    table.setDefaultRenderer(Object.class, renderer);

    SimpleToolWindowPanel panel = new SimpleToolWindowPanel(true, true);
    //Add the scroll pane to this panel.
    panel.setContent(ScrollPaneFactory.createScrollPane(table));
    ActionToolbar toolbar = createToolbar(toolWindow, panel);
    toolbar.setTargetComponent(panel);
    panel.setToolbar(toolbar.getComponent());

    Content content = ContentFactory.SERVICE.getInstance().createContent
        (panel, "", false);
    content.setComponent(panel);

    ContentManager contentManager = toolWindow.getContentManager();
    contentManager.addContent(content);

    StockApp.TickerWatcher watcher = new StockApp.TickerWatcher() {
      @Override
      public void onUpdate(ArrayList<Ticker> list) {
        model.updateData(list);
      }
    };

    StockApp.getInstance().addTickerWatcher(watcher);

    Disposer.register(project, new Disposable() {
      @Override
      public void dispose() {
        StockApp.getInstance().removeTickerWatcher(watcher);
      }
    });
  }

  private ActionToolbar createToolbar(@NotNull ToolWindow toolWindow,
      SimpleToolWindowPanel panel) {

    AnAction settingAction = new AnAction("Settings", "Settings",
        AllIcons.General.Settings) {
      @Override
      public void actionPerformed(AnActionEvent e) {
        new SettingDialog(toolWindow.getComponent()).show();
      }
    };

    DefaultActionGroup group = new DefaultActionGroup(
        settingAction
    );

    return ActionManager.getInstance().createActionToolbar(ActionPlaces
        .UNKNOWN, group, true);
  }
}
