package loader.similarity;

import loader.persistance.Issue;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SimilarityCalculatorTest
{
    private final static Logger LOG = LoggerFactory.getLogger(SimilarityCalculatorTest.class);

    @Test
    void testNormalizeToken()
    {
        // given
        String token1 = "machen!";
        String token2 = "Hauses.";
        String token3 = "Jira-Projekt";

        // when
        String normalizedToken1 = SimilarityCalculator.normalizeToken(token1);
        String normalizedToken2 = SimilarityCalculator.normalizeToken(token2);
        String normalizedToken3 = SimilarityCalculator.normalizeToken(token3);

        // then
        assertEquals("machen", normalizedToken1);
        assertEquals("hauses", normalizedToken2);
        assertEquals("jira-projekt", normalizedToken3);
    }

    @Test
    void testStringToVector()
    {
        // given
        String text1 = "Meine Katze ist flauschig. Flauschig ist meine Katze!";

        // when
        Map<CharSequence, Integer> vector1 = SimilarityCalculator.stringToVector(text1);

        // then
        // "meine" and "ist" are stopwords and get removed
        assertNotNull(vector1);
        assertEquals(2, vector1.keySet().size());
        assertEquals(2, vector1.get("katze"));
        assertEquals(2, vector1.get("flauschig"));
        assertNull(vector1.get("meine"));
        assertNull(vector1.get("ist"));
    }

    @Test
    void testCosineSimilarity()
    {
        // given
        String text1 = "Meine Katze ist flauschig.";
        String text2 = "Flauschig ist meine Katze!";

        // when
        double similarity = SimilarityCalculator.compareStrings(text1, text2);

        // then
        assertTrue(similarity > 0.99);
    }

    @Test
    void testComplexSimilarity()
    {
        // given
        String text11 = "The new software update significantly improved the performance of the application. Users reported faster load times and a more responsive interface, making daily tasks easier and more efficient. In addition, several bugs from the previous version were fixed.";
        String text12 = "The recent software update greatly enhanced the app’s performance. Users noticed quicker loading speeds and a smoother interface, which helped streamline their daily activities. Moreover, a number of issues present in the earlier version were resolved.";

        String text21 = "The new software update significantly improved the performance of the application. Users reported faster load times and a more responsive interface, which made daily tasks easier and more efficient. Additionally, several bugs from the previous version were fixed.";
        String text22 = "The new software update significantly improved the performance of the application. Users reported quicker load times and a more responsive interface, which made daily tasks easier and more efficient. Additionally, several bugs from the earlier version were fixed.";

        // when
        double similarity1 = SimilarityCalculator.compareStrings(text11, text12);
        double similarity2 = SimilarityCalculator.compareStrings(text21, text22);

        // then
        assertEquals(0.3, similarity1, 0.1);
        assertEquals(0.9, similarity2, 0.1);
    }

    @Test
    void compareIssues()
    {
        // given
        Issue issue1 = new Issue().setSummary("This is an example issue").setDescription("And it has this description. It's purpose is testing.");
        Issue issue2 = new Issue().setSummary("This issue is for testing.").setDescription("We use this default description. Let's see if this is equal to the other one.");

        Issue issue3 = new Issue().setDescription("And it has this description. It's purpose is testing.");
        Issue issue4 = new Issue().setSummary("This issue is for testing.").setDescription("We use this default description. Let's see if this is equal to the other one.");

        // when
        Optional<Double> similarity1 = SimilarityCalculator.compare(issue1, issue2);
        Optional<Double> similarity2 = SimilarityCalculator.compare(issue3, issue4);

        // then
        assertTrue(similarity1.isPresent());
        assertTrue(similarity2.isEmpty());
    }
}
