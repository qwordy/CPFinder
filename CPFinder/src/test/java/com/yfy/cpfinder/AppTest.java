package com.yfy.cpfinder;
/*
 * This Java source file was generated by the Gradle 'init' task.
 */

import org.junit.Test;

import static org.junit.Assert.*;

public class AppTest {
  @Test
  public void prepareCode() throws Exception {
    new PrepareCode().prepare();
  }

  @Test
  public void testAppHasAGreeting() {
    App classUnderTest = new App();
    assertNotNull("app should have a greeting", classUnderTest.getGreeting());
  }
}
