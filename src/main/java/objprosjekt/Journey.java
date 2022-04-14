package objprosjekt;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Ellipse;

public class Journey {
    private List<String> cities = new ArrayList<>();
    private Hashtable<String, Ellipse> destinations;

    public Journey(Hashtable<String, Ellipse> possibleDestinations) {
        destinations = possibleDestinations;
    }

    public void addCity(String by) {
        if (cities.contains(by)) {
            throw new IllegalArgumentException("kan ikke reise til samme by to ganger");
        } else if (destinations.get(by) == null) {
            throw new NullPointerException("Det reisemålet eksisterer ikke");
        } else {
            cities.add(by);
        }

    }

    public void removeCity(String by) {
        if (!cities.remove(by)) {
            throw new NullPointerException();
        }
    }

    public int getIndex(String by) {
        if (cities.indexOf(by) == -1) {
            throw new IllegalArgumentException("Kan ikke finne index til en by som ikke er i reisen");
        } else {
            return (cities.indexOf(by));
        }
    }

    public int getSize() {
        return (cities.size());
    }

    public String getCity(int a) {
        try {
            return (cities.get(a));
        } catch (IndexOutOfBoundsException e) {
            throw (e);
        }
    }

    public boolean isIn(String by) {
        if (cities.contains(by)) {
            return (true);
        } else {
            return (false);
        }
    }

    public void clear() {
        cities.clear();
    }

    public long distance(Hashtable<String, Ellipse> knapper) {
        Double distance = 0.0;
        for (int k = 0; k < cities.size() - 1; k++) {
            distance += Math.sqrt(Math.pow(
                    toEllipse(cities.get(k), knapper).getLayoutX()
                            - toEllipse(cities.get(k + 1), knapper).getLayoutX(),
                    2)
                    + Math.pow(toEllipse(cities.get(k), knapper).getLayoutY()
                            - toEllipse(cities.get(k + 1), knapper).getLayoutY(), 2));
        }
        return (Math.round(distance * 22.55));
    }

    public Ellipse toEllipse(String by, Hashtable<String, Ellipse> destinasjoner) {
        if (destinasjoner.get(by) != null) {
            return (destinasjoner.get(by));
        } else {
            throw new IllegalArgumentException("den destinasjoner eksister ikke");
        }
    }
    public Hashtable<String,Ellipse> getAllDestinations(){
        return(this.destinations);
    }

}
