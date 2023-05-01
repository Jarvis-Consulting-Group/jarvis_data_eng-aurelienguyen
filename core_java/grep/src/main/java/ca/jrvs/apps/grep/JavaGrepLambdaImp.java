package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp{
  public static void main(String[] args) {
    if (args.length != 3){
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }
    JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);
    try{
      javaGrepLambdaImp.process();
    } catch (Exception ex){
      ex.printStackTrace();
    }
  }
  @Override
  public List<File> listFiles(String rootDir) {
    File directory = new File(rootDir);
    return Arrays.stream(directory.listFiles()).flatMap(f -> !f.isFile() ?
        listFiles(f.getPath()).stream(): Stream.of(f)).collect(Collectors.toList());
  }

  @Override
  public List<String> readLines(File inputFile) {
    List<String> lines = new ArrayList<>();

    try {
      return Files.lines(inputFile.toPath()).collect(Collectors.toList());
    } catch (IOException e) {
      this.logger.error("Error: File not found", e);
    }
    return Collections.emptyList();
  }
}
