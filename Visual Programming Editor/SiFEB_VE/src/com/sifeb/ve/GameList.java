/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import java.util.ArrayList;

/**
 * This class is used to handle the game list in the system
 * @author Hashini Senaratne
 */
public class GameList {

    private final int level;
    private final ArrayList<Game> games;

    public GameList(int level) {
        this.level = level;
        this.games = new ArrayList();
    }

    // for adding a game
    public void addGame(String id, String file) {
        games.add(new Game(id, file));
    }

    // return the game list
    public ArrayList<Game> getGames() {
        return games;
    }
}
