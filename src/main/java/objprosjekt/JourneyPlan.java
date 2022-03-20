package objprosjekt;

import java.util.List;
import java.util.ArrayList;

public class JourneyPlan {
    private List<Journey> journeyList = new ArrayList<>();

    public JourneyPlan() {
        this.journeyList.add(new Journey());
    }

    public void addJourney(Journey a) {
        this.journeyList.add(a);
    }

    public void removeJourney(Journey a) {
        this.journeyList.remove(a);
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

}
