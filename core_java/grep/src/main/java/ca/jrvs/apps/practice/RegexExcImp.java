package ca.jrvs.apps.practice;

import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc{

  @Override
  public boolean matchJpeg(String filename) {
    Pattern pattern = Pattern.compile(".*\\.(jpg|jpeg)$", Pattern.CASE_INSENSITIVE);
    return pattern.matcher(filename).find();
  }

  @Override
  public boolean matchIp(String ip) {
    Pattern pattern = Pattern.compile("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
    return pattern.matcher(ip).find();
  }

  @Override
  public boolean isEmptyLine(String line) {
    Pattern pattern = Pattern.compile("^\\s*$");
    return pattern.matcher(line).find();
  }
}
