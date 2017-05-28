package com.yfy.cpfinder;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Created by yfy on 5/27/17.
 */
public class Matcher {
  public void match(File file1,File file2) throws Exception {

  }

  public ASTNode parse(File file) throws Exception {
    ASTParser parser = ASTParser.newParser(AST.JLS8);
    String content = FileUtils.readFileToString(file, Charset.defaultCharset());
    parser.setSource(content.toCharArray());
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    return parser.createAST(null);
  }
}
