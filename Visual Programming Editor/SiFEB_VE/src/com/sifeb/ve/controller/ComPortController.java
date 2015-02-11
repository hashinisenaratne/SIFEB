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
import com.sifeb.ve.MainApp;
import com.sifeb.ve.handle.BlockCreator;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.*;

public class ComPortController {

    public static String port = "COM20";
    public static SerialPort serialPort = new SerialPort(port);
    public static BlockCreator blkCreator;
    private static Semaphore writeLock = new Semaphore(1);

    public static void main(String[] args) {

//        listComPorts();
        //        // getBluetooth();
        //        System.out.println("sssssss - ");
        //        //checkConnectedPort();
        //        System.out.println(checkConnectedPort());
    }

    public static void setBlockCreator(BlockCreator blkCreator) {
        ComPortController.blkCreator = blkCreator;
    }

    public static boolean checkConnectedPort() {

        boolean isPortOpen = false;
        String[] portNames = SerialPortList.getPortNames();

        for (int i = 0; i < portNames.length; i++) {

            System.out.println("port - " + portNames[i]);
            SerialPort sport = new SerialPort(portNames[i]);

            if (openPort(sport)) {
                serialPort = sport;
                port = portNames[i];
                isPortOpen = true;
                break;
            }

        }

        return isPortOpen;

    }

    public static boolean openPort(SerialPort sport) {

        boolean isPortOpen = false;

        try {
            if (!sport.isOpened()) {
                sport.openPort();
                System.out.println("opened the port");
                sport.setParams(SerialPort.BAUDRATE_9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_EVEN);

                String msg = "AreYouSiFEB?";
                int inputByteSize = 1;

                //sport.writeBytes(msg.getBytes());
                //  sport.writeByte((byte) 'f');
                System.out.println("openeddddd the port");

                if (sport.isDSR()) {
                    System.out.println("dsr have ");
                    //read data
                }
                // byte[] buffer = serialPort.readBytes(inputByteSize);

//                if (buffer.toString().equals("YesIamSiFEB")) {
//                    isPortOpen = true;
//                    setEventListener(sport);
//                } else {
//                    isPortOpen = false;
//                }
            }
        } catch (SerialPortException ex) {
            isPortOpen = false;
            Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            return isPortOpen;
        }
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
                //setEventListener();
                System.out.println("opened the port");
                serialPort.setParams(SerialPort.BAUDRATE_9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_EVEN);

            }

        } catch (SerialPortException ex) {
            Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);

        }

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
            serialPort.removeEventListener();

        } catch (SerialPortException ex) {
            Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {

                int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
                serialPort.setEventsMask(mask);//Set mask
                System.out.println("block creator id" + blkCreator);

                ComPortController.SerialPortReader reader = new ComPortController.SerialPortReader(ComPortController.blkCreator);
                serialPort.addEventListener(reader);//Add SerialPortEventListener

            } catch (SerialPortException ex) {
                Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void removeEventListener() {
        try {
            serialPort.removeEventListener();
            System.out.println(" removed eventlisterner");
        } catch (SerialPortException ex) {
            Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static byte[] read(int bytes) {

        byte[] buffer = null;
        try {
            writeLock.acquire();
            openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            boolean data = serialPort.isDSR();
            System.out.println("dataddd - " + data);
            buffer = serialPort.readBytes(bytes);
            System.out.println("Buffer received in serial read");

            for (int i = 0; i < buffer.length; i++) {
                System.out.println(buffer[i]);
            }
            data = serialPort.isDSR();
            System.out.println("dataddd - " + data);
            serialPort.purgePort(bytes);
        } catch (SerialPortException ex) {
            System.out.println(ex);
        } finally {
            writeLock.release();
            return buffer;

        }

    }

    public static void writeComPort(String msg) {
        try {
            // serialPort = new SerialPort(port);
            writeLock.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            //Open port
            openPort();
            serialPort.writeBytes(msg.getBytes());

            System.out.println("wrote to the port");
        } catch (SerialPortException ex) {
            System.out.println(ex);
        } finally {
            writeLock.release();
        }

    }

    public static void writeProgram(byte[] byteArray) {
        try {
            // serialPort = new SerialPort(port);
            writeLock.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(ComPortController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            //Open port
            openPort();
            serialPort.writeBytes(byteArray);
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

        public String processByteArray(byte[] readByte) {

            String readValue = "";

            String command = new String(new byte[]{readByte[0]});//Byte.toString(readByte[0]);
            readValue += command + ",";
            int address = Byte.toUnsignedInt(readByte[1]);
            String addressValue = Integer.toString(address);
            readValue += addressValue + ",";

            if (command.equals("c")) {
                char type = (char) readByte[2];
                System.out.println("dddddddd " + type);
                // String typeValue = Integer.toString(type);
                readValue += type + ",";
            }

            // String endHash = new String(readByte, 2, 2);
            // readValue += endHash;
            System.out.println("readValue after process00000 - " + readValue);
            return readValue;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {

            System.out.println("%%%%%%%%%%%%% - event is - " + event);

            if (event.isRXCHAR()) {//If data is available

                // int bufferSize = event.getEventValue();
                /*
                 if (bufferSize > 0) {//Check bytes count in the input buffer
                 //Read data, if 10 bytes available 
                 try {
                 byte buffer[] = serialPort.readBytes(bufferSize);

                 System.out.println("Buffer received");

                 for (int i = 0; i < buffer.length; i++) {
                 System.out.println((char) buffer[i]);
                 }
                 // System.out.println(buffer);

                 } catch (SerialPortException ex) {
                 System.out.println(ex);
                 }
                 }
                 */
                try {

                    byte[] readByte = serialPort.readBytes(5);

                    //  System.out.println("read val1 - " + readValue);
                    String command = new String(new byte[]{readByte[0]});// Byte.toString(readByte[0]);
                    String readValue = "";
                    // readValue = "c,34,4";
                    while (!command.contains("#")) {

                        readValue = processByteArray(readByte);
                        System.out.println("innnnnn");
                        System.out.println("read val1 - " + readValue);
                        System.out.println("blockcccccc - " + blkC);
                        MainApp.blockCreator.addMessagetoQueue(readValue);

                        System.out.println("added to queue");
                        //  readByte = serialPort.readBytes(5);
                        readByte = serialPort.readBytes(5);//new String(readBytes);
                        command = new String(new byte[]{readByte[0]});
                        System.out.println("read val while loop - " + readValue);
                    }

                } catch (SerialPortException ex) {
                    System.out.println(ex);
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
