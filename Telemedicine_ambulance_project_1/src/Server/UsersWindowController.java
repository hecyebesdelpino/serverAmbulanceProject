/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Patient.Ambulance;
import Patient.Users;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
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
public class UsersWindowController implements Initializable {

    private ObservableList <Users> users=FXCollections.observableArrayList();
    
    @FXML private TableView <Users> tableView;
    @FXML private TableColumn<Users, String> user;
    
    @FXML private Button delete;
    
    //private ArrayList<Users> usersList= new ArrayList<Users>();
    
    private Server_two server;
    
    public void initData(Server_two server){
        this.server=server;
        //usersList= this.server.getUsers();
        fillUsers();

    }
    
    public void changeSceneToNewUser(ActionEvent event) throws IOException
    {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewUser.fxml"));
        Parent parent = loader.load();
        
        Scene secondScene = new Scene(parent);
           
        NewUserController controller = loader.getController();
        controller.initData(this);
        
        Stage secondStage = new Stage();
        secondStage.setScene(secondScene);
            
        secondStage.show();
        
    }
    
    public void deleteUser(ActionEvent event) {
        try {
            Users delete=tableView.getSelectionModel().getSelectedItem();
            server.deleteUser(delete);
            users.remove(delete);
        } catch (IOException ex) {
            Logger.getLogger(UsersWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public void addUser(Users u) {
        try {
            server.addUser(u);
        } catch (IOException ex) {
            Logger.getLogger(UsersWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        users.add(u);
    }
    
    
    public void userClickedOnTable()
    {
        this.delete.setDisable(false);
    }
    
    public void fillUsers() {
        users=FXCollections.observableArrayList(server.getUsers());
        tableView.setItems(users);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        //set up the columns in the table
        user.setCellValueFactory(new PropertyValueFactory<Users, String>("user"));
       
        this.delete.setDisable(true);
        
        
        
    }
}
