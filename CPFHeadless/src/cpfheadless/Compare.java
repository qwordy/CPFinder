package cpfheadless;

import java.io.File;
import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import ca.mcgill.cs.swevo.ppa.PPAOptions;
import ca.mcgill.cs.swevo.ppa.ui.PPAUtil;

/**
 * Compare change pairs
 * @author yfy
 *
 */
public class Compare {
  public void compare() throws Exception {
    compare("hadoop");
  }

  private void compare(String project) throws Exception {
    File pdir = new File(Config.pairDir + project);
    for (File commit : pdir.listFiles()) {
      File oldDir = new File(commit, "old");
      File newDir = new File(commit, "new");
      for (File oldJavaFile : oldDir.listFiles()) {
        if (!oldJavaFile.getName().endsWith(".java")) continue;
        String filename = oldJavaFile.getName();
        File newJavaFile = new File(newDir, filename);
        
        ppaAnalyse(oldJavaFile);
        ppaAnalyse(newJavaFile);
        //spoonDiff(oldJavaFile, newJavaFile);
      }
    }
  }
  
  private void ppaAnalyse(File file) throws Exception {
    String ppaFilename = file.getPath() + ".ppa";
    final PrintWriter pw = new PrintWriter(ppaFilename);
    CompilationUnit cu = PPAUtil.getCU(file, new PPAOptions());
    cu.accept(new ASTVisitor() {
      @Override
      public boolean visit(MethodInvocation node) {
        Expression exp = node.getExpression();
        if (exp != null) {
          ITypeBinding typeBinding = exp.resolveTypeBinding();
          pw.println("Node: " + node.toString());
          if (typeBinding != null) {
            pw.println("  Expression: " + exp);
            pw.println("  Type Binding: " + typeBinding.getQualifiedName());
            //pw.println("  " + exp + " " + typeBinding);
          }
        }
        return true;
      }
    });
    pw.close();
  }
  
  private void spoonDiff(File file1, File file2) throws Exception {
//    AstComparator comp = new AstComparator();
//    Diff diff = comp.compare(file1, file2);
//    for (Operation op : diff.getAllOperations())
//      Util.log(op);
  }
}
