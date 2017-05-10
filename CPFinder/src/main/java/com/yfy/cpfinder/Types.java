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
    String line1 = br.readLine();
    String line2 = br.readLine();
    String line3 = br.readLine();
    while (line1 != null && line2 != null && line3 != null) {
      if (line1.startsWith("Node:") && line2.startsWith("  Expression:") &&
          line3.startsWith("  Type")) {
        //Util.log("[ppa]"); Util.log(line1); Util.log(line2); Util.log(line3);
        String node = getValue(line1);
        String exp = getValue(line2);
        String type = getValue(line3);
        Entry entry = new Entry(node, exp, type);
        list.add(entry);
      } else if (line2.startsWith("Node:")) {
        line1 = line2;
        line2 = br.readLine();
        line3 = br.readLine();
      } else if (line3.startsWith("Node:")) {
        line1 = line3;
        line2 = br.readLine();
        line3 = br.readLine();
      }
    }
    br.close();
  }

  private String getValue(String line) {
    int pos = line.indexOf(':');
    if (pos + 2 < line.length())
      return line.substring(pos + 2);
    return "";
  }

  public String getType(String node, String exp) {
    for (Entry e : list) {
      if (e.node.equals(node) && e.exp.equals(exp))
        return e.type;
    }
    return "";
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
