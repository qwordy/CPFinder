package com.yfy.cpfinder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy on 5/9/17.
 */
public class Types {
  private List<Entry> list = new ArrayList<>();

  public Types(String filename) throws Exception {
    BufferedReader br = new BufferedReader(new FileReader(filename));
    String line1;
    while ((line1 = br.readLine()) != null) {
      String line2 = br.readLine();
      String line3 = br.readLine();
      String node = line1.substring(line1.charAt(':') + 2);
      String exp = line2.substring(line1.charAt(':') + 2);
      String type = line3.substring(line1.charAt(':') + 2);
      Entry entry = new Entry(node, exp, type);
      list.add(entry);
    }
  }

  private static class Entry {
    public String node, exp, type;
    public Entry(String node, String exp, String type) {
      this.node = node;
      this.exp = exp;
      this.type = type;
    }
  }
}
