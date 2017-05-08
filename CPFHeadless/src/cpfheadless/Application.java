package cpfheadless;

import java.io.File;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jdt.core.dom.CompilationUnit;

import ca.mcgill.cs.swevo.ppa.PPAOptions;
import ca.mcgill.cs.swevo.ppa.ui.PPAUtil;
import cpf.PPATypeVisitor;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
	  try {
	    new Compare().compare();
//	    test();
	  } catch (Exception e) {
	    e.printStackTrace();
	  }
		return IApplication.EXIT_OK;
	}
	
	private void test() {
	  File javaFile = new File("/home/yfy/workspaceHelios/PPATest/A.java");
    CompilationUnit cu = PPAUtil.getCU(javaFile, new PPAOptions());
    PPATypeVisitor visitor = new PPATypeVisitor(System.out);
    cu.accept(visitor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		// nothing to do
	}
}
