/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui.interfaces;

/**
 *
 * @author mahmo
 */
@FunctionalInterface
public interface OnUserChanged {
    void handle(char currentPlayer);
}

