/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.GameData;
import com.iti.group3.tic_tac_toe_shared.GameMove;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mahmo
 */
public class GameRecorder {

    GameData gameData;
    private static final String RECORD_DIR = "game_records";

    public GameRecorder(GameData gameData) {
        this.gameData = gameData;
    }

    public ArrayList<GameMove> playRecord(String path) {
        ArrayList<GameMove> loadedMoves = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("game_records/"+path+".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Logic based on your format: Order:Cell-Char
                // Example: 1:4-X
                String[] parts = line.split("[:\\-]"); // Splits by either ':' or '-'
                int order = Integer.parseInt(parts[0]);
                int cellId = Integer.parseInt(parts[1]);
                char player = parts[2].charAt(0);

                // Create move object (assuming movePlayer can be null for replay)
                loadedMoves.add(new GameMove(cellId, player, order, null));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return loadedMoves;
    }

    public int saveRecord(ArrayList<GameMove> moves) {
        // 1. Create the directory if it doesn't exist
        File directory = new File(RECORD_DIR);
        if (!directory.exists()) {
            directory.mkdir();
        }

        // 2. Generate a unique filename using the current date/time
        gameData.setDate(LocalDateTime.now());
        String timestamp = gameData.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String fileName = RECORD_DIR + "/game_" + gameData.getId() + "_" + timestamp + ".txt";

        // 3. Write moves to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (GameMove move : moves) {
                // Format: cellId:playerChar (e.g., "4:X")
                writer.write(move.getMoveOrder() + ":" + move.getCellId() + "-" + move.getCharacter());
                writer.newLine();
            }
            System.out.println("Game saved successfully: " + fileName);
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
