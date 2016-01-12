package org.bitchouse.ideastock.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import java.util.ArrayList;
import org.bitchouse.ideastock.Ticker;
import org.bitchouse.ideastock.misc.Log;
import org.jetbrains.annotations.Nullable;

/**
 * Created by bitchouse
 */
@State(
    name = "StockSetting",
    storages = {
        @Storage(
            file = StoragePathMacros.APP_CONFIG +
                "/stock_settings.xml"
        )}
)
public class Setting implements PersistentStateComponent<Setting.State> {

  public interface WatchListListener {
    void onWatchListChanged(ArrayList<Ticker> list);
  }

  public static class State {
    public ArrayList<Ticker> watchList = new ArrayList<>();
  }

  private ArrayList<WatchListListener> mWatchListListeners = new
      ArrayList<>();

  private State mState = new State();

  @Nullable
  @Override
  public State getState() {
    Log.d("setting getState");
    return mState;
  }

  @Override
  public void loadState(State state) {
    Log.d("setting loadState");
    mState = state;
  }

  public static Setting getInstance() {
    return ServiceManager.getService(Setting.class);
  }

  public void addWatchListListener(WatchListListener listener) {
    mWatchListListeners.add(listener);
  }

  public void removeWatchListListener(WatchListListener listener) {
    mWatchListListeners.remove(listener);
  }

  public void updateList(ArrayList<Ticker> list) {
    mState.watchList.clear();
    mState.watchList.addAll(list);
    for (WatchListListener listener : mWatchListListeners) {
      listener.onWatchListChanged(mState.watchList);
    }
  }

  public ArrayList<Ticker> getWatchList() {
    return new ArrayList<>(mState.watchList);
  }
}
