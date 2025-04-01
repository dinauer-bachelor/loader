package loader;

import loader.persistance.Issue;
import org.apache.commons.text.similarity.CosineSimilarity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SimilarityCalculator
{
    private static final double SUMMARY_WEIGHT = 0.3;
    private static final double DESCRIPTION_WEIGHT = 0.7;
    private static final List<String> STOP_WORDS = List.of("the", "with", "has", "a", "or", "and", "to", "in", "on", "into", "onto", "am", "are", "is");

    public static Optional<Double> compare(Issue issue1, Issue issue2)
    {
        if(issue1 != null && issue1.getSummary() != null && issue1.getDescription() != null && issue2 != null && issue2.getSummary() != null && issue2.getDescription() != null)
        {
            double summarySimilarity = compareStrings(issue1.getSummary(), issue2.getSummary());
            double descriptionSimilarity = compareStrings(issue1.getDescription(), issue2.getDescription());
            return Optional.of ((SUMMARY_WEIGHT * summarySimilarity) + (DESCRIPTION_WEIGHT * descriptionSimilarity));
        }
        return Optional.empty();
    }

    protected static double compareStrings(String string1, String string2)
    {
        return new CosineSimilarity().cosineSimilarity(stringToVector(string1), stringToVector(string2));
    }

    protected static Map<CharSequence, Integer> stringToVector(String string)
    {
        Map<CharSequence, Integer> vector = new HashMap<>();
        String[] tokens = string.split("\\s+");
        for(String token : tokens)
        {
            String normalizedToken = normalizeToken(token);
            if(!STOP_WORDS.contains(normalizedToken))
            {
                int count = Optional.ofNullable(vector.get(normalizedToken)).orElse(0);
                count++;
                vector.put(normalizedToken, count);
            }

        }
        return vector;
    }

    protected static String normalizeToken(String token)
    {
        List<String> invalidChars = List.of(",", ".", "!", "?", "(", ")", "]", "[", "{", "}", "/");
        for(String character : invalidChars)
        {
            token = token.replace(character, "");
        }
        return token.toLowerCase();
    }
}
