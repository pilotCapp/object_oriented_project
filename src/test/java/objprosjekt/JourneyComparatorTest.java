package objprosjekt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Hashtable;

import javafx.scene.shape.Ellipse;

public class JourneyComparatorTest {
    private JourneyComparator reiseSammenligner;
    private JourneyPlan reiseplan;

    private Journey reise1;
    private Journey reise2;
    private Journey reise3;

    private Journey reise4;

    private Journey reise1tilbake;
    private Journey reise2tilbake;
    private Journey reise3tilbake;

    private Journey ekstraKortReise;

    private Ellipse reisemål1;
    private Ellipse reisemål2;
    private Ellipse reisemål3;
    private Ellipse reisemål4;

    private JourneyFileHandler filhåndterer;
    private Hashtable<String, Ellipse> destinasjoner;
    private Hashtable<String, Ellipse> ekstraDestinasjoner;

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

        reise1 = new Journey(destinasjoner);
        reise1.addCity("reisemål1");
        reise1.addCity("reisemål2");
        reise1.addCity("reisemål3");

        reise2 = new Journey(destinasjoner);
        reise2.addCity("reisemål2");
        reise2.addCity("reisemål3");
        reise2.addCity("reisemål1");

        reise3 = new Journey(destinasjoner);
        reise3.addCity("reisemål3");
        reise3.addCity("reisemål1");
        reise3.addCity("reisemål2");

        reise1tilbake = new Journey(destinasjoner);
        reise1tilbake.addCity("reisemål3");
        reise1tilbake.addCity("reisemål2");
        reise1tilbake.addCity("reisemål1");

        reise2tilbake = new Journey(destinasjoner);
        reise2tilbake.addCity("reisemål1");
        reise2tilbake.addCity("reisemål3");
        reise2tilbake.addCity("reisemål2");

        reise3tilbake = new Journey(destinasjoner);
        reise3tilbake.addCity("reisemål2");
        reise3tilbake.addCity("reisemål1");
        reise3tilbake.addCity("reisemål3");

        ekstraKortReise = new Journey(destinasjoner);
        ekstraKortReise.addCity("reisemål1");
        ekstraKortReise.addCity("reisemål2");

        filhåndterer = new JourneyFileHandler(destinasjoner);
        filhåndterer.eraseFile();
        reiseplan = new JourneyPlan(destinasjoner);

        reiseSammenligner = new JourneyComparator(destinasjoner);
    }

    @Test
    public void validateCorrectComparing() {
        // tester at reisene er sortert riktig etter avstand
        Assertions.assertEquals(-1, reiseSammenligner.compare(reise1, reise2));
        Assertions.assertEquals(-1, reiseSammenligner.compare(reise1, reise3));
        Assertions.assertEquals(1, reiseSammenligner.compare(reise2, reise1));
        Assertions.assertEquals(1, reiseSammenligner.compare(reise3, reise1));

        // tester at reisene er sortert riktig etter anntall destinasjoner
        Assertions.assertEquals(1, reiseSammenligner.compare(ekstraKortReise, reise1));
        Assertions.assertEquals(1, reiseSammenligner.compare(ekstraKortReise, reise2));
        Assertions.assertEquals(1, reiseSammenligner.compare(ekstraKortReise, reise3));

        // tester at reisene er sortert likt når det er samme reise
        Assertions.assertEquals(0, reiseSammenligner.compare(reise1, reise1));
        Assertions.assertEquals(0, reiseSammenligner.compare(reise2, reise2));
        Assertions.assertEquals(0, reiseSammenligner.compare(reise3, reise3));

        // tester at reisene er sortert likt dersom reisene er speilvendte
        Assertions.assertEquals(0, reiseSammenligner.compare(reise1tilbake, reise1));
        Assertions.assertEquals(0, reiseSammenligner.compare(reise2tilbake, reise2));
        Assertions.assertEquals(0, reiseSammenligner.compare(reise3tilbake, reise3));
    }

    @Test
    public void validateIllegalComparing() {
        illegalJourneySetup();
        // tester at den kan sammenligne to reiser med samme basis for destinasjoner
        Assertions.assertDoesNotThrow(() -> reiseSammenligner.compare(reise1, reise3));
        // tester at den ikke kan sammenligne to reiser med ulik basis for destinasjoner
        Assertions.assertThrows(NullPointerException.class, () -> reiseSammenligner.compare(reise1, reise4));
    }

    private void illegalJourneySetup() {
        // lager en ny destinasjon i tillegg til en reise og hashtable som inneholder
        // destinasjonen
        reisemål4 = new Ellipse();
        reisemål4.setId("reisemål4");
        reisemål4.setLayoutX(100);
        reisemål4.setLayoutY(100);

        ekstraDestinasjoner = new Hashtable<>();
        ekstraDestinasjoner.put(reisemål1.getId(), reisemål1);
        ekstraDestinasjoner.put(reisemål2.getId(), reisemål2);
        ekstraDestinasjoner.put(reisemål3.getId(), reisemål3);
        ekstraDestinasjoner.put(reisemål4.getId(), reisemål4);

        reise4 = new Journey(ekstraDestinasjoner);
        reise4.addCity("reisemål4");
        reise4.addCity("reisemål3");
        reise4.addCity("reisemål2");
        reise4.addCity("reisemål1");
    }
}
