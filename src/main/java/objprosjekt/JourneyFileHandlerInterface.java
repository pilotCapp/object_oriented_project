package objprosjekt;

import java.util.List;

public interface JourneyFileHandlerInterface {
    //setter interface med en metode for å skrive til fil og en metode for å lese fra fil
    public void setTextToFile(List<Journey> reiser);
    public void getTextFromFile(JourneyPlan reiser);
}
