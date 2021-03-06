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
import purejavacomm.*;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComPortControllerPureJavaComm {

    static Enumeration ports;
    static CommPortIdentifier pID;
    static OutputStream outStream;
    static SerialPort serPort;
    
    public static void writeComPort(String port, int address, String message)
            throws PortInUseException, IOException, UnsupportedCommOperationException {
                ports = CommPortIdentifier.getPortIdentifiers();
        boolean portFound = false;
        
        while(ports.hasMoreElements())
        {
            pID = (CommPortIdentifier)ports.nextElement();
            System.out.println("Port " + pID.getName());
            
            if (pID.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                if (pID.getName().equals(port))     // if found the needed port
                {
                    serPort = (SerialPort)pID.open(port,2000);
                    outStream = serPort.getOutputStream();
                    serPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
                    try {
                        Thread.sleep(2000);
                    } 
                    catch (InterruptedException ex) {
                    }
                    System.out.println(port + " found");
                    portFound = true;
                    break;
                }
            }
        }
        
        if(portFound){
            outStream.write((address + ":" + message + '\n').getBytes());  // 123:t,1
            if (port != null) {serPort.close();}
            System.out.print("wrote to the port");
        }
        
        else{
            System.out.println("port not found");
        }
    }


    // Test
    public static void main(String[] args) throws Exception {
        ComPortControllerPureJavaComm cc = new ComPortControllerPureJavaComm();
        cc.writeComPort("COM4", 10, "s");
     }
}