package com.yfy.cpfinder;

import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * Created by yfy on 2017/4/18.
 */
public class Util {
  public static void log(Object obj) {
    System.out.println(obj);
  }

  public static String signature(MethodDeclaration md) {
    StringBuilder sb = new StringBuilder();
    sb.append(md.getName()).append(" ");
    for (Object o : md.parameters())
      sb.append(o).append(" ");
    return sb.toString();
  }
}
