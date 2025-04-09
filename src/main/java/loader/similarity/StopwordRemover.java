package loader.similarity;

import org.apache.kafka.common.protocol.types.Field;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

import java.util.*;
import java.util.stream.Collectors;

public class StopwordRemover
{
    public List<String> remove(List<String> token, String language)
    {
        Set<String> stopwords = getStopwords(language);

        return token.stream().filter(item -> !stopwords.contains(item)).toList();
    }

    private Set<String> getStopwords(String language)
    {
        if("de".equals(language))
        {
            return toSet(GermanAnalyzer.getDefaultStopSet().toString());
        }
        if("en".equals(language))
        {
            return toSet(EnglishAnalyzer.getDefaultStopSet().toString());
        }
        return Set.of();
    }

    private Set<String> toSet(String text)
    {
        return Arrays.stream(text.replace("[", "").replace("]", "").split(",")).map(String::strip).map(item -> item.replace(",", "")).collect(Collectors.toSet());
    }
}
