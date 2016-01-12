/* 
 * Copyright (C) 2014 Peter Cai
 *
 * This file is part of BlackLight
 *
 * BlackLight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BlackLight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BlackLight.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.bitchouse.ideastock.misc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class HttpUtility {
  private static final boolean DEBUG = false;

  public enum Method {GET, POST}

  public static String doRequest(String url, HashMap<String, Object> params, Method method)
      throws IOException {
    boolean isGet = false;
    if (method == Method.GET) {
      isGet = true;
    }

    String myUrl = url;

    String send = encode(params);
    if (isGet && send != null) {
      myUrl += "?" + send;
    }

    if (DEBUG) {
      Log.d("send = " + send);
      Log.d("myUrl = " + myUrl);
    }

    URL u = new URL(myUrl);
    HttpURLConnection conn = (HttpURLConnection) u.openConnection();

    conn.setRequestMethod(method.name());
    conn.setDoOutput(!isGet);

    if (!isGet) {
      conn.setDoInput(true);
    }

    conn.setUseCaches(false);
    conn.setConnectTimeout(10000);
    conn.setReadTimeout(10000);

    conn.setRequestProperty("Connection", "Keep-Alive");
    conn.setRequestProperty("Charset", "UTF-8");
    conn.setRequestProperty("Accept-Encoding", "gzip, deflate");

    conn.connect();

    if (!isGet && send != null) {
      DataOutputStream o = new DataOutputStream(conn.getOutputStream());
      o.write(send.getBytes());
      o.flush();
      o.close();
    }

    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
      return null;
    } else {
      InputStream in = conn.getInputStream();

      String en = conn.getContentEncoding();

      if (en != null && en.equals("gzip")) {
        in = new GZIPInputStream(in);
      }

      BufferedReader buffer = new BufferedReader(new InputStreamReader(in));

      String s;
      StringBuilder str = new StringBuilder();

      while ((s = buffer.readLine()) != null) {
        str.append(s);
      }

      String ret = str.toString();

      if (DEBUG) {
        Log.d(ret);
      }

      return ret;
    }
  }

  // URL Encode
  public static String encode(HashMap<String, Object> map) {
    if (map == null) {
      return null;
    }
    StringBuilder str = new StringBuilder();
    Set<String> keys = map.keySet();
    boolean first = true;

    for (String key : keys) {
      Object value = map.get(key);

      if (first) {
        first = false;
      } else {
        str.append("&");
      }

      try {
        str.append(URLEncoder.encode(key, "UTF-8")).append("=")
            .append(URLEncoder.encode(value.toString(), "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    return str.toString();
  }
}
