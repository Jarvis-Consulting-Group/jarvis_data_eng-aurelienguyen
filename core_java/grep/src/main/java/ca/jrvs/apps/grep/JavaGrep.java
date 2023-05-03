package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JavaGrep {

  /**
   * Top level search workflow
   * @throws IOException
   */
  void process() throws IOException;

  /**
   * Traverse a given directory and return all files
   * @param rootDir in put directory
   * @return files under the rootDir
   */
  List<File> listFiles(String rootDir);

  /**
   * Read a file and return all the lines
   * FileReader is a class that provides a way to read a single character data from a file at a time,
   * whereas BufferedReader reads a block of characters at a time, making it more efficient.
   * Character encoding is the mapping between characters and bytes.
   * @param inputFile file to be read
   * @return lines
   * @throws IllegalAccessException if a given inputFile is not a file
   */
  List<String> readLines(File inputFile);

  /**
   * check if a line contains the regex pattern (passed by user)
   * @param line input string
   * @return true if there is a match
   */
  boolean containsPattern(String line);

  /**
   * Write lines to a file
   *
   * FileOutputStream is used to write binary data to a file
   * OutputStreamWriter is used to write character data to a file
   * BufferedWriter is used to write character data to a file with buffering
   * @param lines matched line
   * @throws IOException if write failed
   */
  void writeToFile(List<String> lines) throws IOException;

  String getRootPath();

  void setRootPath(String rootPath);

  String getRegex();

  void setRegex(String regex);

  String getOutFile();

  void setOutFile(String outfile);

}
