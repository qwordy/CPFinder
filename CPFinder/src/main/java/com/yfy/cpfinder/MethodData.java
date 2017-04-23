package com.yfy.cpfinder;

/**
 * Created by yfy on 4/23/17.
 * Statistics of method
 */
public class MethodData {
  public int callNum, length;
  public String name, signature, code;

  @Override
  public String toString() {
    return name + " " + callNum + " " + length;
  }
}
