/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Pubudu
 */
public class DeviceTest {

    static Device dev;

    public DeviceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
//        try {
//            System.out.printf("About to launch FX App\n");
//            Thread t = new Thread("JavaFX Init Thread") {
//                public void run() {
//                    Application.launch(MainApp.class, new String[0]);
//                }
//            };
//            t.setDaemon(true);
//            t.start();
//            System.out.printf("FX App thread started\n");
//            Thread.sleep(500);
//            Map<Locale, String> actNames = new HashMap<>();
//            actNames.put(new Locale("en_US"), "Wheels");
//            dev = new Device("id1", actNames, 10, "actionC", "dev_1");
//        } catch (InterruptedException ex) {
//            Logger.getLogger(DeviceTest.class.getName()).log(Level.SEVERE, null, ex);
//        }

        Map<Locale, String> actNames = new HashMap<>();
        actNames.put(new Locale("en_US"), "Wheels");
        dev = new Device("id1", actNames, 10, "actionC", "dev_1");
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
     * Test of getDeviceID method, of class Device.
     */
    @Test
    public void testGetDeviceID() {
        System.out.println("getDeviceID");

        String expResult = "id1";
        String result = dev.getDeviceID();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //  fail("The test case is a prototype.");
    }

    /**
     * Test of getAddress method, of class Device.
     */
    @Test
    public void testGetAddress() {
        System.out.println("getAddress");
        Device instance = null;
        int expResult = 0;
        int result = instance.getAddress();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getType method, of class Device.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        Device instance = null;
        String expResult = "";
        String result = instance.getType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getImage method, of class Device.
     */
    @Test
    public void testGetImage() {
        System.out.println("getImage");
        Device instance = null;
        Image expResult = null;
        Image result = instance.getImage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCapabilities method, of class Device.
     */
    @Test
    public void testGetCapabilities() {
        System.out.println("getCapabilities");
        Device instance = null;
        ArrayList<Capability> expResult = null;
        ArrayList<Capability> result = instance.getCapabilities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addCapabilities method, of class Device.
     */
    @Test
    public void testAddCapabilities() {
        System.out.println("addCapabilities");
        ArrayList<Capability> caps = null;
        Device instance = null;
        instance.addCapabilities(caps);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDeviceName method, of class Device.
     */
    @Test
    public void testGetDeviceName_0args() {
        System.out.println("getDeviceName");
        Device instance = null;
        String expResult = "";
        String result = instance.getDeviceName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDeviceName method, of class Device.
     */
    @Test
    public void testGetDeviceName_Locale() {
        System.out.println("getDeviceName");
        Locale locale = null;
        Device instance = null;
        String expResult = "";
        String result = instance.getDeviceName(locale);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDeviceBlock method, of class Device.
     */
    @Test
    public void testGetDeviceBlock() {
        System.out.println("getDeviceBlock");
        Device instance = null;
        DeviceBlock expResult = null;
        DeviceBlock result = instance.getDeviceBlock();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDeviceNames method, of class Device.
     */
    @Test
    public void testGetDeviceNames() {
        System.out.println("getDeviceNames");
        Device instance = null;
        Map<Locale, String> expResult = null;
        Map<Locale, String> result = instance.getDeviceNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getImgName method, of class Device.
     */
    @Test
    public void testGetImgName() {
        System.out.println("getImgName");
        Device instance = null;
        String expResult = "";
        String result = instance.getImgName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addToPane method, of class Device.
     */
    @Test
    public void testAddToPane() {
        System.out.println("addToPane");
        Pane parent = null;
        Device instance = null;
        instance.addToPane(parent);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addCapability method, of class Device.
     */
    @Test
    public void testAddCapability() {
        System.out.println("addCapability");
        Capability cap = null;
        Device instance = null;
        instance.addCapability(cap);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeCapability method, of class Device.
     */
    @Test
    public void testRemoveCapability() {
        System.out.println("removeCapability");
        Capability cap = null;
        Device instance = null;
        boolean expResult = false;
        boolean result = instance.removeCapability(cap);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Device.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Device instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCapabilityBlock method, of class Device.
     */
    @Test
    public void testGetCapabilityBlock() {
        System.out.println("getCapabilityBlock");
        String capId = "";
        Device instance = null;
        Block expResult = null;
        Block result = instance.getCapabilityBlock(capId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
