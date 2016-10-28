import java.util.*;
import java.util.stream.Collectors;

public class Test {

	public static void main(String[] args) {

		testMapReduceInJava8();
	}


	private static void testMapReduceInJava8() {
		List<Integer> list = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			list.add(i);
		}

		Optional maxOdd = list.stream()
				.filter((num) -> num % 2 == 0)
				.reduce(Integer::max);

		if (maxOdd.isPresent()) {
			System.out.println(maxOdd.get());
		}

		// reduction example
		System.out.println("reduction example...");
		List<String> strings = new ArrayList<>();
		strings.add("Asdsd");
		strings.add("sdsd");
		strings.add("Asdsdsdsd");

		strings.stream()
				.filter(s -> s.startsWith("A"))
				.mapToInt(String::length)
				.max()
				.ifPresent(maxSize -> System.out.println(maxSize));

		System.out.println("printing the max length string, starting with A.");
		strings.stream()
				.filter(s -> s.startsWith("A"))
				.max((x,y) -> x.length() - y.length())
				.ifPresent(max -> System.out.println(max));


		// filter stream example
		List<Integer> oddNos = list.stream()
				.filter(num -> num % 2 == 1)
				.collect(Collectors.toList());
		oddNos.size();

		// limit example.
		System.out.println("printing limit...");
		list.stream()
				.limit(3)
				.forEach(x -> System.out.println(x));
	}
}
