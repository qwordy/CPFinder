package com.yfy.cpfinder;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.gen.jdt.JdtTreeGenerator;
import com.github.gumtreediff.matchers.Mapping;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;

import java.io.File;
import java.util.List;

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
        compareFile(oldJavaFile, newJavaFile);
      }
    }
  }

  private void compareFile(File file1, File file2) throws Exception {
    ITree src = new JdtTreeGenerator().generateFromFile(file1).getRoot();
    ITree dst = new JdtTreeGenerator().generateFromFile(file2).getRoot();
    Matcher m = Matchers.getInstance().getMatcher(src, dst);
    m.match();
    MappingStore ms = m.getMappings();

    for (Mapping map : ms) {
      ITree t1 = map.getFirst();
      ITree t2 = map.getSecond();
      Util.log(t1.getLabel());
      Util.log(t2.getLabel());
//      Util.log(t1.toTreeString());
//      Util.log(t2.toTreeString());
    }

    ActionGenerator g = new ActionGenerator(src, dst, ms);
    g.generate();
    List<Action> actions = g.getActions();
    for (Action ac : actions) {
      //Util.log(ac.toString());
    }
  }
}
