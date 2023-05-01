package ca.jrvs.apps.grep;

//import com.sun.org.slf4j.internal.Logger;
//import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep {

  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

  private String regex;
  private String rootPath;
  private String outFile;

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    BasicConfigurator.configure();

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      javaGrepImp.logger.error("Error: Unable to process", ex);
    }
  }

  @Override
  public void process() throws IOException {
    List<String> matchedLines = new ArrayList<>();

    for (File file : this.listFiles(getRootPath())) {
      for (String line : this.readLines(file)) {
        if (this.containsPattern(line)) {
          matchedLines.add(line);
        }
      }
    }
    this.writeToFile(matchedLines);
  }

  @Override
  public List<File> listFiles(String rootDir) {
    List<File> files = new ArrayList<>();
    File directory = new File(rootDir);

    for (File file : directory.listFiles()) {
      if (file.isFile()) {
        files.add(file);
      } else {
        files.addAll(this.listFiles(file.getPath()));
      }
    }
    return files;
  }

  @Override
  public List<String> readLines(File inputFile) {
    List<String> lines = new ArrayList<>();

    try {
      Scanner scanner = new Scanner(inputFile);
      while (scanner.hasNext()) {
        lines.add(scanner.next());
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      this.logger.error("Error: File not found", e);
    }
    return lines;
  }

  @Override
  public boolean containsPattern(String line) {
    return line.matches(getRegex());
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    FileWriter file = new FileWriter(getOutFile());
    BufferedWriter output = new BufferedWriter(file);

    for (String line : lines) {
      output.write(line);
      output.write('\n');
    }
    output.close();
  }

  @Override
  public String getRootPath() {
    return this.rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getRegex() {
    return this.regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getOutFile() {
    return this.outFile;
  }

  @Override
  public void setOutFile(String outfile) {
    this.outFile = outfile;
  }
}
