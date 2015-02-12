/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sifeb.ve.controller;

import com.sifeb.ve.handle.BlockCreator;
import jssc.SerialPort;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pubudu
 */
public class ComPortControllerTest {
    
    public ComPortControllerTest() {
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
     * Test of listComPorts method, of class ComPortController.
     */
    @Test
    public void testListComPorts() {
        System.out.println("listComPorts");
        ComPortController.listComPorts();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of openPort method, of class ComPortController.
     */
    @Test
    public void testOpenPort_0args() {
        System.out.println("openPort");
        ComPortController.openPort();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of closePort method, of class ComPortController.
     */
    @Test
    public void testClosePort() {
        System.out.println("closePort");
        ComPortController.closePort();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEventListener method, of class ComPortController.
     */
    @Test
    public void testSetEventListener() {
        System.out.println("setEventListener");
        ComPortController.setEventListener();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeEventListener method, of class ComPortController.
     */
    @Test
    public void testRemoveEventListener() {
        System.out.println("removeEventListener");
        ComPortController.removeEventListener();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of read method, of class ComPortController.
     */
    @Test
    public void testRead() {
        System.out.println("read");
        int bytes = 0;
        byte[] expResult = null;
        byte[] result = ComPortController.read(bytes);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeComPort method, of class ComPortController.
     */
    @Test
    public void testWriteComPort() {
        System.out.println("writeComPort");
        String msg = "";
        ComPortController.writeComPort(msg);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeProgram method, of class ComPortController.
     */
    @Test
    public void testWriteProgram() {
        System.out.println("writeProgram");
        byte[] byteArray = null;
        ComPortController.writeProgram(byteArray);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
