package objprosjekt;

import java.util.Comparator;
import java.util.Hashtable;

import javafx.scene.shape.Ellipse;

public class JourneyComparator implements Comparator<Journey> {
    private Hashtable<String, Ellipse> allDestinations;

    public JourneyComparator(Hashtable<String, Ellipse> destinations) {
        this.allDestinations = destinations;
    }

    @Override
    public int compare(Journey o1, Journey o2) {
        if(o1.getAllDestinations()!=this.allDestinations||o2.getAllDestinations()!=this.allDestinations){
            throw new NullPointerException("kan ikke sammenligne når en av reisene har en annen basis av destinasjoner");
        } else if (o1.getSize() > o2.getSize()) {
            return (-1);
        } else if (o1.getSize() == o2.getSize()) {
            if (o1.distance(allDestinations) > o2.distance(allDestinations)) {
                return (-1);
            } else if (o1.distance(allDestinations) < o2.distance(allDestinations)) {
                return (1);
            } else {
                return (0);
            }
        } else {
            return (1);
        }
    }

}