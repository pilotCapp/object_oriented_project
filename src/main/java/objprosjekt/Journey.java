package objprosjekt;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Ellipse;

public class Journey {
    private List<String> cities = new ArrayList<>();

    public void addCity(String by) {
        if (!cities.contains(by)) {
            cities.add(by);
        }
    }

    public void removeCity(String by) {
        if (cities.contains(by)) {
            cities.remove(by);
            removeCity(by);
        }
    }

    public int getIndex(String by) {
        if (cities.indexOf(by) == -1) {
            throw new IllegalArgumentException("Byen er ikke i reisen");
        } else {
            return (cities.indexOf(by));
        }
    }

    public int getSize() {
        return (cities.size());
    }

    public String getCity(int a) {
        return (cities.get(a));
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
                    toEllipse(cities.get(k), knapper).getParent().getLayoutX()
                            - toEllipse(cities.get(k + 1), knapper).getParent().getLayoutX(),
                    2)
                    + Math.pow(toEllipse(cities.get(k), knapper).getParent().getLayoutY()
                            - toEllipse(cities.get(k + 1), knapper).getParent().getLayoutY(), 2));
        }
        return (Math.round(distance * 22.55));
    }

    public Ellipse toEllipse(String navn, Hashtable<String, Ellipse> knapper) {
        if (knapper.get(navn) != null) {
            return (knapper.get(navn));
        } else {
            throw new IllegalArgumentException("den knappen finnes ikke");
        }
    }

}
