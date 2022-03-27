package objprosjekt;

import java.util.List;
import java.util.Scanner;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

public class JourneyPlan {
    private List<Journey> journeyList = new ArrayList<>();
    private JourneyFileHandler filHåndterer = new JourneyFileHandler();
    private Journey currentJourney;

    public JourneyPlan() {
        filHåndterer.getTextFromFile(this);
        if (this.getSize() == 0) {
            addJourney();
        }
        currentJourney = journeyList.get(0);
    }

    public void addJourney(Journey reise) {
        journeyList.add(reise);
        currentJourney = this.getJourney(this.getSize() - 1);// -1
    }

    public void addJourney() {
        journeyList.add(new Journey());
        currentJourney = this.getJourney(this.getSize() - 1);// -1
    }

    public void removeJourney() {
        if (this.getSize() > 1) {
            int a = this.getIndex(currentJourney);
            this.journeyList.remove(a);
            currentJourney = this.getJourney(a);
        } else {
            currentJourney.clear();
        }
    }

    public void pinPressed(String ID, Hashtable<String, Ellipse> knapper) {
        if (currentJourney.isIn(ID)) {
            currentJourney.removeCity(ID);
            currentJourney.toEllipse(ID, knapper).setFill(Color.RED);
        } else {
            currentJourney.addCity(ID);
            currentJourney.toEllipse(ID, knapper).setFill(Color.GREEN);
        }
    }

    public int getIndex(Journey a) {
        if (journeyList.indexOf(a) == -1) {
            throw new IllegalArgumentException("Reisen er ikke i planen");
        } else {
            return (journeyList.indexOf(a));
        }
    }

    public int getSize() {
        return (journeyList.size());
    }

    public Journey getJourney(int i) {
        try {
            return (journeyList.get(i));
        } catch (Exception invocationTargetExveException) {
            if (journeyList.size() == 0) {
                throw new IllegalArgumentException("det er ingen reiser i planen");
            } else if (journeyList.size() - 1 < i) {
                return (this.getJourney(i - 1));
            } else {
                return (this.getJourney(0));
            }
        }

    }

    public void save() {
        filHåndterer.setTextToFile(journeyList);
    }

    public Journey getCurrentJourney() {
        return (this.currentJourney);
    }

    public void next() {
        if (this.getSize() > this.getIndex(currentJourney) + 1) {// -1
            currentJourney = this.getJourney(this.getIndex(currentJourney) + 1);// -1
        }
    }

    public void last() {
        if (this.getIndex(currentJourney) >= 1) {
            currentJourney = this.getJourney(this.getIndex(currentJourney) - 1);
        }
    }
}
