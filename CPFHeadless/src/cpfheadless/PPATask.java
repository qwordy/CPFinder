package cpfheadless;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import ca.mcgill.cs.swevo.ppa.PPAOptions;
import ca.mcgill.cs.swevo.ppa.ui.PPAUtil;

public class PPATask implements Runnable {
  private File file;
  
  public PPATask(File file) {
    this.file = file;
  }
  
  @Override
  public void run() {
    try {
      String objFilename = file.getPath() + ".obj";
      File objFile = new File(objFilename);
      if (objFile.exists() && objFile.length() > 0) return;
      final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(objFile));
      
      CompilationUnit cu = PPAUtil.getCU(file, new PPAOptions());
      cu.accept(new ASTVisitor() {
        @Override
        public boolean visit(MethodInvocation node) {
          Expression exp = node.getExpression();
          if (exp != null) {
            ITypeBinding typeBinding = exp.resolveTypeBinding();
            Type type = new Type();
            type.node = node.toString();
            type.exp = exp.toString();
            type.type = typeBinding.getQualifiedName();
            try {
              oos.writeObject(type);
            } catch (Exception e) {
              System.out.println("Exc");
            }
          }
          return true;
        }
      });
      oos.close();
      System.out.println("Success");
    } catch (Exception e) {
      System.out.println("Exception");
    }
  }

}
