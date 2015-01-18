/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import java.util.ArrayList;

/**
 *
 * @author Hashini
 */
public class GameList {

    private final int level;
    private final ArrayList<Game> games;

    public GameList(int level) {
        this.level = level;
        this.games = new ArrayList();
    }

    public void addGame(String id, String file) {
        games.add(new Game(id, file));
    }

    public ArrayList<Game> getGames() {
        return games;
    }
}
