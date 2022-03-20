package objprosjekt;

//import java.util.List;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Ellipse;

public class Journey {
    private List<Ellipse> cities = new ArrayList<>();

    public void addCity(Ellipse by) {
        if (!this.isIn(by)) {
            cities.add(by);
        }
    }

    public void removeCity(Ellipse by) {
        if (this.isIn(by)) {
            cities.remove(by);
            removeCity(by);
        }
    }

    public int getIndex(Ellipse by) {
        if (cities.indexOf(by) == -1) {
            throw new IllegalArgumentException("Byen er ikke i reisen");
        } else {
            return (cities.indexOf(by));
        }
    }

    public int getSize() {
        return (cities.size());
    }

    public Ellipse getCity(int a) {
        return (cities.get(a));
    }

    public boolean isIn(Ellipse by) {
        if (cities.contains(by)) {
            return (true);
        } else {
            return (false);
        }
    }

    public void clear() {
        cities.clear();
    }

    public long distance() {
        Double distance = 0.0;
        for (int k = 0; k < cities.size() - 1; k++) {
            distance += Math.sqrt(Math
                    .pow(cities.get(k).getParent().getLayoutX() - cities.get(k + 1).getParent().getLayoutX(), 2)
                    + Math.pow(cities.get(k).getParent().getLayoutY() - cities.get(k + 1).getParent().getLayoutY(), 2));
        }
        return (Math.round(distance * 22.55));
    }

}
