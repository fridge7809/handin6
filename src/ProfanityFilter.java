import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfanityFilter {
	private final InputStream inputStream;
	private final List<Character> replacementChars;
	private final List<String> swearWords;
	private final List<String> linesToFilter;
	private final Scanner scanner;

	public ProfanityFilter(InputStream inputStream) {
		this.inputStream = inputStream;
		this.replacementChars = Arrays.asList('*', '&', '#', '$', '%');
		this.swearWords = new ArrayList<>();
		this.linesToFilter = new ArrayList<>();
		this.scanner = new Scanner(this.inputStream);
	}

	public void run() {
		readSwearWords();
		readLinesToFilter();
		filterLines();
		printFilteredLines();
	}

	private void readSwearWords() {
		Pattern pattern = Pattern.compile("\\b\\w+\\b", Pattern.CASE_INSENSITIVE);
		swearWords.addAll(getTokens(scanner.nextLine(), pattern));
	}

	private void readLinesToFilter() {
		do {
			linesToFilter.add(scanner.nextLine());
		} while (scanner.hasNextLine());
		scanner.close();
	}

	private void filterLines() {
		Pattern pattern = Pattern.compile("\\b\\w+\\b|[,.!?'-]|\\s", Pattern.CASE_INSENSITIVE);
		for (int i = 0; i < linesToFilter.size(); i++) {
			String currentLine = linesToFilter.get(i);
			List<String> wordsInLine = getTokens(currentLine, pattern);

			for (int j = 0; j < wordsInLine.size(); j++) {
				String word = wordsInLine.get(j);
				for (String swearWord : swearWords) {
					if (word.equalsIgnoreCase(swearWord)) {
						wordsInLine.set(j, getFilteredWord(word));
					}
				}
			}

			linesToFilter.set(i, lineBuilder(wordsInLine));
		}
	}

	private String lineBuilder(List<String> input) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String token : input) {
			stringBuilder.append(token);
		}
		return String.valueOf(stringBuilder);
	}

	private void printFilteredLines() {
		for (String line : linesToFilter) {
			System.out.println(line);
		}
	}

	private String getFilteredWord(String word) {
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

	public List<String> getTokens(String input, Pattern pattern) {
		List<String> tokens = new ArrayList<>();
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			String token = matcher.group();
			tokens.add(token);
		}
		return tokens;
	}

	public static void main(String[] args) {
		ProfanityFilter filter = new ProfanityFilter(System.in);
		filter.run();
	}
}