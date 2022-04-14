package objprosjekt;

import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import javafx.scene.shape.Ellipse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class JourneyFileHandler implements JourneyFileHandlerInterface {
    Hashtable<String, Ellipse> allDestinations;

    public JourneyFileHandler(Hashtable<String, Ellipse> allDestinations) {
        this.allDestinations = allDestinations;
    }

    public void setTextToFile(List<Journey> alleReiser) {
        try (PrintWriter skriver = new PrintWriter("txtJourneyPlan.txt")) {
            for (Journey k : alleReiser) {
                for (int l = 0; l < k.getSize(); l++) {
                    if (allDestinations.get(k.getCity(l)) != null) {
                        skriver.print(k.getCity(l) + ",");
                    } else {
                        throw new NullPointerException(k.getCity(l) + " er ikke en godkjent destinasjon");
                    }
                }
                skriver.println();
            }
            skriver.flush();
            skriver.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getTextFromFile(JourneyPlan alleReiser) {
        Scanner scanner;
        try {
            scanner = new Scanner(new File("txtJourneyPlan.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] reise = line.split(",");
                Journey planlagtReise = new Journey(allDestinations);
                for (String k : reise) {
                    if (k != "," && k != "") {
                        if (allDestinations.get(k) == null) {
                            throw new NullPointerException(k + "er ikke en godkjent destinasjon");
                        } else {
                            planlagtReise.addCity(k);
                        }
                    }
                }
                alleReiser.addJourney(planlagtReise);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (alleReiser.getSize() == 0) {
            alleReiser.addJourney();
        }
    }

    public void eraseFile() {
        try (PrintWriter skriver = new PrintWriter("txtJourneyPlan.txt")) {
            skriver.flush();
            skriver.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
