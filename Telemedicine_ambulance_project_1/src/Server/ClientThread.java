/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Patient.Ambulance;
import Patient.Patient;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread implements Runnable {

    private ServerOnWindowController window;
    private Patient patient;
    private Socket clientSocket;
    private Server_two baseServer;
    private ObjectInputStream fromClient;
    private ObjectOutputStream toClient;
    private String clientID;
    private Ambulance clientAmbulance;
    private String received = "";

    private SocketAddress address;
    private int port;

    public ClientThread(Socket clientSocket, Server_two baseServer, ServerOnWindowController window) {
        this.clientSocket = clientSocket;
        this.address = clientSocket.getRemoteSocketAddress();
        this.port = clientSocket.getPort();
        this.baseServer = baseServer;
        this.window = window;
        try {
            toClient= new ObjectOutputStream(clientSocket.getOutputStream());
            fromClient = new ObjectInputStream(clientSocket.getInputStream());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
            try {
                toClient.writeObject(baseServer.getUsers());
            } catch (IOException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                received = (String) fromClient.readObject();
            } catch (IOException ex) {
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            if (received.toLowerCase().contains("logout") || received == "-1") {
                baseServer.clientDisconnected(clientSocket, this);
                releaseResources();
                
            }else{
                try {
                    Object tmp;
                    tmp = fromClient.readObject();
                    patient = (Patient) tmp;
                    clientAmbulance = patient.getAmbulance();
                    clientID = patient.getId();

                    window.chatWindow.appendText(patient.getAmbulance() + ":  connected \n");
                    
                    baseServer.addPatient(patient);
                    
                    while (true) {
                        toClient.flush();
                        toClient.writeByte(1);
                        toClient.flush();

                        received = (String) fromClient.readObject();
                        if (received.equals("check")) {
                            continue;
                        }
                        if (received.toLowerCase().contains("logout") || received == "-1") {
                            window.chatWindow.appendText(patient.getAmbulance() + ":  disconnected \n");
                            baseServer.clientDisconnected(clientSocket, this);
                            releaseResources();
                            break;
                        }
                        window.chatWindow.appendText(patient.getAmbulance() + ":  " + received + "\n");

                    }
            } catch (IOException ex) {
                try{
                window.chatWindow.appendText(patient.getAmbulance() + ":  disconnected \n");
                baseServer.clientDisconnected(clientSocket, this);
                }catch(Exception e){
                }
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }}

    public String getClientID() {
        return this.clientID;
    }

    public Ambulance getClientAmbulance() {
        return this.clientAmbulance;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public Patient getPatient() {
        return patient;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public ObjectInputStream getFromClient() {
        return fromClient;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void releaseResources() {
        try {
            fromClient.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            toClient.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String toString() {
        return "ClientThread{" + "clientID=" + clientID + '}';
    }

}
