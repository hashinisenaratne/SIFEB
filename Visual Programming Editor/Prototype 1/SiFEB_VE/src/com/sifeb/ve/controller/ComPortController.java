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
import jssc.*;

public class ComPortController {

    public static String port = "COM16";
    
    public static void listComPorts(){
        String[] portNames = SerialPortList.getPortNames();
        for(int i = 0; i < portNames.length; i++){
            System.out.println(portNames[i]);
        }
    }

    public static String read(String port, int bytes){
        SerialPort serialPort = new SerialPort(port);
    try {
        //Open port
        serialPort.openPort();
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
        serialPort.closePort();
        return buffer.toString();
    }
    catch (SerialPortException ex) {
        System.out.println(ex);
    }
    return null;
    }

    public static void writeComPort(String port, int address, String msg){
        SerialPort serialPort = new SerialPort(port);
        try {
            //Open port
            serialPort.openPort();
            //We expose the settings. You can also use this line - serialPort.setParams(9600, 8, 1, 0);
            serialPort.setParams(SerialPort.BAUDRATE_9600, 
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);
            //Writes data to port
            serialPort.writeBytes(msg.getBytes());
            //serialPort.writeBytes((address + ":" + msg).getBytes());  // write to the port
            //(10 + ":" + "t,1\n")

            //Closing the port
            serialPort.closePort();
            System.out.print("wrote to the port");
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }
    // Test
    /*public static void main(String[] args) {
        //Method getPortNames() returns an array of strings. Elements of the array is already sorted.
        listComPorts();
        write("COM44", "C");
        //System.out.println(read("COM4",1));
    }*/
}