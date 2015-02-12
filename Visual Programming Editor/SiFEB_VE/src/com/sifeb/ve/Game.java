/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

/**
 * This class is used to handle game files
 * @author Hashini Senaratne
 */
public class Game {

    private final String gameID;
    private final String gameFile;

    public Game(String id, String file) {
        this.gameID = id;
        this.gameFile = file;
    }

    // returns the game id
    public String getGameID() {
        return gameID;
    }

    // returns the game file
    public String getGameFile() {
        return gameFile;
    }
}
