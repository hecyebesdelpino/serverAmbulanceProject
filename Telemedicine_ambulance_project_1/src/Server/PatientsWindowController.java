/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Patient.Ambulance;
import Patient.Patient;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author AdriCortellucci
 */
public class PatientsWindowController implements Initializable {

    public ObservableList <Patient> patients_= FXCollections.observableArrayList();
    
    @FXML private TableView <Patient> tableView;
    @FXML private TableColumn<Patient, Date> date;
    @FXML private TableColumn<Patient, String> patientID;
    @FXML private TableColumn<Patient, String> patientName;
    
    @FXML private Button detailedPersonViewButton;
    
    public ArrayList<Patient> patients= new ArrayList<Patient>();
    
    public Server_two server;
    
    public void initData(Server_two server){
        this.server=server;
        patients= this.server.getPatients();
        fillPatients();

    }
    
    public void changeSceneToDetailedPersonView(ActionEvent event) throws IOException
    {
        
        StackPane secondaryLayout = new StackPane();
          
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ShowPatient.fxml"));
        Parent tableViewParent = loader.load();
        
        Scene secondScene = new Scene(tableViewParent);
           
        ShowPatientController controller = loader.getController();
        controller.initData(tableView.getSelectionModel().getSelectedItem());
        
        Stage secondStage = new Stage();
        secondStage.setScene(secondScene);
            
        secondStage.show();
    }
    
    public void userClickedOnTable()
    {
        this.detailedPersonViewButton.setDisable(false);
    }
    
    public void fillPatients() {
        patients_=FXCollections.observableArrayList(server.getPatients());
        tableView.setItems(patients_);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
         //set up the columns in the table
        patientName.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
        patientID.setCellValueFactory(new PropertyValueFactory<Patient, String>("id"));
        date.setCellValueFactory(new PropertyValueFactory<Patient, Date>("date"));
        
        this.detailedPersonViewButton.setDisable(true);
        
    }  
    
}