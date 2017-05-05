package cpfinderplug;

import java.io.File;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;

import ca.mcgill.cs.swevo.ppa.PPAOptions;
import ca.mcgill.cs.swevo.ppa.ui.PPAUtil;

public class PPA {
  public static void analyse(File file) {
    CompilationUnit cu = PPAUtil.getCU(file, new PPAOptions());
    cu.accept(new ASTVisitor() {
      @Override
      public boolean visit(MethodInvocation node) {
        //Util.log("[invoke] " + node);
//        Expression exp = node.getExpression();
//        if (exp != null) {
//          ITypeBinding tb = exp.resolveTypeBinding();
//          if (tb != null) Util.log(tb);
//        }
        //Util.log(exp);
        //Util.log(node.getName());
        Name nameNode = node.getName();
        IBinding binding = nameNode.resolveBinding();
        ITypeBinding typeBinding = nameNode.resolveTypeBinding();
        return true;
      }
    });
  }
}
