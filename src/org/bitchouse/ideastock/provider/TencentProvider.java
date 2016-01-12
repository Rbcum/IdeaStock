package org.bitchouse.ideastock.provider;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bitchouse.ideastock.Ticker;
import org.bitchouse.ideastock.misc.HttpUtility;

/**
 * Created by bitchouse
 */
public class TencentProvider implements StockProvider {
  static final Pattern CODE_PATTERN =
      Pattern.compile("(?<=var\\sshq_str_sh)\\d+");
  static final Pattern INFO_PATTERN = Pattern.compile("(?<==\").+(?=\")");

  @Override
  public ArrayList<Ticker> get(String[] codes) {
    try {
      String s = HttpUtility.doRequest("http://hq.sinajs" + "" +
          ".cn/list=sh600031,sh601001,sh601002", null, HttpUtility
          .Method.GET);
      return parse(s);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private ArrayList<Ticker> parse(String s) {
    String[] arr = s.split(";");
    ArrayList<Ticker> list = new ArrayList<>(arr.length);
    for (String seg : arr) {
      String code = regexFind(CODE_PATTERN, seg);
      String[] info = regexFind(INFO_PATTERN, seg).split(",");
      String name = info[0];

      float open = Float.parseFloat(info[1]);
      float close = Float.parseFloat(info[2]);
      float last = Float.parseFloat(info[3]);
      float high = Float.parseFloat(info[4]);
      float low = Float.parseFloat(info[5]);
      String vol = info[8];

      BigDecimal change = new BigDecimal(info[3]).subtract(
          new BigDecimal(info[2]));
      BigDecimal changeP = change.setScale(4, BigDecimal
          .ROUND_UNNECESSARY).divide(new BigDecimal(info[2]),
          BigDecimal.ROUND_HALF_UP);

      Ticker ticker = new Ticker(code, name);
      ticker.setOpen(open);
      ticker.setClose(close);
      ticker.setLast(last);
      ticker.setHigh(high);
      ticker.setLow(low);
      ticker.setChange(change.floatValue());
      ticker.setChangeP(changeP.floatValue());
      ticker.setVol(vol);
      list.add(ticker);
    }
    return list;
  }

  private static String regexFind(Pattern pattern, String sample) {
    Matcher matcher = pattern.matcher(sample);
    return matcher.find() ? matcher.group() : null;
  }
}
