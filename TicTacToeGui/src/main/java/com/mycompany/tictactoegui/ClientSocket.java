/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.Response;
import com.iti.group3.tic_tac_toe_shared.UserData;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author mahmo
 */
public class ClientSocket extends Thread {

    static private Socket socket;
    static private ObjectInputStream in;
    static private ObjectOutputStream out;
    static public UserData user;
    public static ServerListener serverListener;

    static private void connectToServer() throws UnknownHostException, UnknownHostException, IOException {
        if (socket == null) {
            socket = new Socket(InetAddress.getLocalHost(), 5005);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
        }
    }

    private static void closeConnection() {
        try {
            System.out.println("Closing Connection");
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException ex) {
            out = null;
            in = null;
            socket = null;
            user=null;
        }
    }

    static public Response read() throws IOException, ClassNotFoundException {
        try {
            Response response = null;
            connectToServer();
            response = (Response) in.readObject();
            return response;
        } catch (SocketException e) {
            closeConnection();
            throw e;
        }
    }

    static public void write(Request request) throws IOException, ClassNotFoundException {
        try {
            connectToServer();
            out.writeObject(request);
            out.flush();
        } catch (SocketException e) {
            closeConnection();
            throw e;
        }
    }

}
