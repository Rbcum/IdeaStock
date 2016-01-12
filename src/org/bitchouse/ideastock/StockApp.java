package org.bitchouse.ideastock;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.DefaultLogger;
import com.intellij.openapi.diagnostic.Logger;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import org.bitchouse.ideastock.misc.Log;
import org.bitchouse.ideastock.provider.SinaProvider;
import org.bitchouse.ideastock.setting.Setting;
import org.jetbrains.annotations.NotNull;

/**
 * Created by bitchouse
 */
public class StockApp implements ApplicationComponent, Setting
    .WatchListListener {

  private static final long REFRESH_INTERVAL = 3 * 1000;

  public interface TickerWatcher {
    /**
     * @param list Caller should not modify this list
     */
    void onUpdate(ArrayList<Ticker> list);
  }

  ScheduledThreadPoolExecutor mExecutor;
  ArrayList<Ticker> mTickerList;
  String[] mCodeList;
  ArrayList<TickerWatcher> mWatcherList = new ArrayList<>();

  public StockApp() {
  }

  public static StockApp getInstance() {
    return ApplicationManager.getApplication().getComponent(StockApp.class);
  }

  public void initComponent() {
    Log.d("Application component inited");

    mTickerList = Setting.getInstance().getWatchList();
    updateCodeList();
    Setting.getInstance().addWatchListListener(this);

    refresh();
  }

  public void updateCodeList() {
    String[] codes = new String[mTickerList.size()];
    ArrayList<Ticker> watchList = mTickerList;
    for (int i = 0; i < watchList.size(); i++) {
      Ticker ticker = watchList.get(i);
      codes[i] = ticker.getCode();
    }
    mCodeList = codes;
  }

  static class Fac implements Logger.Factory {
    @Override
    public Logger getLoggerInstance(String category) {
      return new DefaultLogger("");
    }
  }

  @Override
  public void onWatchListChanged(ArrayList<Ticker> list) {
    mTickerList = list;
    updateCodeList();
  }

  public void disposeComponent() {
    Setting.getInstance().removeWatchListListener(this);
  }

  @NotNull
  public String getComponentName() {
    return getClass().getName();
  }

  public void addTickerWatcher(TickerWatcher watcher) {
    mWatcherList.add(watcher);
  }

  public void removeTickerWatcher(TickerWatcher watcher) {
    mWatcherList.remove(watcher);
  }

  private void refresh() {
    mExecutor = new ScheduledThreadPoolExecutor(1);
    mExecutor.scheduleWithFixedDelay(new RefreshRunner(), 0,
        REFRESH_INTERVAL, TimeUnit.MILLISECONDS);
  }

  private void pause() {
    if (mExecutor != null) {
      mExecutor.shutdown();
      mExecutor = null;
    }
  }

  class RefreshRunner implements Runnable {

    @Override
    public void run() {
      SinaProvider provider = new SinaProvider();
      ArrayList<Ticker> list = provider.get(mCodeList);
      if (list == null) {
        return;
      }
      SwingUtilities.invokeLater(() -> {
        for (TickerWatcher watcher : mWatcherList) {
          watcher.onUpdate(list);
        }
      });
    }
  }
}
