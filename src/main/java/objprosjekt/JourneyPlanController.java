package objprosjekt;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import java.util.Hashtable;

public class JourneyPlanController {

    @FXML
    private Ellipse NewYork, Oslo, CapeTown, Mumbai, Paris, London, Madrid, LosAngeles, Toronto, Rio, Reykjavik, Moscow,
            BuenosAires, Beijing, MexicoCity, Bogota, Sydney;

    @FXML
    private AnchorPane anker;
    @FXML
    private Canvas panel;
    @FXML
    private Button btnBack, btnNext, btnAdd, btnDelete, btnSave, btnLoad;
    @FXML
    private Label txtCurrentJourney, txtIndex;
    @FXML
    private ToggleButton autoToggle, btnSorted;

    private JourneyPlan travelPlan;
    private Hashtable<String, Ellipse> knapper;

    @FXML
    public void initialize() { // hva som skjer når man åpner applikasjnen
        openHash(); // åpner hashtable
        travelPlan = new JourneyPlan(knapper); // oppretter travelPlan med en ny JourneyPlan og tar inn valgte
                                               // destinasjoner
        travelPlan.setSorted(btnSorted.isSelected()); // legger inn i reiseplan om sortering er av eller på
        write();
        buttonCheck();
        draw();
    }

    public void openHash() {
        knapper = new Hashtable<>();
        knapper.put("NewYork", NewYork);
        knapper.put("Oslo", Oslo);
        knapper.put("CapeTown", CapeTown);
        knapper.put("Mumbai", Mumbai);
        knapper.put("Paris", Paris);
        knapper.put("London", London);
        knapper.put("Madrid", Madrid);
        knapper.put("LosAngeles", LosAngeles);
        knapper.put("Toronto", Toronto);
        knapper.put("Rio", Rio);
        knapper.put("Reykjavik", Reykjavik);
        knapper.put("Moscow", Moscow);
        knapper.put("BuenosAires", BuenosAires);
        knapper.put("Beijing", Beijing);
        knapper.put("MexicoCity", MexicoCity);
        knapper.put("Bogota", Bogota);
        knapper.put("Sydney", Sydney);
    }

    @FXML
    private void nextJourney() { // blar til neste reise
        travelPlan.next();
        refresh(); // oppdater brukergrensesnitt
    }

    @FXML
    private void lastJourney() { // blar tilbake til forrige
        travelPlan.last();
        refresh(); // oppdater brukergrensesnitt
    }

    @FXML
    private void newJourney() {
        travelPlan.setSorted(btnSorted.isSelected()); // setter sorteringsveri, true/false
        travelPlan.addJourney(); // lager ny reise
        refresh(); // oppdater brukergrensesnitt
    }

    @FXML
    private void deleteJourney() {
        travelPlan.setSorted(btnSorted.isSelected());
        travelPlan.removeJourney();
        refresh(); // oppdater brukergrensesnitt
    }

    @FXML
    private void pinEvent(MouseEvent e) { // ved trykk på knapp
        travelPlan.manageDestination(((Ellipse) e.getSource()).getId(), knapper); // kjører pinPressed i journey plan, henter
                                                                           // objektet og legger inn id til knapp og
                                                                           // knappehashtable
        refresh(); // oppdater brukergrensesnitt
    }

    private void refresh() { // oppdater brukergrensesnittet
        travelPlan.sort(btnSorted.isSelected());
        write();
        buttonCheck();
        draw();
        if (autoToggle.isSelected()) { // hvis automatisk lagring er aktivert
            travelPlan.save(); // vil lagring skje automatisk
        }
    }

