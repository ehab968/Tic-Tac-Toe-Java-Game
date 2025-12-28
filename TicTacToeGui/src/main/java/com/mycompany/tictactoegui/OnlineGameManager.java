package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.RequestType;
import com.iti.group3.tic_tac_toe_shared.GameMove;
import com.iti.group3.tic_tac_toe_shared.UserData;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class OnlineGameManager extends GameManager {

    private final char mySymbol;
    private final UserData user;
    public UserData opponent;
    boolean isMyTurn = false;

    OnlineGameManager(Pane gamePane, GridPane gridPane) {
        this.gridPane = gridPane;
        this.gamePane = gamePane;
        gameMoves = new ArrayList(0);

        game = ClientStreamSocket.currentGame;
        user = ClientStreamSocket.user;
        if (game.playerX.getUserName().equals(user.getUserName())) {
            opponent = game.playerO;
            mySymbol = 'X';
            isMyTurn = true;
        } else {
            opponent = game.playerX;
            mySymbol = 'O';
            isMyTurn = false;
        }
        initiateGrid();
    }

    @Override
    protected void initiateGrid() {
        super.initiateGrid();
        for (StackPane cell : stackCells) {
            cell.setOnMouseClicked((MouseEvent e) -> {
                handleCellPress(cell);
            });
        }
    }

    @Override
    public void handleCellPress(StackPane cell) {
        if (!isMyTurn || isWin || cell.isDisable()) {
            SoundPlayer.playbuttonClick();
            return;
        }
        doClickSound(mySymbol);
        try {
            int cellId = Integer.parseInt(cell.getId());
            GameMove move = new GameMove(cellId, mySymbol);
            ClientStreamSocket.write(new Request(RequestType.MOVE, move));
        } catch (Exception ex) {
            System.getLogger(GameManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void handleMove(GameMove move) {
        int cellId = move.getCellId();
        char player = move.getCharacter();
        currentPlayer = player;
        StackPane cell = stackCells[cellId];
        super.addShapeToCell(cell, player);
        charMatrix[cellId] = player;
        cell.setDisable(true);
        checkWin();
        if (!isWin) {
            isMyTurn = (player != mySymbol);
            if (onUserChanged != null) {
                onUserChanged.handle(player);
            }
        }
    }

    public UserData getUserData() {
        return user;
    }

    public UserData getOpponentData() {
        return opponent;
    }

    public char getMySymbol() {
        return mySymbol;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    @Override
    public void restartGame() {
        currentPlayer = '\0';
        isMyTurn = mySymbol == 'X';

        super.restartGame();
    }

    public boolean allCellsFull() {
        return actionOrder >= 9;
    }

    @Override
    public void exitGame() {
        try {
            if (WinDialogController.instance != null) {
                WinDialogController.instance.close();
            }
            App.setRoot("onLineUsers");
        } catch (IOException ex) {
            System.getLogger(PrimaryController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @Override
    public void onUserWon(int[] winningLine) {
        isWin = true;
        drawWinningLine(winningLine);

        boolean iWon = (currentPlayer == mySymbol);

        if (iWon) {
            game.setWinner(user);
            try {
                ClientStreamSocket.write(new Request(RequestType.UPDATE_SCORE, game.winner));
            } catch (IOException | ClassNotFoundException ex) {
                System.getLogger(OnlineGameManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        } else {
            game.setWinner(opponent);
        }
        showWinningDialog(game);
        if (onUserWinning != null) {
            onUserWinning.handle(currentPlayer);
        }
    }
}
