/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.handle;

import java.util.ArrayList;
import javafx.scene.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Pubudu
 */
public class GameCreator {

    FileHandler fileHandler;
    NodeList nodeList;

    public GameCreator() {
        fileHandler = new FileHandler();
        nodeList = null;
    }

    public static void main(String[] args) {

        GameCreator gc = new GameCreator();
        gc.readGameFile("game_001");
    }

    public Node getStory(int storyNumber) {
        //  System.out.println(nodeList.item(3).getChildNodes().item(1).getTextContent());
        //  System.out.println(nodeList.item(3).getChildNodes().item(0).getTextContent());
        return (Node) nodeList.item(storyNumber);
    }

    public void readGameFile(String gameName) {
        Element element = fileHandler.readFromGameFile(gameName);
        nodeList = element.getElementsByTagName("Stories").item(0).getChildNodes();
        int tt=nodeList.getLength();
        System.out.println("fdsfa = "+tt);
        System.out.println("done");
    }

}
