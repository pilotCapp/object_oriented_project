package objprosjekt;

import java.util.List;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class JourneyFileHandler implements JourneyFileHandlerInterface {

    public void setTextToFile(List<Journey> alleReiser) {
        try (PrintWriter skriver = new PrintWriter("txtJourneyPlan.txt")) {
            for (Journey k : alleReiser) {
                for (int l = 0; l < k.getSize(); l++) {
                    skriver.print(k.getCity(l) + ",");
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
                Journey planlagtReise = new Journey();
                for (String k : reise) {
                    if (k != null && k != "," && k != "") {
                        planlagtReise.addCity(k);
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
}
