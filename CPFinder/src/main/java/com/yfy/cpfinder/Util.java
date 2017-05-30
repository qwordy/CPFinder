package com.yfy.cpfinder;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by yfy on 2017/4/18.
 */
public class Util {
  private static PrintWriter pw;

  static {
    try {
      pw = new PrintWriter("log");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void log(Object obj) {
    //System.out.println(obj);
    pw.println(obj);
  }

  public static String signature(MethodDeclaration md) {
    StringBuilder sb = new StringBuilder();
    sb.append(md.getName()).append(" ");
    for (Object o : md.parameters())
      sb.append(o).append(" ");
    return sb.toString();
  }
}