    private void buttonCheck() {
        Ellipse[] btnArray = { NewYork, Oslo, CapeTown, Mumbai, Paris, London, Madrid, LosAngeles, Toronto, Rio,
                Reykjavik, Moscow, BuenosAires, Beijing, MexicoCity, Bogota, Sydney };
        for (Ellipse k : btnArray) {
            if (travelPlan.getCurrentJourney().isIn(k.getId())) { // itererer over knapper inkludert i currentJourney
                                                                  // for å
                k.setFill(Color.GREEN); // markere de som er inkludert grønn
            } else {
                k.setFill(Color.RED); // resten er ubrukt, og røde
            }
        }
        if (travelPlan.getIndex(travelPlan.getCurrentJourney()) == 0) { // sjekker om indeks på currentJourney er første
                                                                        // element i reiseplan for å
            btnBack.setVisible(false); // fjerne 'back' på første journey
        } else {
            btnBack.setVisible(true); // vise ellers
        }

        if (travelPlan.getIndex(travelPlan.getCurrentJourney()) == travelPlan.getSize() - 1) { // sjekker om indeks på
                                                                                               // currentJourney er
                                                                                               // siste element i
                                                                                               // reiseplan for å
            btnNext.setVisible(false); // fjerne 'next' på siste journey
        } else {
            btnNext.setVisible(true); // vise ellers
        }
    }

    private void write() {
        // oppretter String
        String tekst = "Reise: " + (travelPlan.getIndex(travelPlan.getCurrentJourney()) + 1) + "\nByer:\n";
        // legger til alle byene etter rekkefølge
        for (int k = 0; k < travelPlan.getCurrentJourney().getSize(); k++) {
            tekst += (k + 1) + ": " + travelPlan.getCurrentJourney().getCity(k) + "\n";
        }
        // sjekker at reisen har en distanse (slipper å regne avstand hvis den uansett
        // er 0)
        if (travelPlan.getCurrentJourney().getSize() > 1) { // hvis to eller flere byer
            tekst += "Total Reiseavstand " + travelPlan.getCurrentJourney().distance() + "Km";
        } else {// hvis mindre enn to byer
            tekst += "Total Reiseavstand " + 0 + "Km";
        }
        // setter label med String
        txtCurrentJourney.setText(tekst);
        txtIndex.setText(Integer.toString(travelPlan.getIndex(travelPlan.getCurrentJourney()) + 1));
    }

    private void draw() {
        // lager GraphicContext
        GraphicsContext mal = panel.getGraphicsContext2D();
        // clearer et rektangel like stort som hele canvas
        mal.clearRect(0, 0, panel.getWidth(), panel.getHeight());
        // starter path
        mal.beginPath();
        // seter linje
        mal.setLineWidth(3);
        mal.setStroke(Color.RED);
        // skriver linge for alle byer i den viste reisen
        for (int k = 0; k < travelPlan.getCurrentJourney().getSize() - 1; k++) {
            mal.moveTo(toEllipse(travelPlan.getCurrentJourney().getCity(k)).getLayoutX() - 6, // for å flytte penselen
                                                                                              // til første destinasjon,
                                                                                              // ikke (0,0)
                    toEllipse(travelPlan.getCurrentJourney().getCity(k)).getLayoutY() + 25);

            mal.lineTo(toEllipse(travelPlan.getCurrentJourney().getCity(k + 1)).getLayoutX() - 6, // tegne fra forrige
                                                                                                  // til neste
                                                                                                  // destinasjon
                    toEllipse(travelPlan.getCurrentJourney().getCity(k + 1)).getLayoutY() + 25);

        }
        mal.stroke();
    }

    // tar inn string og returnerer tilsvarende ellipse
    // kunne brukt intern Journey toEllipse-funksjon, men da hadde koden over blitt
    // veldig
    // rotete
    private Ellipse toEllipse(String navn) throws NullPointerException {
        if (knapper.get(navn) != null) {
            return (knapper.get(navn));
        } else {
            throw new IllegalArgumentException("den knappen finnes ikke (controller toEllipse)");
        }
    }

    @FXML
    private void save() { // binder 'save' knapp med save funksjon
        travelPlan.save();
    }

    @FXML
    private void load() { // binder 'load' knapp med initialize funksjon
        initialize();
        refresh();
    }

    @FXML
    private void btnSorterPressed() {// sorterer reiseplanen utifra om den skal være sortert eller ikke
        refresh();
    }
}
