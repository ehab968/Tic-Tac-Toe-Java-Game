/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author Shahd
 */
public class SingleMode {

    private char[] charMatrix;
    private StackPane[] stackCells;
    public GameController controller;

    public SingleMode(char[] charMatrix, StackPane[] stackCells, GameController controller) {
        this.charMatrix = charMatrix;
        this.stackCells = stackCells;
        this.controller = controller;
    }

    private void computerMove(int index) {
        for (StackPane cell : stackCells) {
            cell.setDisable(true);
        }
        PauseTransition pause = new PauseTransition(Duration.seconds(.4));

        pause.setOnFinished((ActionEvent event) -> {
            StackPane cell = stackCells[index];

            controller.addShapeToCell(cell, 'O');
            charMatrix[index] = 'O';
            cell.setDisable(true);

            controller.checkWin();
            if (!controller.getIsWin()) {
                controller.changePlayer();
            }

            for (int i = 0; i < stackCells.length; i++) {
                if (charMatrix[i] == '\0') {
                    stackCells[i].setDisable(false);
                }
            }
        });
        pause.play();

    }

    /**
     * *********************Easy************************
     */
    public void computerRoleEasy() {
        List<Integer> emptyCells = new ArrayList<>();
        for (int i = 0; i < charMatrix.length; i++) {
            if (charMatrix[i] == '\0') {
                emptyCells.add(i);
            }
        }
        if (!emptyCells.isEmpty()) {
            Random rand = new Random();
            int randomNumber = rand.nextInt(emptyCells.size());
            int cellNumber = emptyCells.get(randomNumber);

            computerMove(cellNumber);
        }

    }

    private int findSuitableMove(char player) {
        int[][] wins = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
        };

        for (int[] line : wins) {
            int countPlayer = 0;
            int emptyIndex = -1;

            for (int i : line) {
                if (charMatrix[i] == player) {
                    countPlayer++;
                } else if (charMatrix[i] == '\0') {
                    emptyIndex = i;
                }
            }

            if (countPlayer == 2 && emptyIndex != -1) {
                return emptyIndex;
            }
        }
        return -1;
    }

    /**
     * *********************Meduim************************
     */
    public void computerRoleMeduim() {
        List<Integer> emptyCells = new ArrayList<>();
        for (int i = 0; i < charMatrix.length; i++) {
            if (charMatrix[i] == '\0') {
                emptyCells.add(i);
            }
        }
        if (!emptyCells.isEmpty()) {

            int move = findSuitableMove('O');
            if (move != -1) {
                computerMove(move);
                return;
            }

            move = findSuitableMove('X');
            if (move != -1) {
                computerMove(move);
                return;
            }
            int[][] winPatterns = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
            };

            if (charMatrix[4] == '\0') {
                computerMove(4);
                return;
            }
            computerRoleEasy();

        }
    }

    /**
     * *********************hard************************
     */
    private int evaluateBoard(char[] board) {
        int[][] wins = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
        };
        for (int[] line : wins) {
            if (board[line[0]] != '\0' && board[line[0]] == board[line[1]] && board[line[1]] == board[line[2]]) {
                return (board[line[0]] == 'O') ? 1 : -1;
            }
        }
        return 0;
    }

    private int minimax(char[] board, boolean isMax) {
        int score = evaluateBoard(board);
        if (score == 1 || score == -1) {
            return score;
        }

        boolean movesLeft = false;
        for (char c : board) {
            if (c == '\0') {
                movesLeft = true;
                break;
            }
        }
        if (!movesLeft) {
            return 0;
        }

        if (isMax) {
            int best = -2;
            for (int i = 0; i < board.length; i++) {
                if (board[i] == '\0') {
                    board[i] = 'O';
                    best = Math.max(best, minimax(board, false));
                    board[i] = '\0';
                }
            }
            return best;
        } else {
            int best = 2;
            for (int i = 0; i < board.length; i++) {
                if (board[i] == '\0') {
                    board[i] = 'X';
                    best = Math.min(best, minimax(board, true));
                    board[i] = '\0';
                }
            }
            return best;
        }
    }

    private int findBestMoveMinimax() {
        int bestVal = -2;
        int bestMove = -1;
        for (int i = 0; i < charMatrix.length; i++) {
            if (charMatrix[i] == '\0') {
                charMatrix[i] = 'O';
                int moveVal = minimax(charMatrix, false);
                charMatrix[i] = '\0';
                if (moveVal > bestVal) {
                    bestVal = moveVal;
                    bestMove = i;
                }
            }
        }
        return bestMove;
    }

    public void computerRoleHard() {
        int bestMove = findBestMoveMinimax();
        if (bestMove != -1) {
            computerMove(bestMove);
        }
    }

}
