import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ProfanityFilterTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void regEx_splitsTokensCorrectly() {
		var swearWords = "hej, dette er en test! der ? tester? bandeord";
		var expectedResult = Arrays.asList("hej",
				",",
				"dette",
				"er",
				"en",
				"test",
				"!",
				"der",
				"?",
				"tester",
				"?",
				"bandeord");
		//ByteArrayInputStream mockInput = new ByteArrayInputStream(swearWords.getBytes());
		ProfanityFilter profanityFilter = new ProfanityFilter(System.in);
		assertEquals(expectedResult, profanityFilter.getTokens(swearWords));
	}

	@Test
	void test() {
		String input = "cudle\nhello i am very cudle hehe";
		String expected = "hello i am very *&#$% hehe";
		ByteArrayInputStream mockInput = new ByteArrayInputStream(input.getBytes());
		ProfanityFilter profanityFilter = new ProfanityFilter(mockInput);
		profanityFilter.readInput();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		System.setOut(printStream);
		profanityFilter.filterLines();
		profanityFilter.printLines();
		String actual = outputStream.toString();
		assertEquals(expected, actual);

	}

	@AfterEach
	void tearDown() {
	}
}