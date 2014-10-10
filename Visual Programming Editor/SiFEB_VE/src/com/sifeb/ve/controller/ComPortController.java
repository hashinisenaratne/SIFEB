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
import com.sifeb.ve.handle.BlockCreator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.*;

public class ComPortController {

    public static String port = "COM18";
    public static SerialPort serialPort = new SerialPort(port);
    public static BlockCreator blkCreator;
    public static Timer timer = new Timer();
    public static TimerTask statusQuery = new TimerTask() {
        @Override
        public void run() {
            writeComPort(port, 10, "z");
        }
    };

    public static void setBlockCreator(BlockCreator blkCreator) {
        ComPortController.blkCreator = blkCreator;
    }

    public static void listComPorts() {
        String[] portNames = SerialPortList.getPortNames();
        for (int i = 0; i < portNames.length; i++) {
            System.out.println(portNames[i]);
        }
    }

    public static void openPort() {
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
        } catch (SerialPortException ex) {
            Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);
        }
        timer.schedule(statusQuery, 2000, 5000);
    }

    public static void closePort() {
        try {
            serialPort.closePort();
        } catch (SerialPortException ex) {
            Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setEventListener() {
        try {
            if (!serialPort.isOpened()) {
                serialPort.openPort();
            }
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            serialPort.setEventsMask(mask);//Set mask
            // ComPortEventListener listener=new ComPortEventListener();

            serialPort.addEventListener(new ComPortController.SerialPortReader(blkCreator));//Add SerialPortEventListener
        } catch (SerialPortException ex) {
            Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String read(String port, int bytes) {
        //  SerialPort serialPort = new SerialPort(port);
        try {
            //Open port
            if (!serialPort.isOpened()) {
                serialPort.openPort();
            }

            //We expose the settings. You can also use this line - serialPort.setParams(9600, 8, 1, 0);
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            //Read the data of 10 bytes. Be careful with the method readBytes(), if the number of bytes in the input buffer
            //is less than you need, then the method will wait for the right amount. Better to use it in conjunction with the
            //interface SerialPortEventListener.
            byte[] buffer = serialPort.readBytes(bytes);
            //Closing the port
            // serialPort.closePort();
            return buffer.toString();
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public static void writeComPort(String port, int address, String msg) {
        // serialPort = new SerialPort(port);
        try {
            //Open port
            if (!serialPort.isOpened()) {
                serialPort.openPort();
            }
            //We expose the settings. You can also use this line - serialPort.setParams(9600, 8, 1, 0);
//            serialPort.setParams(SerialPort.BAUDRATE_9600,
//                    SerialPort.DATABITS_8,
//                    SerialPort.STOPBITS_1,
//                    SerialPort.PARITY_NONE);
            //Writes data to port
            serialPort.writeBytes(msg.getBytes());
            //serialPort.writeBytes((address + ":" + msg).getBytes());  // write to the port
            //(10 + ":" + "t,1\n")

            //Closing the port
            // serialPort.closePort();
            System.out.println("wrote to the port");
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }

    static class SerialPortReader implements SerialPortEventListener {

        public BlockCreator blkC;

        public SerialPortReader(BlockCreator blkC) {
            this.blkC = blkC;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {

            //  System.out.println(event);
            if (event.isRXCHAR()) {//If data is available
                // System.out.println("lll");
                if (event.getEventValue() == 1) {//Check bytes count in the input buffer
                    //Read data, if 10 bytes available 
                    try {
                        //byte buffer[] = serialPort.readBytes(1);

//                        String readValue = serialPort.readString(4);
//                        System.out.println("MMMMMMM - "+readValue);
//
//                        blkC.addMessagetoQueue(readValue);
//                        char command = readValue.charAt(0);
//
//                        String address = Integer.toString((int) readValue.charAt(1));
//
//                        System.out.println("command - " + command + " add - " + address);
                        String readValue = serialPort.readString(4);

                        while (!readValue.contains("##")) {
                            blkC.addMessagetoQueue(readValue);
                            readValue = serialPort.readString(4);
                            System.out.println("llll - " + readValue);
                        }
                      //  System.out.println("MMMMMMM - "+readValue);

//                        blkC.addMessagetoQueue(readValue);
//                        char command = readValue.charAt(0);
//
//                        String address = Integer.toString((int) readValue.charAt(1));
//
//                        System.out.println("command - " + command + " add - " + address);
//
//                        switch (command) {
//                            case 'c':
//                                blkC.createBlock(address);
//                                break;
//                            case 'd':
//                                blkC.removeBlock(address);
//                                break;
//                        }
                        System.out.println(readValue);
                        //  blkC.createBlock(address);
                    } catch (SerialPortException ex) {
                        System.out.println(ex);
                    }
                }
            } else if (event.isCTS()) {//If CTS line has changed state
                if (event.getEventValue() == 1) {//If line is ON
                    System.out.println("CTS - ON");
                } else {
                    System.out.println("CTS - OFF");
                }
            } else if (event.isDSR()) {///If DSR line has changed state
                if (event.getEventValue() == 1) {//If line is ON
                    System.out.println("DSR - ON");
                } else {
                    System.out.println("DSR - OFF");
                }
            }
        }

    }

    // Test
//    public static void main(String[] args) {
//     //Method getPortNames() returns an array of strings. Elements of the array is already sorted.
//        //listComPorts();
//
//        openPort();
//        setEventListener();
//        writeComPort("Com", 10, "h");
//
//    // write("COM44", "C");
//        //System.out.println(read("COM4",1));
//    }
}
