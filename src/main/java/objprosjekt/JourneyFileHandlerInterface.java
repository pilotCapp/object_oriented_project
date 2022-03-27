package objprosjekt;

import java.util.List;

public interface JourneyFileHandlerInterface {
    public void setTextToFile(List<Journey> reiser);
    public void getTextFromFile(JourneyPlan reiser);
}
