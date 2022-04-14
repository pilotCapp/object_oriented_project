package objprosjekt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Hashtable;
import javafx.scene.shape.Ellipse;

public class JourneyPlanTest {
    private JourneyPlan reiseplan;

    private Journey reise1;
    private Journey reise2;
    private Journey reise3;
    private Journey reise4;

    private Ellipse reisemål1;
    private Ellipse reisemål2;
    private Ellipse reisemål3;
    private Ellipse reisemål4;

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

        JourneyFileHandler filhåndterer = new JourneyFileHandler(destinasjoner);
        filhåndterer.eraseFile();
        reiseplan = new JourneyPlan(destinasjoner);
    }

    @Test
    public void validategetSizeMethod() {
        Assertions.assertEquals(1, reiseplan.getSize());
    }

    @Test
    public void validateJourneyPlanConstructor() {
        Assertions.assertEquals(1, reiseplan.getSize());
        Assertions.assertEquals(Journey.class, reiseplan.getCurrentJourney().getClass());
    }

    @Test
    public void validateAddJourneyMethodes() {
        // tester å legge til ny reise
        reiseplan.addJourney();
        Assertions.assertEquals(2, reiseplan.getSize());
        // tester å legge til eksisterende reise
        Assertions.assertDoesNotThrow(() -> reiseplan.addJourney(reise1));
        // tester å legge til eksisterende reise med ikke-eksisterende destinasjoner
        illegalJourneySetup();
        Assertions.assertThrows(IllegalArgumentException.class, () -> reiseplan.addJourney(reise4));
    }

    @Test
    public void validateRemoveJourneyMethod() {
        // tester å legge til og så trekke fra reiser
        reiseplan.addJourney(reise1);
        reiseplan.addJourney(reise2);
        reiseplan.addJourney(reise3);
        Assertions.assertEquals(4, reiseplan.getSize());
        reiseplan.removeJourney();
        Assertions.assertEquals(3, reiseplan.getSize());
        reiseplan.removeJourney();
        Assertions.assertEquals(2, reiseplan.getSize());
        reiseplan.removeJourney();
        Assertions.assertEquals(1, reiseplan.getSize());
        // tester at den alltid har minst en reise i reiseplanen
        reiseplan.removeJourney();
        Assertions.assertEquals(1, reiseplan.getSize());

    }

    @Test
    public void validatePinEventMethod() {
        // tester at knappen som trykkes på eksisterer i henholdt til destinasjoner
        illegalJourneySetup();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> reiseplan.pinPressed("reise4", ekstraDestinasjoner));
    }

    @Test
    public void validateGetJourneyMethod() {
        // tester hent reise funksjon
        reiseplan.addJourney(reise1);
        reiseplan.addJourney(reise2);
        reiseplan.addJourney(reise3);
        Assertions.assertEquals(reise1, reiseplan.getJourney(1));
        Assertions.assertEquals(reise2, reiseplan.getJourney(2));
        Assertions.assertEquals(reise3, reiseplan.getJourney(3));
    }

    @Test
    public void validateGetCurrentJourneyMethod() {
        // tester at gjeldene reise følger nye reiser
        reiseplan.addJourney(reise1);
        Assertions.assertEquals(reise1, reiseplan.getCurrentJourney());
        reiseplan.addJourney(reise2);
        Assertions.assertEquals(reise2, reiseplan.getCurrentJourney());
    }

    @Test
    public void validateGetIndexMethod() {
        // tester index til gjeldende reise
        Assertions.assertEquals(0, reiseplan.getIndex(reiseplan.getCurrentJourney()));
        reiseplan.addJourney(reise1);
        Assertions.assertEquals(1, reiseplan.getIndex(reiseplan.getCurrentJourney()));
    }

    @Test
    public void validatSwitching() {
        // tester at man ikke kan gå videre fra siste reise
        Assertions.assertThrows(IllegalArgumentException.class, () -> reiseplan.next());
        // tester at man ikke kan gå tilbake fra første reise
        Assertions.assertThrows(IllegalArgumentException.class, () -> reiseplan.last());

        reiseplan.addJourney(reise1);
        reiseplan.addJourney(reise2);
        reiseplan.addJourney(reise3);

        // tester at gjeldende reise følger forflytningen
        Assertions.assertEquals(3, reiseplan.getIndex(reiseplan.getCurrentJourney()));
        reiseplan.last();
        Assertions.assertEquals(2, reiseplan.getIndex(reiseplan.getCurrentJourney()));
        reiseplan.last();
        Assertions.assertEquals(1, reiseplan.getIndex(reiseplan.getCurrentJourney()));
        reiseplan.next();
        Assertions.assertEquals(2, reiseplan.getIndex(reiseplan.getCurrentJourney()));
    }

    @Test
    public void validateSortedBehavior() {
        reiseplan.addJourney(reise1);
        reiseplan.addJourney(reise2);
        reiseplan.addJourney(reise3);
        reiseplan.setSorted(true);
        // sorterer reiseplanen med den største/lengste reisen først
        reiseplan.sort(reiseplan.getSorted());
        // tester at gjeldende reise framdeles er den nyeste
        Assertions.assertEquals(reise3, reiseplan.getCurrentJourney());

        // distansen til alle reisene til referanse
        Assertions.assertEquals(11481, reise1.distance(destinasjoner));
        Assertions.assertEquals(6059, reise2.distance(destinasjoner));
        Assertions.assertEquals(6697, reise3.distance(destinasjoner));

        // tester at indexen til reisene er endret i riktig rekkefølge
        Assertions.assertEquals(1, reiseplan.getIndex(reise3));
        Assertions.assertEquals(2, reiseplan.getIndex(reise2));
        Assertions.assertEquals(0, reiseplan.getIndex(reise1));
        // her kan det nevnes at reisen med index 3 er en tom reise og derfor den
        // korteste

        // tester så å bla mellom de ulike reisene i en sortert reiseplan
        reiseplan.last();
        Assertions.assertEquals(reise1, reiseplan.getCurrentJourney());
        reiseplan.next();
        Assertions.assertEquals(reise3, reiseplan.getCurrentJourney());
        reiseplan.next();
        Assertions.assertEquals(reise2, reiseplan.getCurrentJourney());
        reiseplan.next();
        Assertions.assertEquals(3, reiseplan.getIndex(reiseplan.getCurrentJourney()));

        // tester så å avslutte sorteringen og at den originale reisen har den første
        // indexen
        reiseplan.setSorted(false);
        reiseplan.sort(reiseplan.getSorted());
        Assertions.assertEquals(0, reiseplan.getIndex(reiseplan.getCurrentJourney()));
    }

    private void illegalJourneySetup() {
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
