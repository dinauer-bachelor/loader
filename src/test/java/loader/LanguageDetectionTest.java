package loader;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LanguageDetectionTest
{
    @Test
    void detectGerman()
    {
        // given
        String german_0 = """
            Eines Tages beschloss ein Huhn, genug vom Landleben zu haben.
            „Ich will online sein!“, gackerte es und pickte wild auf einen alten Router.
                        
            Der Bauer wunderte sich, warum plötzlich sein WLAN besser wurde.
            Stellt sich raus: Das Huhn hatte sich auf den Router gesetzt – für besseren Empfang.
                        
            Seitdem nennt man es nicht mehr einfach „Huhn“, sondern „Hotspot-Henriette“.
            Sie hat jetzt mehr Follower als der Traktor des Nachbarn.
        """;
        String german_1 = "Fehler beim Laden der Benutzerdaten nach dem Login";
        LanguageDetector languageDetector = new OptimaizeLangDetector().loadModels();

        //when
        LanguageResult result_0 = languageDetector.detect(german_0);
        LanguageResult result_1 = languageDetector.detect(german_1);

        // then
        assertEquals("de", result_0.getLanguage());
        assertEquals("de", result_1.getLanguage());
    }

    @Test
    void detectEnglish()
    {
        // given
        String english_0 = """
            One day, a chicken decided it was done with farm life.
            “I want to be online!” it clucked and pecked wildly at an old router.
            
            The farmer was confused why the Wi-Fi suddenly got better.
            Turns out, the chicken had sat on the router—for stronger signal.
            
            Since then, they don’t call it just a chicken anymore.
            It’s now Hotspot Henrietta, with more followers than the neighbor’s tractor.
        """;
        String english_1 = "User Data Fails to Load After Successful Login";
        LanguageDetector languageDetector = new OptimaizeLangDetector().loadModels();

        //when
        LanguageResult result_0 = languageDetector.detect(english_0);
        LanguageResult result_1 = languageDetector.detect(english_1);

        // then
        assertEquals("en", result_0.getLanguage());
        assertEquals("en", result_1.getLanguage());
    }
}
