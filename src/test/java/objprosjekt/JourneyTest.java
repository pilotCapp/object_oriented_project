package objprosjekt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Hashtable;
import javafx.scene.shape.Ellipse;

public class JourneyTest {
    private Journey reise;
    private Ellipse reisemål1;
    private Ellipse reisemål2;
    private Ellipse reisemål3;
    Hashtable<String, Ellipse> destinasjoner;

    @BeforeEach
    public void setup() {
        reisemål1 = new Ellipse();
        reisemål1.setId("reisemål1");
        reisemål1.setLayoutX(10);
        reisemål1.setLayoutY(10);

        reisemål2 = new Ellipse();
        reisemål2.setId("reisemål2");
        reisemål2.setLayoutX(200);
        reisemål2.setLayoutY(200);

        reisemål3 = new Ellipse();
        reisemål3.setId("reisemål3");
        reisemål3.setLayoutX(30);
        reisemål3.setLayoutY(30);

        destinasjoner = new Hashtable<>();
        destinasjoner.put(reisemål1.getId(), reisemål1);
        destinasjoner.put(reisemål2.getId(), reisemål2);
        destinasjoner.put(reisemål3.getId(), reisemål3);

        reise = new Journey(destinasjoner);
    }

    @Test
    void validateLegalDestination() {
        // tester at kun indikerte destinasjoner kan bli lagt til i reisen
        Assertions.assertThrows(NullPointerException.class, () -> {
            reise.addCity("reisemål4");
        });
        // tester at en indikert reise kan bli lagt til i reisen
        Assertions.assertDoesNotThrow(() -> {
            reise.addCity("reisemål1");
        });
        // tester at en indikert reise ikke kan bli lagt til mer enn en gang
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            reise.addCity("reisemål1");
        });
    }

    @Test
    void validateDestinationRemover() {
        // tester at kun destinasjoner i reisen kan fjernes
        Assertions.assertThrows(NullPointerException.class, () -> {
            reise.removeCity("reisemål");
        });
        // tester at detsinasjoner i reisen kan fjernes
        reise.addCity("reisemål1");
        Assertions.assertDoesNotThrow(() -> {
            reise.removeCity("reisemål1");
        });
    }

    @Test
    void validateContainsCity() {
        // tester at en tom reise har ingen destinasjoner
        Assertions.assertFalse(reise.isIn("reisemål1"));
        reise.addCity("reisemål1");
        Assertions.assertTrue(reise.isIn("reisemål1"));
    }

    @Test
    void validateNumberOfDestinations() {
        // tester at en tom reise har ingen destinasjoner
        Assertions.assertEquals(0, reise.getSize());
        reise.addCity("reisemål1");
        // tester at et reisemål ble lagt til
        Assertions.assertEquals(1, reise.getSize());
        reise.addCity("reisemål2");
        // tester at to reisemål er lagt til
        Assertions.assertEquals(2, reise.getSize());
    }

    @Test
    void validateIndexOfJourney() {
        // tester at man ikke kan hente index til et mål som ikke er i reiseplanen
        Assertions.assertThrows(IllegalArgumentException.class, () -> reise.getIndex("reisemål1"));
        // tester at den første reisen
    }

    @Test
    void validateGetCityMethod() {
        // tester diverse indekser uten en by i reisen
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> reise.getCity(0));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> reise.getCity(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> reise.getCity(-1));
        reise.addCity("reisemål1");
        // tester diverse indekser med kun en by i reisen
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> reise.getCity(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> reise.getCity(-1));
        Assertions.assertDoesNotThrow(() -> reise.getCity(0));
    }

    @Test
    void validateToEllipseMethod() {
        // tester at metoden returnerer riktig ellipse objekt
        Assertions.assertEquals(reisemål1, reise.toEllipse("reisemål1", destinasjoner));
        Assertions.assertEquals(reisemål2, reise.toEllipse("reisemål2", destinasjoner));
        Assertions.assertEquals(reisemål3, reise.toEllipse("reisemål3", destinasjoner));
        // tester at metoden håndterer reisemål som ikke er en destinasjon
        Assertions.assertThrows(IllegalArgumentException.class, () -> reise.toEllipse("reisemål4", destinasjoner));
    }

    @Test
    void validateDistanceCalculator() {
        // tester at en tom reise har avstand på 0
        Assertions.assertEquals(0, reise.distance(destinasjoner));
        // tester at en reise med kun en by har avstand på 0
        reise.addCity("reisemål1");
        Assertions.assertEquals(0, reise.distance(destinasjoner));
        // tester reiser med 2 og 3 byer
        reise.addCity("reisemål2");
        Assertions.assertEquals(6059, reise.distance(destinasjoner));
        reise.addCity("reisemål3");
        Assertions.assertEquals(11481, reise.distance(destinasjoner));
        reise.clear();
        reise.addCity("reisemål3");
        // tester at reisen i motsat retning har samme avstand (med 1 km avrundingsfeil)
        Assertions.assertEquals(0, reise.distance(destinasjoner));
        reise.addCity("reisemål2");
        Assertions.assertEquals(11481 - 6059, reise.distance(destinasjoner), 1);
        reise.addCity("reisemål1");
        Assertions.assertEquals(11481, reise.distance(destinasjoner), 1);
    }
}