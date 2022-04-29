package objprosjekt;

import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import javafx.scene.shape.Ellipse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class JourneyFileHandler implements JourneyFileHandlerInterface {
    // lager basis for alle destinasjoner
    private Hashtable<String, Ellipse> destinations;

    // oppretter basis fra oppstart
    public JourneyFileHandler(Hashtable<String, Ellipse> allDestinations) {
        destinations = allDestinations;
    }

    // skriver den sorterte reiselisten fra reiseplan til tekstfil
    public void setTextToFile(List<Journey> alleReiser) {
        // oppretter PrintWriter med ferdig opprettet tekstfil
        try (PrintWriter skriver = new PrintWriter("txtJourneyPlan.txt")) {
            for (Journey k : alleReiser) {
                for (int l = 0; l < k.getSize(); l++) {
                    if (destinations.get(k.getCity(l)) != null) {
                        skriver.print(k.getCity(l) + ",");
                    } else {// dersom listen inneolder en destinasjon som ikke gjennkjennes av basis
                        throw new NullPointerException(k.getCity(l) + " er ikke en godkjent destinasjon");
                    }
                }
                skriver.println();
            }
            skriver.flush();
            skriver.close();
        } catch (FileNotFoundException e) {// kaster exception dersom filen ikke eksisterer
            e.printStackTrace();
        }
    }

    // tar inn en reiseplan og skriver alle reisene i tekstfilen til reiseplanen
    public void getTextFromFile(JourneyPlan alleReiser) {
        Scanner scanner;
        try {// oppretter en Scanner for ferdig opprettet tekstfil
            scanner = new Scanner(new File("txtJourneyPlan.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] reise = line.split(",");
                Journey planlagtReise = new Journey(destinations);
                for (String k : reise) {
                    if (k != null && k != "," && k != "") {
                        if (destinations.get(k) == null) {// dersom det er skrevet en destinasjon på fil som den ikke
                                                          // gjennkjenner fra basis
                            throw new NullPointerException(k + "er ikke en godkjent destinasjon");
                        } else {
                            planlagtReise.addCity(k);
                        }
                    }
                }
                alleReiser.addJourney(planlagtReise);
            }
            scanner.close();
        } catch (FileNotFoundException e) {//kaster exception dersom filen ikke eksisterer
            e.printStackTrace();
        }
        //dersom reiseplanen er tom legger den til en ny reise (gjør dette også internt i reiseplanen da det alltid må være minst en reise i reiseplanen)
        
        if (alleReiser.getSize() == 0) {
            alleReiser.addJourney();
        }
    }
    //fjerner all tekst fra tekstfilen (spesialtilfelle av setTextToFile der listen er tom)
    public void eraseFile() {
        try (PrintWriter skriver = new PrintWriter("txtJourneyPlan.txt")) {
            skriver.flush();
            skriver.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
