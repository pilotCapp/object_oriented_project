package objprosjekt;

import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.shape.Ellipse;
import java.util.ArrayList;
import java.util.Hashtable;

public class JourneyPlan {
    private Hashtable<String, Ellipse> destinations;// basis for reiseplanen med alle mulige reisedestiasjoner

    // tre sorrteringslister
    private List<Journey> unSortedJourneyList = new ArrayList<>();// den usorterte reiselisten er hovedlisten som
                                                                  // inneholder alle reisene fra den eldste til den
                                                                  // nyeste
    private List<Journey> sortedJourneyList;// den sorterte reiselisten er en sortert versjon av den usorterte
                                            // reiselisten utifra største reisen til den minste
    private List<Journey> journeyList;// journeyList er den reiselisten man bruker, og vil variere mellom den sorterte
                                      // og ikke-sorterte utifra om man har valgt å sortere eller ikke

    private JourneyFileHandler fileManager;// filhåndtereren til reiseplanen
    private Journey currentJourney;// den nåværende reisen som er den reisen man aktivt ser på
    private JourneyComparator sortBySize;// en comparator som vil sortere reisene fra størst til minst (først mengde
                                         // destinasjoner, og så avstand dersom mengden destinasjoner er lik)
    private boolean sorted;// boolean som varierer utifra om man vil sortere eller ikke

    // oppstart
    public JourneyPlan(Hashtable<String, Ellipse> allDestinations) {
        destinations = allDestinations;// tar inn basis for destinasjoner
        sorted = false;// setter sortering som false
        fileManager = new JourneyFileHandler(allDestinations);// oppretter filhåndterer
        sortBySize = new JourneyComparator(destinations);// oppretter reisesorterer (comparator)
        journeyList = unSortedJourneyList;// setter den nåverende reiselisten som den usorterte
        fileManager.getTextFromFile(this);// henter eksisterende reiseplan fra fil og skriver til denne reiseplanen
        // dersom reiseplanen er tom etter oppstart legger man inn en ny reise da en
        // reiseplan alltid må ha minst en reise
        if (this.getSize() == 0) {
            addJourney();
        }
        // setter nåværende reise som den første reisen i reiseplanen
        currentJourney = journeyList.get(0);
    }

    // setter sorteringsboolean
    public void setSorted(boolean isSorted) {
        sorted = isSorted;
    }

    // henter sorteringsboolean
    public boolean getSorted() {
        return (this.sorted);
    }

    // legge til en eksisterende reise i reiseplanen
    public void addJourney(Journey reise) {
        if (reise.getAllDestinations().equals(this.destinations)) { // hvis destinasjoner i en journey/reise er lik
                                                                    // destinasjoner i reiseplanen
            unSortedJourneyList.add(reise); // legge til reisen i planen
            this.sort(sorted); // dersom den skal sortere
            currentJourney = this.getJourney(this.getSize() - 1);// -1
        } else {
            throw new IllegalArgumentException("reisen og reiseplanen har ulik basis for reisedestinasjoner");
        }
    }

    // legge til en ny reise i reiseplanen
    public void addJourney() {
        Journey nyReise = new Journey(this.destinations);
        unSortedJourneyList.add(nyReise);
        currentJourney = nyReise;
        this.sort(sorted);// sorterer utifra om man skal sortere eller ikke
    }

    // fjerner den nåværende reisen i reiseplanen dersom det er 2 eller flere reiser
    // i planen
    public void removeJourney() {
        if (this.getSize() > 1) {
            this.sort(sorted);
            int a = unSortedJourneyList.indexOf(currentJourney);
            int b=this.getIndex(currentJourney);
            this.unSortedJourneyList.remove(a);
            this.sort(sorted);
            currentJourney = this.getJourney(b);
        } else {// fjerner alle destinasjoner i den nåværende reisen dersom det er den eneste
                // reisen i planen
            currentJourney.clear();
        }
    }

    public void manageDestination(String ID, Hashtable<String, Ellipse> knapper) { // hva som skjer hvis ellipsene er trykket
        if (!knapper.equals(destinations)) {
            throw new IllegalArgumentException("basis av knapper er ikke likt basis av destionasjoner");
        } else if (currentJourney.isIn(ID)) {
            currentJourney.removeCity(ID);
            // currentJourney.toEllipse(ID, knapper).setFill(Color.RED);
        } else {
            currentJourney.addCity(ID);
            // currentJourney.toEllipse(ID, knapper).setFill(Color.GREEN);
        }
    }

    // returnerer index til valgt reise dersom reisen er i planen
    public int getIndex(Journey a) {
        if (journeyList.indexOf(a) == -1) {
            throw new IllegalArgumentException("Reisen er ikke i planen");
        } else {
            return (journeyList.indexOf(a));
        }
    }

    // returnerer mengde reiser i planen
    public int getSize() {
        return (journeyList.size());
    }

    // returnerer reise i valgt index dersom den har en reise i den indexen
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

    // skriver den usorterte reiselisten til fil
    public void save() {
        fileManager.setTextToFile(unSortedJourneyList);
    }

    // returnerer den nåværende reisen
    public Journey getCurrentJourney() {
        return (this.currentJourney);
    }

    public void next() { // gå til neste ved 'next' knapp, så lenge man ikke er i siste reise
        if (this.getSize() > this.getIndex(currentJourney) + 1) {// -1
            currentJourney = this.getJourney(this.getIndex(currentJourney) + 1);// -1
        } else {
            throw new IllegalArgumentException("du er på siste reise");
        }
    }

    public void last() { // gå tilbake ved 'back' knapp, så lenge man ikke er i første reise
        if (this.getIndex(currentJourney) >= 1) {
            currentJourney = this.getJourney(this.getIndex(currentJourney) - 1);
        } else {
            throw new IllegalArgumentException("du er på første reise");
        }
    }

    public void sort(boolean sorted) {
        // setter den sorterte reiselisten som en sortert versjon av den usorterte
        // reiselisten
        sortedJourneyList = unSortedJourneyList.stream().sorted(sortBySize).collect(Collectors.toList());
        // dersom man skal sortere
        if (sorted) {
            journeyList = sortedJourneyList;// setter man den nåværende reiselisten som den sorterte reiselisten
        }
        // dersom man ikke skal sortere
        else {
            journeyList = unSortedJourneyList;// setter men den nåværende reiselisten som den usorterte reiselisten
        }
    }
    //returnerer den sorterte reiselisten
    public List<Journey> getSortedJourneyList() {
        return (this.sortedJourneyList);
    }
}
