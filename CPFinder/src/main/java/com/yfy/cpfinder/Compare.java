package com.yfy.cpfinder;

import com.github.gumtreediff.gen.jdt.JdtTreeGenerator;
import com.github.gumtreediff.matchers.Mapping;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import gumtree.spoon.diff.operations.*;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.*;
import spoon.reflect.declaration.CtElement;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by yfy on 4/19/17.
 * Compare change pairs
 */
public class Compare {
  Matcher matcher = new Matcher();

  public void compare() throws Exception {
    compare("hadoop");
  }

  private void compare(String project) throws Exception {
    File pdir = new File(Config.pairDir + project);
    for (File commit : pdir.listFiles()) {
      File oldDir = new File(commit, "old");
      File newDir = new File(commit, "new");
      for (File oldFile : oldDir.listFiles()) {
        if (!oldFile.getName().endsWith(".java")) continue;
        String filename = oldFile.getName();
        File newFile = new File(newDir, filename);
        //compareFileJdt(oldFile, newFile);
        //spoonDiff(oldFile, newFile);
        //matcher.match(oldFile, newFile);
        compareFileGumtree(oldFile, newFile);
      }
    }
  }

  private void spoonDiff(File file1, File file2) throws Exception {
    Types types1 = new Types(file1.getPath() + ".ppa");
    Types types2 = new Types(file2.getPath() + ".ppa");
    AstComparator diff = new AstComparator();
    try {
      Diff result = diff.compare(file1, file2);
      //Util.log("[spoonDiff]");
      for (Operation op : result.getRootOperations()) {
        if (op instanceof UpdateOperation) {
          UpdateOperation uop = (UpdateOperation) op;
          CtElement se = uop.getNode();
          CtElement de = uop.getDestElement();
          InvokeData d1 = dealNode(se, types1);
          InvokeData d2 = dealNode(de, types2);
          if (d2.callNum > 0 || d1.callNum > 0)
            Util.log("Call num: " + d1.callNum + " " + d2.callNum);
//          Util.log("[Update]");
//          Util.log("  src: " + se);
//          Util.log("  pos: " + (se == null ? "" : se.getPosition()));
//          Util.log("  dst: " + de);
//          Util.log("  pos: " + (de == null ? "" : de.getPosition()));
        } else if (op instanceof DeleteOperation) {
          DeleteOperation dop = (DeleteOperation) op;
//          dealNode(dop.getNode());
//          Util.log("[Delete]");
//          Util.log("  node: " + dop.getNode());
//          Util.log("  pos: " + (dop.getNode() == null ? "" : dop.getNode().getPosition()));
        } else if (op instanceof InsertOperation) {
          InsertOperation iop = (InsertOperation) op;
          //dealNode(iop.getNode(), types2);
//          Util.log("[Insert]");
//          Util.log("  node: " + iop.getNode());
//          Util.log("  pos: " + (iop.getNode() == null ? "" : iop.getNode().getPosition()));
          //Util.log("  pos: " + iop.getPosition());
        } else if (op instanceof MoveOperation) {
          MoveOperation mop = (MoveOperation) op;
//          Util.log("[Move]");
//          Util.log("  node: " + mop.getNode());
//          Util.log("  pos: " + (mop.getNode() == null ? "" : mop.getNode().getPosition()));
          //Util.log("  pos: " + mop.getPosition());
        }
        //Util.log("[spoon] " + op.getNode());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private InvokeData dealNode(CtElement node, Types types) {
    if (node == null) return new InvokeData();
    ASTParser parser = ASTParser.newParser(AST.JLS8);
    parser.setSource(node.toString().toCharArray());
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    ASTNode ast = parser.createAST(null);
    InvokeData data = new InvokeData();
    ast.accept(new ASTVisitor() {
      @Override
      public boolean visit(MethodInvocation node) {
        //Util.log("[invoke] " + node);
        String type = "";//types.getType(node.toString(), node.getExpression().toString());
        if (!type.startsWith("org.apache.hadoop"))
          data.callNum++;
        return true;
      }
    });
    return data;
  }

  private void compareFileJdt(File file1, File file2) throws Exception {
    Map<String, MethodData> map1 = parseJdt(file1);
    Map<String, MethodData> map2 = parseJdt(file2);
    for (String key : map1.keySet()) {
      MethodData md2 = map2.get(key);
      if (md2 != null) {
        MethodData md1 = map1.get(key);
        if (md1.length > md2.length && md1.callNum < md2.callNum) {
//          Util.log("Compare");
//          Util.log(md1);
//          Util.log(md2);
        }
      }
    }
  }

  private int callNum;

  // import classes
  private HashSet<String> classSet = new HashSet<>();

  private HashMap<String, String> typeMap = new HashMap<>();

  private Map<String, MethodData> parseJdt(File file) throws Exception {
    ASTParser parser = ASTParser.newParser(AST.JLS8);
    String content = FileUtils.readFileToString(file, Charset.defaultCharset());
    parser.setSource(content.toCharArray());
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    parser.setResolveBindings(true);
//    parser.setBindingsRecovery(true);
    ASTNode ast = parser.createAST(null);
    Map<String, MethodData> map = new HashMap<>();
    classSet.clear();
    typeMap.clear();
    ast.accept(new ASTVisitor() {
      @Override
      public boolean visit(ImportDeclaration node) {
        //Util.log(node);
        String name = node.getName().toString();
        if (!name.startsWith("org.apache.hadoop") && !name.startsWith("java"))
          classSet.add(name.substring(name.lastIndexOf('.')));
        return true;
      }

      @Override
      public boolean visit(VariableDeclarationExpression node) {
//        Util.log(node);
        String type = node.getType().toString();
        if (type.indexOf('<') != -1)
          type = type.substring(0, type.indexOf('<'));
        for (Object o : node.fragments()) {
          VariableDeclarationFragment f = (VariableDeclarationFragment) o;
          typeMap.put(f.getName().toString(), type);
          Util.log(f.getName() + " " + type);
        }
        return true;
      }

      @Override
      public boolean visit(MethodDeclaration node) {
        if (node.getBody() == null) return true;
        MethodData methodData = new MethodData();
        callNum = 0;
        node.accept(new ASTVisitor() {
          @Override
          public boolean visit(MethodInvocation node) {
            Util.log("[invoke] " + node);
            Util.log(node.getName());
            Util.log(node.getExpression());
            Expression exp = node.getExpression();
            if (exp != null) {
              ITypeBinding tb = exp.resolveTypeBinding();
              if (tb != null) Util.log(tb);
            }
            //Util.log(exp);
            //Util.log(node.getName());
            callNum++;
            return true;
          }
        });
        String signature = Util.signature(node);
        //Util.log("[method] " + signature);
        methodData.callNum = callNum;
        methodData.length = node.getBody().getLength();
        methodData.name = node.getName().toString();
        methodData.signature = signature;
        methodData.code = node.toString();
        //Util.log(methodData);
        map.put(signature, methodData);
        return true;
      }
    });
    return map;
  }

  private void compareFileGumtree(File file1, File file2) throws Exception {
    Types types1 = new Types(file1.getPath() + ".obj");
    Types types2 = new Types(file2.getPath() + ".obj");
    RandomAccessFile raf1 = new RandomAccessFile(file1, "r");
    RandomAccessFile raf2 = new RandomAccessFile(file2, "r");

    ITree src = new JdtTreeGenerator().generateFromFile(file1).getRoot();
    ITree dst = new JdtTreeGenerator().generateFromFile(file2).getRoot();
    com.github.gumtreediff.matchers.Matcher m = Matchers.getInstance().getMatcher(src, dst);
    m.match();
    MappingStore ms = m.getMappings();

    for (Mapping map : ms) {
      ITree t1 = map.getFirst();
      ITree t2 = map.getSecond();
      if (!t1.toTreeString().equals(t2.toTreeString())) {
        Util.log("[Mapping]");
        InvokeData d1 = new InvokeData();
        InvokeData d2 = new InvokeData();
        dfs(t1, raf1, types1, d1);
        dfs(t2, raf2, types2, d2);
        Util.log("[CallNum] " + d1.callNum + " " + d2.callNum);
//        Util.log(t1.toTreeString());
//        Util.log(t2.toTreeString());
//        Util.log(t1.getPos() + " " + t1.getLength());
//        Util.log(t2.getPos() + " " + t2.getLength());
      }
      //Util.log(t1.getType() + " " + t2.getType());
      //Util.log(t1.getLabel().equals(t2.getLabel()));
      //Util.log(t2.getLabel());
      //Util.log(t1.toTreeString().equals(t2.toTreeString()));
      //Util.log(t2.toTreeString());
    }
    raf1.close();
    raf2.close();

//    ActionGenerator g = new ActionGenerator(src, dst, ms);
//    g.generate();
//    List<Action> actions = g.getActions();
//    for (Action ac : actions) Util.log(ac.toString());
  }

  private byte[] buf = new byte[1 << 22];

  private void dfs(ITree node, RandomAccessFile raf, Types types, InvokeData id) throws Exception {
    if (node.getType() == ASTNode.METHOD_INVOCATION) {
      Util.log("[Call]");

      raf.seek(node.getPos());
      raf.read(buf, 0, node.getLength());
      String str = new String(buf, 0, node.getLength());
      Util.log("Code: " + str);

      ASTNode ast = getJdtAst(str);
      ast.accept(new ASTVisitor() {
        @Override
        public boolean visit(MethodInvocation node) {
          Util.log("Exp: " + node.getExpression());
          Util.log("Name: " + node.getName());
          String type = types.getType(node.toString());
          if (!type.startsWith("org.apache.hadoop"))
            id.callNum++;
          Util.log("Type: " + type);
          return false;
        }
      });
    }
    for (ITree t : node.getChildren())
      dfs(t, raf, types, id);
  }

  private ASTNode getJdtAst(String str) {
    ASTParser parser = ASTParser.newParser(AST.JLS8);
    parser.setSource(str.toCharArray());
    parser.setKind(ASTParser.K_EXPRESSION);
    return parser.createAST(null);
  }

  public static void main(String[] args) throws Exception {
    new Compare().compare();
  }
}
