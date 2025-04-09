package dev.dinauer.similarity;

import dev.dinauer.persistence.entity.Issue;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;

import java.util.*;

public class SimilarityCalculator
{
    private static final double SUMMARY_WEIGHT = 0.3;
    private static final double DESCRIPTION_WEIGHT = 0.7;

    public static Optional<Double> compare(Issue issue1, Issue issue2)
    {
        if (issue1 != null && issue1.getSummary() != null && issue1.getDescription() != null && issue2 != null && issue2.getSummary() != null && issue2.getDescription() != null)
        {
            double summarySimilarity = compareStrings(issue1.getSummary(), issue2.getSummary());
            double descriptionSimilarity = compareStrings(issue1.getDescription(), issue2.getDescription());
            return Optional.of((SUMMARY_WEIGHT * summarySimilarity) + (DESCRIPTION_WEIGHT * descriptionSimilarity));
        }
        return Optional.empty();
    }

    protected static double compareStrings(String string1, String string2)
    {
        return new CosineSimilarity().cosineSimilarity(stringToVector(string1), stringToVector(string2));
    }

    protected static Map<CharSequence, Integer> stringToVector(String string)
    {
        String language = getLanguage(string);
        Map<CharSequence, Integer> vector = new HashMap<>();
        List<String> tokens = tokenize(string);
        List<String> tokensWithoutStopwords = new StopwordRemover().remove(tokens, language);
        for (String token : tokensWithoutStopwords)
        {
            int count = Optional.ofNullable(vector.get(token)).orElse(0);
            count++;
            vector.put(token, count);
        }
        return vector;
    }

    protected static List<String> tokenize(String text)
    {
        String language = getLanguage(text);
        List<String> token = Arrays.stream(text.split("\\s+")).toList();
        return token.stream().map(SimilarityCalculator::normalizeToken).filter(item -> !item.isEmpty()).toList();
    }

    protected static String normalizeToken(String token)
    {
        List<String> invalidChars = List.of(",", ".", "!", "?", "(", ")", "]", "[", "{", "}", "/");
        for (String character : invalidChars)
        {
            token = token.replace(character, "");
        }
        return token.strip().toLowerCase();
    }

    private static String getLanguage(String text)
    {
        LanguageDetector languageDetector = new OptimaizeLangDetector().loadModels();
        return languageDetector.detect(text).getLanguage();
    }
}
