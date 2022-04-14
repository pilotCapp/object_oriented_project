package objprosjekt;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Group;
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
    private Label txtCurrentJourney;
    @FXML
    private ToggleButton autoToggle, btnSorted;

    private JourneyPlan travelPlan;
    private Hashtable<String, Ellipse> knapper;

    @FXML
    public void initialize() {
        openHash();
        travelPlan = new JourneyPlan(knapper);
        travelPlan.setSorted(btnSorted.isSelected());
        write();
        pinCheck();
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
    private void nextJourney() {
        travelPlan.next();
        refresh();
    }

    @FXML
    private void lastJourney() {
        travelPlan.last();
        refresh();
    }

    @FXML
    private void newJourney() {
        travelPlan.setSorted(btnSorted.isSelected());
        travelPlan.addJourney();
        refresh();
    }

    @FXML
    private void deleteJourney() {
        travelPlan.setSorted(btnSorted.isSelected());
        travelPlan.removeJourney();
        refresh();
    }

    @FXML
    private void pinEvent(MouseEvent e) {
        travelPlan.pinPressed(((Ellipse) e.getSource()).getId(), knapper);
        refresh();
    }

    private void refresh() {
        write();
        pinCheck();
        draw();
        if (autoToggle.isSelected()) {
            travelPlan.save();
        }
    }

    private void pinCheck() {
        Ellipse[] btnArray = { NewYork, Oslo, CapeTown, Mumbai, Paris, London, Madrid, LosAngeles, Toronto, Rio,
                Reykjavik, Moscow, BuenosAires, Beijing, MexicoCity, Bogota, Sydney };
        for (Ellipse k : btnArray) {
            if (travelPlan.getCurrentJourney().isIn(k.getId())) {
                k.setFill(Color.GREEN);
            } else {
                k.setFill(Color.RED);
            }
        }
        if (travelPlan.getIndex(travelPlan.getCurrentJourney()) == 0) {
            btnBack.setVisible(false);
        } else {
            btnBack.setVisible(true);
        }

        if (travelPlan.getIndex(travelPlan.getCurrentJourney()) == travelPlan.getSize() - 1) {
            btnNext.setVisible(false);
        } else {
            btnNext.setVisible(true);
        }
    }

    private void write() {
        // oppretter String
        String tekst = "Reise: " + (travelPlan.getIndex(travelPlan.getCurrentJourney()) + 1) + "\nByer:\n";
        // legger til alle byene etter rekkefølge
        for (int k = 0; k < travelPlan.getCurrentJourney().getSize(); k++) {
            tekst += (k + 1) + ": " + travelPlan.getCurrentJourney().getCity(k) + "\n";
        }
        // sjekker at reisen har en distanse
        if (travelPlan.getCurrentJourney().getSize() > 1) {
            tekst += "Total Reiseavstand " + travelPlan.getCurrentJourney().distance(knapper) + "Km";
        } else {
            tekst += "Total Reiseavstand " + 0 + "Km";
        }
        // setter label med String
        txtCurrentJourney.setText(tekst);
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
            mal.moveTo(toEllipse(travelPlan.getCurrentJourney().getCity(k)).getLayoutX() - 6,
                    toEllipse(travelPlan.getCurrentJourney().getCity(k)).getLayoutY() + 25);
            mal.lineTo(toEllipse(travelPlan.getCurrentJourney().getCity(k + 1)).getLayoutX() - 6,
                    toEllipse(travelPlan.getCurrentJourney().getCity(k + 1)).getLayoutY() + 25);

        }
        mal.stroke();
    }

    // tar inn string og returnerer tilsvarende ellipse
    private Ellipse toEllipse(String navn) {
        if (knapper.get(navn) != null) {
            return (knapper.get(navn));
        } else {
            throw new IllegalArgumentException("den knappen finnes ikke (controller toEllipse)");
        }
    }

    @FXML
    private void save() {
        travelPlan.save();
    }

    @FXML
    private void load() {
        initialize();
    }

    @FXML
    private void btnSorterPressed() {
        travelPlan.sort(btnSorted.isSelected());
        refresh();
    }
}
