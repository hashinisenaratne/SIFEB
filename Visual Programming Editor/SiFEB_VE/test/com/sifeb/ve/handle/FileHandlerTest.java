/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.handle;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;

/**
 *
 * @author Pubudu
 */
public class FileHandlerTest {

    public FileHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of readFromGameFile method, of class FileHandler.
     */
    @Test
    public void testReadFromGameFile() {
        System.out.println("readFromGameFile");
        String fileName = "game_1_001";
        FileHandler instance = new FileHandler();
        String expResult = "Game";
        String result = instance.readFromGameFile(fileName).getNodeName();
        assertEquals(expResult, result);

    }

    /**
     * Test of readFromEditorFile method, of class FileHandler.
     */
    @Test
    public void testReadFromEditorFile() {
        System.out.println("readFromEditorFile");

        String filePath1 = "C:\\FYP\\SIFEB\\Visual Programming Editor\\SiFEB_VE\\test\\TestFiles\\test1.sifeb";
        String filePath2 = "C:\\FYP\\SIFEB\\Visual Programming Editor\\SiFEB_VE\\test\\TestFiles\\test2.sifeb";
        FileHandler instance = new FileHandler();
        String expResult1 = "MainEditor";
        Element expResult2 = null;
        String result1 = instance.readFromEditorFile(filePath1).getNodeName();
        Element result2 = instance.readFromEditorFile(filePath2);
        assertEquals(expResult1, result1);
        assertEquals(expResult2, result2);

    }

}
