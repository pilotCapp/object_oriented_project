package prosjekt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class JourneyPlanController {
    private Journey currentJourney=new Journey();

    @FXML
    private TextField txtCities,txtIndex;
    @FXML 
    private Label txtJourney=new Label();

   
   private void addJourney(){
        currentJourney=new Journey();
   }
    
    @FXML
    private void addCity(){currentJourney.cities.add(txtCities.getText());refresh();}
    @FXML
    private void removeCity(){currentJourney.cities.remove(txtCities.getText());refresh();}

    private void refresh(){
        txtJourney.setText("test");
        System.out.println("done");
    }
}
