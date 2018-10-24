package kata.timbahro.Anagram;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class App {
	public static void main(String[] args) throws IOException {
		String[] words = FileUtils.readFileToString(new File("wordlist.txt"), "cp1252").split("\n");

		Map<String, List<String>> anagrams = Arrays.stream(words)
				.collect(Collectors.groupingByConcurrent(s -> new String(s.chars().sorted().toArray(), 0, s.length())));

		anagrams.entrySet().stream().sorted((k, v) -> v.getValue().size() - k.getValue().size())
				.forEach((e) -> System.out.println(
						"[" + e.getValue().size() + "] " + e.getKey() + " --> " + StringUtils.join(e.getValue(), ",")));
	}
}
