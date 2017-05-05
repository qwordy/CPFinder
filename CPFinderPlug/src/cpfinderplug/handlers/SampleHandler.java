package cpfinderplug.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import cpfinderplug.Compare;

import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
//    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
//    MessageDialog.openInformation(window.getShell(), "CPFinderPlug", "Hello, Eclipse world");
    try {
      new Compare().compare();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
