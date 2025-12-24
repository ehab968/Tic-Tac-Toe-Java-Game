/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.GameData;
import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.RequestType;
import com.iti.group3.tic_tac_toe_shared.Response;
import static com.iti.group3.tic_tac_toe_shared.ResponseType.INVITE_ACCEPTED;
import static com.iti.group3.tic_tac_toe_shared.ResponseType.INVITE_DROPPED;
import static com.iti.group3.tic_tac_toe_shared.ResponseType.INVITE_REJECTED;
import static com.iti.group3.tic_tac_toe_shared.ResponseType.REQUEST_GAME;
import static com.iti.group3.tic_tac_toe_shared.ResponseType.SERVER_FAILURE;
import static com.iti.group3.tic_tac_toe_shared.ResponseType.START_GAME;
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
public class ClientStreamSocket {

    static private Socket socket;
    static private ObjectInputStream in;
    static private ObjectOutputStream out;
    static public UserData user;

    static private void connectToServer() throws UnknownHostException, UnknownHostException, IOException {
        if (socket == null) {
            socket = new Socket(InetAddress.getLocalHost(), 5006);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            setUserInServer();
        }
    }

    private static void setUserInServer() {
        int retryCount = 5;
        for (int i = 0; i < retryCount; i++) {
            try {
                write(new Request(RequestType.SET_USER, user));
                return;
            } catch (IOException ex) {
                System.getLogger(ClientStreamSocket.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (ClassNotFoundException ex) {
                System.getLogger(ClientStreamSocket.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            System.out.println("setUserInServer count= " + i);
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

        } finally {
            out = null;
            in = null;
            socket = null;
            user = null;
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

    public static boolean isInGame = false;

    static public void startStream() {
        System.out.println("Stream Socket started");

        new Thread(() -> {
            try {
                connectToServer();
                while (true) {
                    Response response = (Response) in.readObject();
                    System.out.println("Client Received response " + response);

                    switch (response.getMessage()) {

                        case REQUEST_GAME:
                            if (!isInGame) {
                                GameRequest.RequestReceived(response);
                            }
                            break;

                        case START_GAME:
                            if (!isInGame) {
                                GameRequest.startOnlineGame((GameData) response.getData());
                                isInGame = true;
                            }
                            break;

                        case INVITE_ACCEPTED:
                        case INVITE_REJECTED:
                        case INVITE_DROPPED:
                        case SERVER_FAILURE:
                            GameRequest.handleInviteResponse(response);
                            break;

                        case GAME_OVER:
                            isInGame = false;
                            break;
                    }

                }
            } catch (IOException ex) {
                System.getLogger(ClientStreamSocket.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (ClassNotFoundException ex) {
                System.getLogger(ClientStreamSocket.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            closeConnection();

        }).start();

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
