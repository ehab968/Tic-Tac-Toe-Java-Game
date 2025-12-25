/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.UserData;
import javafx.stage.Stage;

/**
 *
 * @author Ahmed Sayed
 */
public class PendingInvite {

    private static UserData sender;
    private static Stage waitingStage;

    public static void setSender(UserData user) {
        sender = user;
    }

    public static UserData getSender() {
        return sender;
    }

    public static void clear() {
        sender = null;
    }

}
