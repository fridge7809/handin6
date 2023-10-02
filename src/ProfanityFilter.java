import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfanityFilter {
	private Scanner scanner;
	private final List<Character> replacementChars;
	private List<String> swearWords;
	private List<String> linesToFilter;
	private String pattern = "\\s+|\\p{Punct}";
	private List<Character> punctuation = Arrays.asList(',', '.', '!', '?');


	public ProfanityFilter(InputStream inputStream) {
		scanner = new Scanner(inputStream);
		replacementChars = Arrays.asList('*', '&', '#', '$', '%');
		swearWords = new ArrayList<>();
		linesToFilter = new ArrayList<>();
	}

	public void readInput() {
		boolean firstLine = true;
		do {
			String input = scanner.nextLine();
			if (firstLine){
				swearWords.addAll(getTokens(input));
				firstLine = false;
				continue;
			}
			if (input.isEmpty()) {
				break;
			}
			linesToFilter.add(input);
		} while (scanner.hasNextLine());
		scanner.close();
	}

	public void filterLines() {
		// todo simplify, extract logic
		Iterator<String> iterator = linesToFilter.iterator();
		while (iterator.hasNext()) {
			String currentLine = iterator.next();
			List<String> wordsInLine = getTokens(currentLine);
			List<Integer> swearWordIndices = new ArrayList<>();
			for (int i = 0; i < wordsInLine.size(); i++) {
				if (swearWords.contains(wordsInLine.get(i).toLowerCase())) {
					swearWordIndices.add(i);
				}
			}
			for (Integer index : swearWordIndices) {
				wordsInLine.set(index, replaceWord(wordsInLine.get(index)));
			}
			linesToFilter.set(linesToFilter.indexOf(currentLine), lineBuilder(wordsInLine));
		}
	}

	private String lineBuilder(List<String> input) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String word : input) {
			stringBuilder.append(word);
			// fixme if next word isnt a punctuation char then add whitespace
			//if (!(punctuation.contains(input.get(input.indexOf(word) + 1)))) {
				stringBuilder.append(" ");
		//	}
		}
		return String.valueOf(stringBuilder);
	}

	public void printLines() {
		for (String line : linesToFilter) {
			System.out.print(line);
		}
	}

	private String replaceWord(String word) {
		StringBuilder builder = new StringBuilder(word);
		int charIndex = 0;
		for (int i = 0; i < builder.length(); i++) {
			if (i == 6) {
				charIndex = 0;
			}
			builder.replace(i, i + 1, String.valueOf(replacementChars.get(charIndex)));
			charIndex++;
		}
		return String.valueOf(builder);
	}

	// todo String replacedText = matcher.replaceAll("***");
	public List<String> getTokens(String input) {
		List<String> tokens = new ArrayList<>();
		Pattern pattern = Pattern.compile("\\b\\w+\\b|[,.!?]", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			String token = matcher.group().toLowerCase();
			tokens.add(token);
		}
		return tokens;
	}

//	public static void main(String[] args) {
//		System.out.println("Hello world!");
//		ProfanityFilter filter = new ProfanityFilter(System.in);
//		filter.readInput();
//		filter.filterLines();
//		filter.printLines();
//	}
}