package org.bitchouse.ideastock.provider;

import java.util.ArrayList;
import org.bitchouse.ideastock.Ticker;

/**
 * Created by bitchouse
 */
public interface StockProvider {

  ArrayList<Ticker> get(String[] codes);
}
