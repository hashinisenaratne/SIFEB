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
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
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
    private static Semaphore writeLock = new Semaphore(1);

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
            if (!serialPort.isOpened()) {
                serialPort.openPort();
                setEventListener();
                System.out.println("opened the port");
                serialPort.setParams(SerialPort.BAUDRATE_9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
            }

        } catch (SerialPortException ex) {
//            Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex1) {
                Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex1);
            }
            JOptionPane.showMessageDialog(null, ex.getPortName()+" - "+ex.getExceptionType(), "COM Port Error", JOptionPane.ERROR_MESSAGE);
        }
//        timer.schedule(statusQuery, 2000, 5000);
        //   writeComPort(port, 10, "z");
    }

    public static void closePort() {
        try {
            if (serialPort.isOpened()) {
                serialPort.removeEventListener();
                serialPort.purgePort(0);
                serialPort.closePort();
                System.out.println("closed the port");
            }

        } catch (SerialPortException ex) {
            Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setEventListener() {
        try {
           // openPort();

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
            openPort();

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
        try {
            // serialPort = new SerialPort(port);
            writeLock.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            //Open port
            openPort();
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
        } finally {
            writeLock.release();
        }

    }

    static class SerialPortReader implements SerialPortEventListener {

        public BlockCreator blkC;

        public SerialPortReader(BlockCreator blkC) {
            this.blkC = blkC;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {

            System.out.println("%%%%%%%%%%%%% - event is - " + event);
            //     System.out.println("RXChar - "+event.isRXCHAR());
            //    System.out.println("event value - "+event.getEventValue());
            //   System.out.println("event type - "+event.getEventType());

            if (event.isRXCHAR()) {//If data is available
                // System.out.println("lll");
                // if (event.getEventValue() == 1) {//Check bytes count in the input buffer
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
                    byte[] readByte = serialPort.readBytes(4);
                    String readValue = new String(readByte);

//                    byte[] readByte = serialPort.readBytes();
//                    String readValue = new String(readByte);
                    System.out.println("read val - " + readValue);

                    while (!readValue.contains("#")) {

                        if (readValue.charAt(0) == 'h') {
                            int gh = readByte[1] & 0xFF;
                            blkC.mainEditor.hValue = gh;
                            System.out.println("gggggg- " + gh);
                        } else {
                            blkC.addMessagetoQueue(readValue);
                        }

                        System.out.println("added to queue");
                        readValue = serialPort.readString(4);
                        //   System.out.println("read val#2 %%%% - " + readValue);
                    }
//                    if (readValue.contains("#") && (!readValue.equals("##"))) {
//                        blkC.addMessagetoQueue(readValue);
//                    }
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
                    //  System.out.println("read value ###3### - "+readValue);
                    //  blkC.createBlock(address);
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                }
                //  }
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
//        
//        for (int i = 0; i < 10; i++) {
//            try {
//                writeComPort("Com15", 10, "h");
//                Thread.sleep(1500);
//            }
//            //writeComPort("Com15", 10, "h");
//            // write("COM44", "C");
//            //System.out.println(read("COM4",1));
//            catch (InterruptedException ex) {
//                Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
}
