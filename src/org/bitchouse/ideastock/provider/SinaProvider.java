package org.bitchouse.ideastock.provider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bitchouse.ideastock.Ticker;
import org.bitchouse.ideastock.misc.HttpUtility;

/**
 * Created by bitchouse
 */
public class SinaProvider implements StockProvider {

  static final Pattern CODE_PATTERN =
      Pattern.compile("(?<=var\\sshq_str_sh)\\d+");
  static final Pattern INFO_PATTERN = Pattern.compile("(?<==\").+(?=\")");

  @Override
  public ArrayList<Ticker> get(String[] codes) {
    try {
      String url = "http://hq.sinajs.cn/list=" + buildCodeParam(codes);
      String s = HttpUtility.doRequest(url, null, HttpUtility.Method.GET);

      //            s="var hq_str_sh600031=\"三一重工,6.57,6.58,6.70,6.78,6.54,6.70,6
      // .71," +
      //                    "71575506,477359891,73400,6.70,548843,6.69,247500,6.68," +
      //                    "112800,6.67,175100,6.66,269355,6.71,286241,6.72,
      // 247059,6" +
      //                    ".73,337600,6.74,937694,6.75,2015-10-09,15:04:09,00\";
      // \n" +
      //                    "var hq_str_sh601001=\"大同煤业,5.59,5.58,5.77,5.80,5.55,5" +
      //                    ".76,5.77,22932068,130698570,178620,5.76,124399,5.75," +
      //                    "5000,5.74,41000,5.73,22600,5.72,115400,5.77,313200,5
      // .78," +
      //                    "193800,5.79,557000,5.80,238500,5.81,2015-10-09,
      // 15:04:09," +
      //                    "00\";\n" +
      //                    "var hq_str_sh601002=\"晋亿实业,11.21,11.41,11.56,11.70,11" +
      //                    ".01,11.55,11.56,52529058,594417232,6200,11.55,34300,11" +
      //                    ".54,28800,11.53,21100,11.52,163600,11.51,800,11.56," +
      //                    "133321,11.57,154700,11.58,101600,11.59,208000,11.60," +
      //                    "2015-10-09,15:04:09,00\";";
      return parse(s);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private String buildCodeParam(String[] codes) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < codes.length; i++) {
      String code = codes[i];
      builder.append(code);
      if (i < codes.length - 1) {
        builder.append(",");
      }
    }
    return builder.toString();
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
