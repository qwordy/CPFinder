package com.yfy.cpfinder;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yfy on 10/20/16.
 */
public class Execute {

  public static BufferedReader execWithOutput(String cmd, String dir) {
    try {
      Process process;
      if (dir == null)
        process = Runtime.getRuntime().exec(cmd);
      else
        process = Runtime.getRuntime().exec(cmd, null, new File(dir));
      return new BufferedReader(
          new InputStreamReader(process.getInputStream()));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void execIgnoreOutput(String cmd, String dir) {
    try {
      InputStream is;
      if (dir == null)
        is = Runtime.getRuntime().exec(cmd).getInputStream();
      else
        is = Runtime.getRuntime().exec(cmd, null, new File(dir))
            .getInputStream();
      byte[] buf = new byte[4096];
      while (is.read(buf) != -1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
