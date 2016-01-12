package org.bitchouse.ideastock;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 * Created by bitchouse
 */
public class IndexTableModel extends DefaultTableModel {

  private ArrayList<Ticker> mList = new ArrayList<>(16);

  public IndexTableModel() {
  }

  public void updateData(ArrayList<Ticker> list) {
    mList.clear();
    mList.addAll(list);
    fireTableDataChanged();
  }

  String[] columnNames = {
      "Ticker",
      "Last",
      "Change%",
      "Change",
      "Low",
      "High",
      "Open",
      "Close",
      "Vol"};

  @Override
  public int getRowCount() {
    return mList.size();
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  @Override
  public String getColumnName(int column) {
    return columnNames[column];
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Ticker ticker = mList.get(rowIndex);
    switch (columnIndex) {
      case 0:
        return ticker.getName();
      case 1:
        return ticker.getLast();
      case 2:
        return ticker.getChangeP() + "%";
      case 3:
        return ticker.getChange();
      case 4:
        return ticker.getLow();
      case 5:
        return ticker.getHigh();
      case 6:
        return ticker.getOpen();
      case 7:
        return ticker.getClose();
      case 8:
        return ticker.getVol();
      default:
        return null;
    }
  }
}
