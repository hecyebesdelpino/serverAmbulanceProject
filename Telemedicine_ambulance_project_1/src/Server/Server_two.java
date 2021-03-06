/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Server.ClientThread;
import Patient.Patient;
import Patient.Users;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Server_two implements Runnable {
    private String password;
    private ServerOnWindowController window;
    private int portNumber;
    private ServerSocket socket;
    private ArrayList<Patient> patients;
    private ArrayList<Users> users;
    private ArrayList<Socket> clients;
    private ArrayList<ClientThread> clientThreads;

    public Server_two(int portNumber, ServerOnWindowController window, String password) throws IOException {
        this.window=window;
        this.portNumber = portNumber;
        this.password=password;
        patients = new ArrayList<Patient>();
        clients = new ArrayList<Socket>();
        users= new ArrayList<Users>();
        clientThreads = new ArrayList<ClientThread>();
        socket = new ServerSocket(portNumber);

    }

    public ServerSocket getSocket(){
        return socket;
    }

    public String getPassword(){
        return password;
    }

    public ServerOnWindowController getWindow(){
        return window;
    }

    public ArrayList<Socket> getClients(){
        return clients;
    }
    
    public ArrayList<Users> getUsers(){
        return users;
    }
    
    public  ArrayList<ClientThread> getClientThreads(){
        return clientThreads;
    }
    public  ArrayList<Patient> getPatients(){
        return patients;
    }

    public void setPassword(String newPassword){
        this.password=newPassword;
    }

    public void setPatients(ArrayList<Patient> patients) {
        this.patients = patients;
    }
    
    public void setUsers(ArrayList<Users> u) {
        this.users = u;
    }

    public void deleteUser(Users user) throws IOException{
        this.users.remove(user);
        
        ObjectOutputStream output = null;
             
        output = new ObjectOutputStream(new FileOutputStream("./Files/users.txt"));
             
        for (Users u : users) {
            output.writeObject(u);
        }
             
        output.close();
        
    }
    
    public void addPatient(Patient patient) throws IOException{
        
        int size = patients.size();
            for (int i = 0; i < size; i++) {
                if (patient.getId().equalsIgnoreCase(patients.get(i).getId())) {
                    patients.remove(i);
                    break;
                }
            }
        this.patients.add(patient);
        
        ObjectOutputStream output = null;

        output = new ObjectOutputStream(new FileOutputStream("./Files/serverPatients.txt"));
          
        for (Patient p : patients) {
            output.writeObject(p);
        }
             
        output.close();
        
    }
    
    
    public void addUser(Users user) throws IOException{
        
        this.users.add(user);
        
        ObjectOutputStream output = null;
             
        output = new ObjectOutputStream(new FileOutputStream("./Files/users.txt"));
             
        for (Users u : users) {
            output.writeObject(u);
        }
             
        output.close();
        
    }


    public void run() {
        
        while (socket.isClosed()==false) {
            Socket clientSocket=null;
            try{
                clientSocket = socket.accept();
            }catch(Exception e){
                break;
            }

            clients.add(clientSocket);

            ClientThread clientThreadHolderClass = new ClientThread(
                                        clientSocket, this, window);
            Thread clientThread = new Thread(clientThreadHolderClass);
            clientThreads.add(clientThreadHolderClass);
            clientThread.start();
        }
           
    }

    public void close(){
        for(ClientThread client: clientThreads){
            client.releaseResources();
        }
        
        for(Socket sock: clients){
            try {
                sock.close();
            } catch (IOException ex) {
                Logger.getLogger(Server_two.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server_two.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void clientDisconnected(Socket socket, ClientThread client) {
        clients.remove(socket);
        clientThreads.remove(client);
    }

}
