package cpfinderplug;

import java.io.File;

/**
 * Created by yfy on 4/19/17.
 * Compare change pairs
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
        String filename = oldJavaFile.getName();
        File newJavaFile = new File(newDir, filename);
        
      }
    }
  }
}
