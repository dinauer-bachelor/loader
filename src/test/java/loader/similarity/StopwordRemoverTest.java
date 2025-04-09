package loader.similarity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class StopwordRemoverTest
{
    @Test
    void testGerman()
    {
        // given
        List<String> tokens = List.of(
            "es", "gibt", "kaum", "etwas", "schöneres", "als", "den", "ersten", "kaffee", "am", "morgen",
            "noch", "bevor", "der", "tag", "richtig", "beginnt", "bevor", "die", "emails", "sich", "stapeln",
            "oder", "das", "handy", "zum", "zwanzigsten", "mal", "vibriert", "steht", "er", "da", "dampfend",
            "duftend", "verheißungsvoll"
        );

        // when
        List<String> withoutStopwords = new StopwordRemover().remove(tokens, "de");

        // then
        Assertions.assertTrue(tokens.size() > withoutStopwords.size());
    }

    @Test
    void testEnglish()
    {
        // given
        List<String> tokens = List.of(
                "there", "is", "hardly", "anything", "more", "beautiful", "than", "the", "first", "coffee", "in", "the", "morning",
                "even", "before", "the", "day", "really", "begins", "before", "the", "emails", "pile", "up",
                "or", "the", "phone", "vibrates", "for", "the", "twentieth", "time",
                "it", "stands", "there", "steaming", "fragrant", "full", "of", "promise"
        );

        // when
        List<String> withoutStopwords = new StopwordRemover().remove(tokens, "en");

        // then
        Assertions.assertTrue(tokens.size() > withoutStopwords.size());
    }
}
