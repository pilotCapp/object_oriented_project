package objprosjekt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javafx.scene.shape.Ellipse;

public class JourneyFileHandlerTest {
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

    JourneyFileHandler filhåndterer;

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

        filhåndterer = new JourneyFileHandler(destinasjoner);
        filhåndterer.eraseFile();
        reiseplan = new JourneyPlan(destinasjoner);
    }

    @Test
    public void validateEreaseFileMethod() {
        // tester at construktøren til reiseplan hadde en tom fil å hente reiser fra
        // som betyr at det er kun en reise i reiseplanen
        Assertions.assertEquals(1, reiseplan.getSize());
    }

    @Test
    public void validateTextFromFileDoesNotTrow() {
        // tester at filhåndterer har en fil å hente tekst fra
        Assertions.assertDoesNotThrow(() -> filhåndterer.getTextFromFile(reiseplan));
    }

    @Test
    public void validateTextToFileDoesNotTrow() {
        // tester at filhåndter har en fil å sette tekst til
        Assertions.assertDoesNotThrow(() -> filhåndterer.setTextToFile(new ArrayList<Journey>()));
    }

    @Test
    public void validateTextToAndFromFile() {
        // lager en liste med en reise i
        List<Journey> reiseliste = new ArrayList<>();
        reiseliste.add(reise1);
        // legger reisen inn i filen (som var tom)
        filhåndterer.setTextToFile(reiseliste);
        // legger alle reiser fra filen inn i reiseplan
        filhåndterer.getTextFromFile(reiseplan);
        // tester at reisen ble lagt til i reiseplanen
        Assertions.assertEquals(2, reiseplan.getSize());
        // tester at reisen som ble lagt til er identisk den nye reisen i reiseplanen
        // med en comparator
        JourneyComparator reiseSammenligner = new JourneyComparator(destinasjoner);
        Assertions.assertEquals(0, reiseSammenligner.compare(reise1, reiseplan.getJourney(1)));
        // dobbeltsjekker at reisen begynner samme sted da comparatoren ikke sjekker
        // dette
        Assertions.assertEquals(reise1.getCity(0), reiseplan.getJourney(1).getCity(0));
    }

    @Test
    public void validateLegalDestinationsToFile() {
        // tester at en ikke godkjent reise ikke kan skrives til fil
        illegalJourneySetup();
        List<Journey> reiseliste = new ArrayList<>();
        reiseliste.add(reise4);
        Assertions.assertThrows(NullPointerException.class, () -> filhåndterer.setTextToFile(reiseliste));
        Assertions.assertNotEquals(reise4.distance(),
                reiseplan.getJourney(1).distance());

    }

    @Test
    public void validateLegalDestinationsFromFile() {
        // tester at en ikke godkjent reise ikke kan skrives fra fil
        illegalJourneySetup();
        // lager en ny reiseplan med flere godkjente reisedestinasjoner enn den
        // originale reiseplanen
        JourneyPlan ekstraReisePlan = new JourneyPlan(ekstraDestinasjoner);
        // legger til en reise med en destinasjon som ikke er godkjent i den originale
        // reisplanen
        ekstraReisePlan.addJourney(reise4);
        // lagrer så den nye reiseplanen til fil (skriver alle reisene og destinasjonene
        // til fil)
        ekstraReisePlan.save();

        // sjekker så at filhåndtereren ikke gjennkjenner denne nye ukjente
        // reisedestinasjonen
        Assertions.assertThrows(NullPointerException.class, () -> filhåndterer.getTextFromFile(reiseplan));
        // sjekker så at denne ukjente reise destinajsonen ikke har blitt lest fra fil
        // til den originale reiseplanen
        Assertions.assertNotEquals(ekstraReisePlan.getJourney(1).distance(),
                reiseplan.getJourney(1).distance());
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
