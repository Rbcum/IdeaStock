package org.bitchouse.ideastock;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import org.bitchouse.ideastock.misc.Log;

import static org.bitchouse.ideastock.misc.FormatUtil.formatFloat;

/**
 * Created by bitchouse
 */
public class TickerTableModel extends AbstractTableModel {

  private ArrayList<Ticker> mList = new ArrayList<>(16);

  public TickerTableModel() {
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
    //        return 5;
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
    //        return "dumb"+Math.random();
    Ticker ticker = mList.get(rowIndex);
    switch (columnIndex) {
      case 0:
        return ticker.getName();
      case 1:
        return formatFloat("%.2f", ticker.getLast());
      case 2:
        return formatFloat("%+.2f%%", ticker.getChangeP() * 100);
      case 3:
        return formatFloat("%+.2f", ticker.getChange());
      case 4:
        return formatFloat("%.2f", ticker.getLow());
      case 5:
        return formatFloat("%.2f", ticker.getHigh());
      case 6:
        return formatFloat("%.2f", ticker.getOpen());
      case 7:
        return formatFloat("%.2f", ticker.getClose());
      case 8:
        return ticker.getVol();
      default:
        return null;
    }
  }
}
