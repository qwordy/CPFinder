package cpf;

import java.io.PrintStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PPABindingsUtil;

public class PPATypeVisitor extends ASTVisitor {

  private PrintStream printer;
  
  public PPATypeVisitor(PrintStream printer) {
    super();
    this.printer = printer;
  }

  @Override
  public void postVisit(ASTNode node) {
    super.postVisit(node);

    if (node instanceof Expression) {
      Expression exp = (Expression) node;

      IBinding binding = null;
      if (exp instanceof Name) {
        Name name = (Name) exp;
        binding = name.resolveBinding();
      } else if (exp instanceof MethodInvocation) {
        MethodInvocation mi = (MethodInvocation) exp;
        binding = mi.resolveMethodBinding();
      } else if (exp instanceof ClassInstanceCreation) {
        ClassInstanceCreation cic = (ClassInstanceCreation) exp;
        binding = cic.resolveConstructorBinding();
      } else {
        return;
      }
      printer.println("Node: " + node.toString());
      ITypeBinding tBinding = exp.resolveTypeBinding();
      if (tBinding != null) {
        printer.println("  Type Binding: " + tBinding.getQualifiedName());
      }

      if (binding != null) {
        printer.println("  " + PPABindingsUtil.getBindingText(binding));
      }
      printer.flush();
    }
  }

}