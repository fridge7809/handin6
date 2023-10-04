import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfanityFilter {
	private final InputStream inputStream;
	private final List<Character> replacementChars;
	private final List<String> swearWords;
	private final List<String> linesToFilter;

	public ProfanityFilter(InputStream inputStream) {
		this.inputStream = inputStream;
		this.replacementChars = Arrays.asList('*', '&', '#', '$', '%');
		this.swearWords = new ArrayList<>();
		this.linesToFilter = new ArrayList<>();
	}

	public void run() {
		readInput();
		filterLines();
		printFilteredLines();
	}

	// todo refac
	private void readInput() {
		boolean firstLine = true;
		Scanner scanner = new Scanner(inputStream);
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

	private void filterLines() {
		for (int i = 0; i < linesToFilter.size(); i++) {
			String currentLine = linesToFilter.get(i);
			List<String> wordsInLine = getTokens(currentLine, true);

			for (int j = 0; j < wordsInLine.size(); j++) {
				String word = wordsInLine.get(j);
				for (String swearWord : swearWords) {
					if (word.equalsIgnoreCase(swearWord)) {
						wordsInLine.set(j, filterWord(word));
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

	private String filterWord(String word) {
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
		filter.run();
	}
}