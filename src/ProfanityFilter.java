import java.util.*;

public class ProfanityFilter {
	private Scanner scanner;
	private final List<Character> replacementChars;
	private List<String> swearWords;
	private List<String> linesToFilter;
	private Random random;

	public ProfanityFilter() {
		scanner = new Scanner(System.in);
		replacementChars = Arrays.asList('*', '&', '#', '$', '%', '%');
		swearWords = new ArrayList<>();
		linesToFilter = new ArrayList<>();
		random = new Random();
	}

	public void readInput() {
		boolean firstLine = true;
		do {
			String input = scanner.nextLine();
			if (firstLine){
				swearWords.addAll(splitString(input));
				firstLine = false;
				continue;
			}
			if (input.isEmpty()) {
				break;
			}
			linesToFilter.add(input);
		} while (scanner.hasNextLine());
	}

	public void filterLines() {
		Iterator<String> iterator = linesToFilter.iterator();
		while (iterator.hasNext()) {
			String currentLine = iterator.next();
			List<String> wordsInLine = splitString(currentLine);

			// save indices of swearwords
			List<Integer> swearWordIndices = new ArrayList<>();
			for (int i = 0; i < wordsInLine.size(); i++) {
				if (swearWords.contains(wordsInLine.get(i))) {
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
			stringBuilder.append(" ");
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
		for (int i = 0; i < builder.length(); i++) {
			int randomInt = random.nextInt(replacementChars.size());
			builder.replace(i, i + 1, String.valueOf(replacementChars.get(randomInt)));
		}
		return String.valueOf(builder);
	}

	private List<String> splitString(String input) {
		return Arrays.asList(input.split(" "));
	}

	public static void main(String[] args) {
		System.out.println("Hello world!");
		ProfanityFilter filter = new ProfanityFilter();
		filter.readInput();
		filter.filterLines();
		filter.printLines();
	}
}