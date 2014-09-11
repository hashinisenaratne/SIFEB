/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.controller;

/**
 *
 * @author Hashini Senaratne
 */
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComPortControllerRxtx {

    static Enumeration ports;
    static CommPortIdentifier pID;
    static OutputStream outStream;
    static SerialPort serPort;
    public static String port = "COM16";
    
    public static void writeComPort(String port, int address, String message)
            throws PortInUseException, IOException, UnsupportedCommOperationException {
        // 's'(show) , 't1'(test action 1)
        //ports = CommPortIdentifier.getPortIdentifiers();
        boolean portFound = false;

        System.out.println("message - "+message);
        try {
            //while (ports.hasMoreElements()) {
            //pID = (CommPortIdentifier) ports.nextElement();
            pID = CommPortIdentifier.getPortIdentifier(port);
        } catch (NoSuchPortException ex) {
            Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);
        }
            System.out.println("Port " + pID.getName());

            if (pID.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (pID.getName().equals(port)) // if found the needed port
                {
                    serPort = (SerialPort) pID.open(port, 2000);

                    outStream = serPort.getOutputStream();
                    serPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println(port + " found");
                    portFound = true;
                    //break;
                }
            }
        //}

        if (portFound) {
          //outStream.write((10 + ":" + "t,1\n").getBytes());  // write to the port
           outStream.write((message).getBytes());  // write to the port
            if (port != null) {
                serPort.close();
            }
            System.out.print("wrote to the port");
        } else {
            System.out.println("port not found");
        }
    }

    // Test
    /*public static void main(String[] args) throws Exception {
     ComPortControllerRxtx cc = new ComPortControllerRxtx();
     cc.writeComPort("COM1", 10, "s");
     }*/
}