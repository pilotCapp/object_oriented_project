package objprosjekt;

import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import java.util.ArrayList;
import java.util.Hashtable;

public class JourneyPlan {
    private List<Journey> unSortedJourneyList = new ArrayList<>();
    private List<Journey> journeyList;
    private List<Journey> sortedJourneyList;

    private JourneyFileHandler filHåndterer;
    private Journey currentJourney;
    private Hashtable<String, Ellipse> destinations;
    private JourneyComparator sorter;
    private boolean sorted;

    public JourneyPlan(Hashtable<String, Ellipse> allDestinations) {
        destinations = allDestinations;
        sorted = false;
        filHåndterer = new JourneyFileHandler(allDestinations);
        sorter = new JourneyComparator(destinations);
        journeyList = unSortedJourneyList;
        filHåndterer.getTextFromFile(this);
        if (this.getSize() == 0) {
            addJourney();
        }
        currentJourney = journeyList.get(0);
    }

    public void setSorted(boolean isSorted) {
        sorted = isSorted;
    }

    public boolean getSorted() {
        return (this.sorted);
    }

    public void addJourney(Journey reise) {
        if (reise.getAllDestinations().equals(this.destinations)) {
            unSortedJourneyList.add(reise);
            this.sort(sorted);
            currentJourney = this.getJourney(this.getSize() - 1);// -1
        } else {
            throw new IllegalArgumentException("reisen har ikke-eksisterende reisedestinasjoner");
        }
    }

    public void addJourney() {
        Journey nyReise = new Journey(destinations);
        unSortedJourneyList.add(nyReise);
        currentJourney = nyReise;
        this.sort(sorted);
    }

    public void removeJourney() {
        if (this.getSize() > 1) {
            this.sort(sorted);
            int a = this.getIndex(currentJourney);
            this.unSortedJourneyList.remove(a);
            this.sort(sorted);
            currentJourney = this.getJourney(a);
        } else {
            currentJourney.clear();
        }
    }

    public void pinPressed(String ID, Hashtable<String, Ellipse> knapper) {
        if (!knapper.equals(destinations)) {
            throw new IllegalArgumentException("basis av knapper er ikke likt basis av destionasjoner");
        } else if (currentJourney.isIn(ID)) {
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
        filHåndterer.setTextToFile(unSortedJourneyList);
    }

    public Journey getCurrentJourney() {
        return (this.currentJourney);
    }

    public void next() {
        if (this.getSize() > this.getIndex(currentJourney) + 1) {// -1
            currentJourney = this.getJourney(this.getIndex(currentJourney) + 1);// -1
        } else {
            throw new IllegalArgumentException("du er på siste reise");
        }
    }

    public void last() {
        if (this.getIndex(currentJourney) >= 1) {
            currentJourney = this.getJourney(this.getIndex(currentJourney) - 1);
        } else {
            throw new IllegalArgumentException("du er på første reise");
        }
    }

    public void sort(boolean sorted) {
        sortedJourneyList = unSortedJourneyList.stream().sorted(sorter).collect(Collectors.toList());
        if (sorted) {
            journeyList = sortedJourneyList;
        } else {
            journeyList = unSortedJourneyList;
        }
    }

    public List<Journey> getSortedJourneyList() {
        return (this.sortedJourneyList);
    }
}
