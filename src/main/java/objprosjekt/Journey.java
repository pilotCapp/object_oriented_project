package objprosjekt;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Ellipse;

public class Journey {
    private List<String> cities = new ArrayList<>();
    private Hashtable<String, Ellipse> destinations;

    //oppretter basis for mulige destinasjoner i reisen ved oppstart
    public Journey(Hashtable<String, Ellipse> possibleDestinations) {
        destinations = possibleDestinations;
    }

    //legger til ny destinasjon i reisen dersom 
    public void addCity(String by) {
        if (cities.contains(by)) {//byen ikke likker i reisen fra før av
            throw new IllegalArgumentException("kan ikke reise til samme by to ganger");
        } else if (destinations.get(by) == null) {//destinasjonen eksisterer i hendhold til satt basis^^
            throw new NullPointerException("Det reisemålet eksisterer ikke");
        } else {
            cities.add(by);
        }

    }

    public void removeCity(String by) {
        if (!cities.remove(by)) {//prøver å gjerne destinasjon dersom den er i reisen
            throw new NullPointerException();
        }
    }
    //returnerer indeks til reisen dersom den er i reisen
    public int getIndex(String by) {
        if (cities.indexOf(by) == -1) {
            throw new IllegalArgumentException("Kan ikke finne index til en by som ikke er i reisen");
        } else {
            return (cities.indexOf(by));
        }
    }

    //returnerer mengden med destinasjoner i reiser
    public int getSize() {
        return (cities.size());
    }

    //returnerer destinasjonen på valgt indeks dersom det eksisterer en destinasjon med valgt index 
    public String getCity(int a) {
        try {
            return (cities.get(a));
        } catch (IndexOutOfBoundsException e) {
            throw (e);
        }
    }

    //returnerer true dersom destinasjonen er i reisen
    public boolean isIn(String by) {
        if (cities.contains(by)) {
            return (true);
        } else {
            return (false);
        }
    }

    //fjerner alle desitnasjoner i reisen
    public void clear() {
        cities.clear();
    }

    //regner avstanden på reisen ved å addere avstanden mellom alle destinasjonene i reisen
    public long distance() {
        Double distance = 0.0;
        for (int k = 0; k < cities.size() - 1; k++) {
            distance += Math.sqrt(Math.pow(
                    toEllipse(cities.get(k)).getLayoutX()
                            - toEllipse(cities.get(k + 1)).getLayoutX(),
                    2)
                    + Math.pow(toEllipse(cities.get(k)).getLayoutY()
                            - toEllipse(cities.get(k + 1)).getLayoutY(), 2));
        }
        return (Math.round(distance * 22.55));
    }

    //tar in en destinasjon som string og returnerer Ellipsen til destinasjonen
    public Ellipse toEllipse(String by) {
        if (destinations.get(by) != null) {
            return (destinations.get(by));
        } else {
            throw new IllegalArgumentException("den destinasjoner eksister ikke");
        }
    }

    //returnerer basis for alle destinasjonene til reisen som ble satt ved oppstart av reisen^^
    public Hashtable<String, Ellipse> getAllDestinations() {
        return (this.destinations);
    }
}


