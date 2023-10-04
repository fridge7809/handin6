import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfanityFilter {
	private Scanner scanner;
	private final List<Character> replacementChars;
	private List<String> swearWords;
	private List<String> linesToFilter;

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
				List<String> tokens = new ArrayList<>();
				for (String token : getTokens(input, false)) {
					if (Pattern.matches("\\p{Punct}", token)) {
						continue;
					}
					tokens.add(token);
				}
				swearWords.addAll(tokens);
				firstLine = false;
				continue;
			}
			linesToFilter.add(input);
		} while (scanner.hasNextLine());
		scanner.close();
	}

	public void filterLines() {
		Iterator<String> iterator = linesToFilter.iterator();
		do {
			String currentLine = iterator.next();
			List<String> wordsInLine = getTokens(currentLine, true);
			int wordIndex = 0;
			for (String word : wordsInLine) {
				for (String swearWord : swearWords) {
					if (word.equalsIgnoreCase(swearWord)) {
						String filteredWord = replaceWord(word);
						wordsInLine.set(wordIndex, filteredWord);
					}
				}
				wordIndex++;
			}
			linesToFilter.set(linesToFilter.indexOf(currentLine), lineBuilder(wordsInLine));
		} while (iterator.hasNext());
	}

	private String lineBuilder(List<String> input) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String token : input) {
			stringBuilder.append(token);
		}
		return String.valueOf(stringBuilder);
	}

	public void printLines() {
		for (String line : linesToFilter) {
			System.out.println(line);
		}
	}

	private String replaceWord(String word) {
		StringBuilder builder = new StringBuilder(word);
		int charIndex = 0;
		for (int i = 0; i < builder.length(); i++) {
			if (i % 5 == 0) {
				charIndex = 0;
			}
			builder.replace(i, i + 1, String.valueOf(replacementChars.get(charIndex)));
			charIndex++;
		}
		return String.valueOf(builder);
	}

	public List<String> getTokens(String input, boolean includeWhitespace) {
		List<String> tokens = new ArrayList<>();
		Pattern pattern;
		if (!includeWhitespace) {
			pattern = Pattern.compile("\\b\\w+\\b|[,.!?'-]", Pattern.CASE_INSENSITIVE);
		} else {
			pattern = Pattern.compile("\\b\\w+\\b|[,.!?'-]|\\s", Pattern.CASE_INSENSITIVE);
		}
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			String token = matcher.group();
			tokens.add(token);
		}
		return tokens;
	}

	public static void main(String[] args) {
		ProfanityFilter filter = new ProfanityFilter(System.in);
		filter.readInput();
		filter.filterLines();
		filter.printLines();
	}
}