package com.yfy.cpfinder;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.*;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yfy on 5/27/17.
 */
public class Matcher {


  public void match(File file1, File file2) throws Exception {
    Map<String, ASTNode> methods1 = parse(file1);
    Map<String, ASTNode> methods2 = parse(file2);
    for (String key : methods1.keySet()) {
      ASTNode node2 = methods2.get(key);
      if (node2 != null)
        compareMatchedMethods(methods1.get(key), node2);
    }
  }

  private Map<String, ASTNode> parse(File file) throws Exception {
    ASTParser parser = ASTParser.newParser(AST.JLS8);
    String content = FileUtils.readFileToString(file, Charset.defaultCharset());
    parser.setSource(content.toCharArray());
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    ASTNode ast = parser.createAST(null);
    Map<String, ASTNode> methods = new HashMap<>();
    ast.accept(new ASTVisitor() {
      @Override
      public boolean visit(MethodDeclaration node) {
        methods.put(Util.signature(node), node);
        return true;
      }
    });
    return methods;
  }

  private void compareMatchedMethods(ASTNode node1, ASTNode node2) {
    
  }
}
