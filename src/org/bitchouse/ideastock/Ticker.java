package org.bitchouse.ideastock;

/**
 * Created by bitchouse
 */
public class Ticker {

  public String code;
  public String name;
  public float last;
  public float change;
  public float changeP;
  public float low;
  public float high;
  public float open;
  public float close;
  public String vol;

  public Ticker() {
  }

  public Ticker(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public float getLast() {
    return last;
  }

  public void setLast(float last) {
    this.last = last;
  }

  public float getChange() {
    return change;
  }

  public void setChange(float change) {
    this.change = change;
  }

  public float getChangeP() {
    return changeP;
  }

  public void setChangeP(float changeP) {
    this.changeP = changeP;
  }

  public float getLow() {
    return low;
  }

  public void setLow(float low) {
    this.low = low;
  }

  public float getHigh() {
    return high;
  }

  public void setHigh(float high) {
    this.high = high;
  }

  public float getOpen() {
    return open;
  }

  public void setOpen(float open) {
    this.open = open;
  }

  public float getClose() {
    return close;
  }

  public void setClose(float close) {
    this.close = close;
  }

  public String getVol() {
    return vol;
  }

  public void setVol(String vol) {
    this.vol = vol;
  }
}
