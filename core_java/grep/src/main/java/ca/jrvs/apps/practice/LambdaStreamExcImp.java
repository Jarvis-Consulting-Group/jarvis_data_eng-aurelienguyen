package ca.jrvs.apps.practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExcImp implements LambdaStreamExc{

  @Override
  public Stream<String> createStrStream(String... strings) {
    return Stream.of(strings);
  }

  @Override
  public Stream<String> toUpperCase(String... strings) {
    return Stream.of(strings).map(String::toUpperCase);
  }

  @Override
  public Stream<String> filter(Stream<String> stringStream, String pattern) {
    return stringStream.filter(s -> !s.contains(pattern));
  }

  @Override
  public IntStream createIntStream(int[] arr) {
    return Arrays.stream(arr);
  }

  @Override
  public <E> List<E> toList(Stream<E> stream) {
    return stream.collect(Collectors.toList());
  }

  @Override
  public List<Integer> toList(IntStream intStream) {
    return intStream.boxed().collect(Collectors.toList());
  }

  @Override
  public IntStream createIntStream(int start, int end) {
    return IntStream.rangeClosed(start, end);
  }

  @Override
  public DoubleStream squareRootIntStream(IntStream intStream) {
    return intStream.mapToDouble(x -> x*x);
  }

  @Override
  public IntStream getOdd(IntStream intStream) {
    return intStream.filter(x -> x % 2 != 0);
  }

  @Override
  public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
    return str -> System.out.println(prefix + str + suffix);
  }

  @Override
  public void printMessages(String[] messages, Consumer<String> printer) {
    for (String message : messages){
      printer.accept(message);
    }
  }

  @Override
  public void printOdd(IntStream intStream, Consumer<String> printer) {
    intStream.filter(num -> num % 2 != 0).mapToObj(String::valueOf).forEach(printer::accept);
  }

  @Override
  public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
//    return ints.flatMap(List::stream).map(x -> x * x);
    List<Integer> result = new ArrayList<>();
    ints.forEach(list -> list.forEach(i -> result.add(i*i)));
    return result.stream();
  }
}
