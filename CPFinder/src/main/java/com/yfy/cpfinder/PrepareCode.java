package com.yfy.cpfinder;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yfy on 2017/4/18.
 * Prepare change code pairs for analysis
 */
public class PrepareCode {
  private String dir = "../projects/";

  private String pairDir = "../pairs/";

  public void prepare() throws Exception {
    prepare("hadoop");
  }

  public void prepare(String project) throws Exception {

    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    String projectDir = dir + project + '/';
    Repository repo = builder.setGitDir(new File(projectDir + ".git"))
        .setMustExist(true).build();
    Git git = new Git(repo);
    Iterable<RevCommit> log = git.log().call();
    for (RevCommit commit : log)
      getModifiedFiles(commit.getName(), projectDir, project);
  }

  private void getModifiedFiles(String commit, String projectDir, String project)
      throws Exception {
    String cmd = "git diff-tree --no-commit-id --name-status -r " + commit;
    BufferedReader br = Execute.execWithOutput(cmd, projectDir);
    if (br == null) {
      Util.log("[Error] get modified files");
      return;
    }
    String line;
    while ((line = br.readLine()) != null) {
      if (line.length() > 5 && line.charAt(0) == 'M' &&
          line.substring(line.length() - 5).equals(".java")) {
        String filename = line.substring(2);
        //Util.log(filename);
        checkoutCommit(commit, filename, projectDir, project);
      }
    }
  }

  // Check out filename before and at some commit. Write change pairs to fs.
  private void checkoutCommit(String commit, String filename, String projectDir, String project)
      throws Exception {
    checkoutCommit0(commit, "^ -- ", filename, projectDir, project, "old");
    checkoutCommit0(commit, " -- ", filename, projectDir, project, "new");
  }

  private void checkoutCommit0(String commit, String cmdpart, String filename,
      String projectDir, String project, String path)
      throws Exception {
    String cmd = "git checkout " + commit + cmdpart + filename;
    Execute.execIgnoreOutput(cmd, projectDir);
    String fullFilename = projectDir + filename;
    String content = new String(Files.readAllBytes(Paths.get(fullFilename)));

  }
}
