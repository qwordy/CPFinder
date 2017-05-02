package cpfinderplug;

import java.io.File;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;

import ca.mcgill.cs.swevo.ppa.PPAOptions;
import ca.mcgill.cs.swevo.ppa.ui.PPAUtil;

public class PPA {
  public void analyse(File file) {
    CompilationUnit cu = PPAUtil.getCU(file, new PPAOptions());
    cu.accept(new ASTVisitor() {
      
    });
    Name nameNode = ;
    IBinding binding = nameNode.resolveBinding();
    ITypeBinding typeBinding = nameNode.resolveTypeBinding();
  }
}
