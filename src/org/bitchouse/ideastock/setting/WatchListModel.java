package org.bitchouse.ideastock.setting;

import com.intellij.ui.CollectionListModel;
import java.util.ArrayList;
import org.bitchouse.ideastock.Ticker;

/**
 * Created by bitchouse
 */
public class WatchListModel extends CollectionListModel<Ticker> {

  private final ArrayList<Ticker> mList;

  WatchListModel(ArrayList<Ticker> list) {
    mList = list;
  }

  @Override
  public int getSize() {
    return mList.size();
  }

  @Override
  public Ticker getElementAt(int index) {
    return mList.get(index);
  }
}
