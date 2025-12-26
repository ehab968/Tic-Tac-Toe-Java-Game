/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.GameData;
import com.iti.group3.tic_tac_toe_shared.GameMove;
import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.RequestType;
import com.iti.group3.tic_tac_toe_shared.Response;
import static com.iti.group3.tic_tac_toe_shared.ResponseType.GAME_OVER;
import static com.iti.group3.tic_tac_toe_shared.ResponseType.INVITE_ACCEPTED;
import static com.iti.group3.tic_tac_toe_shared.ResponseType.INVITE_DROPPED;
import static com.iti.group3.tic_tac_toe_shared.ResponseType.INVITE_REJECTED;
import static com.iti.group3.tic_tac_toe_shared.ResponseType.REQUEST_GAME;
import static com.iti.group3.tic_tac_toe_shared.ResponseType.SERVER_FAILURE;
import static com.iti.group3.tic_tac_toe_shared.ResponseType.SERVER_RESTART_GAME;
import static com.iti.group3.tic_tac_toe_shared.ResponseType.START_GAME;
import com.iti.group3.tic_tac_toe_shared.UserData;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 *
 * @author mahmo
 */
public class ClientStreamSocket {

    static private Socket socket;
    static private ObjectInputStream in;
    static private ObjectOutputStream out;
    static public UserData user;
    public static GameData currentGame;
    static boolean isInGame = false;
    public static volatile boolean appRun = true;

    static private void connectToServer() throws UnknownHostException, UnknownHostException, IOException {
        if (socket == null) {
            socket = new Socket(InetAddress.getLocalHost(), 5006);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            setUserInServer();
        }
    }

    public static void stopStream() {
        appRun = false;
        closeConnection();
    }

    static public void startStream() {
        System.out.println("Stream Socket started");

        new Thread(() -> {
            try {
                connectToServer();
                while (appRun) {
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
                                PrimaryController.isOnline = true;
                                currentGame = (GameData) response.getData();
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
                        case Server_SENT_MOVE:
                            GameMove move = (GameMove) response.getData();
                            if (PrimaryController.getInstance() != null) {
                                System.out.println("move recieved");
                                Platform.runLater(() -> {
                                    PrimaryController.getInstance().getOnlineGameController().handleMove(move);
                                });
                            }
                            break;
                        case SERVER_RESTART_GAME:
                            if (PrimaryController.getInstance() != null) {
                                System.out.println("game restart");
                                Platform.runLater(() -> {
                                    PrimaryController.getInstance().getOnlineGameController().restartGame();
                                });
                            }
                            break;
                        case SERVER_END_GAME:
                            isInGame = false;
                            if (PrimaryController.getInstance() != null) {
                                System.out.println("game end");
                                Platform.runLater(() -> {
                                    PrimaryController.getInstance().getOnlineGameController().exitGame();
                                });
                            }
                            break;
                        case SERVER_UPDATE_WINNER_SCORE:
                            int updatedScoreWinner = (int) response.getData();
                            Platform.runLater(() -> {
                                if (currentGame.winner.getUserName().equals(ClientStreamSocket.user.getUserName())) {
                                    user.setScore(updatedScoreWinner);
                                    PrimaryController.getInstance().updateScoreUI(
                                            user.getScore(),
                                            PrimaryController.getInstance().getOnlineGameController().getOpponentData().getScore()
                                    );
                                } else {
                                    PrimaryController.getInstance().getOnlineGameController().opponent.setScore(updatedScoreWinner);
                                    PrimaryController.getInstance().updateScoreUI(
                                            user.getScore(),
                                            PrimaryController.getInstance().getOnlineGameController().getOpponentData().getScore()
                                    );
                                }
                            });
                            break;
                    }
                }
            } catch (EOFException | SocketException ex) {
                if (appRun) {
                    System.out.println("server disconnected");
                    handleServerDown();
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("server disconnected");
                handleServerDown();
            }

        }).start();

    }

    public static void handleServerDown() {
        appRun = false;
        isInGame = false;
        closeConnection();
        Platform.runLater(() -> {
            try {
                App.setRoot("home");
                showAlert("ServerDown", "Sorry, Server Closed", "try again at another time");
            } catch (IOException ex) {
                System.getLogger(ClientStreamSocket.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });

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

    private static void showAlert(String title, String headMsg, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headMsg);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
