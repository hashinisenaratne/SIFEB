/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.handle;

import com.sifeb.ve.Block;
import com.sifeb.ve.Capability;
import com.sifeb.ve.ConditionBlock;
import com.sifeb.ve.Device;
import com.sifeb.ve.Holder;
import com.sifeb.ve.IfBlock;
import com.sifeb.ve.RepeatBlock;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

/**
 * This class generates code for visual programming blocks
 *
 * @author Udith Arosha
 */
public class CodeGenerator {

    /* INSTRUCTION DEFINITIONS
     *
     * BasicInstruction 'b'
     * ConditionalInstruction 'c'
     * JumpInstruction 'j'
     * EndInstruction 'e'
     * Equal '='
     * Inequal '!'
     * GreaterThanOrEqual '.'
     * Greater '>'
     * LesserThanOrEqual ','
     * Lesser '<'
     */
    Image playImg;

    /*
     * Returns the final code as an byte array
     */
    public byte[] generateCode(VBox editorBox) {
        ArrayList<Byte> bytes = generateByteList(editorBox);
        bytes.add((byte) 2);
        bytes.add((byte) 'e');
        byte[] byteArray = new byte[bytes.size()];

        for (int i = 0; i < bytes.size(); i++) {
            byteArray[i] = bytes.get(i);
        }

        return byteArray;
//        int count =0;
//        for(Byte b:bytes){
//            if(count==0){
//                System.out.println();
//                count = Byte.toUnsignedInt(b);
//            }
//            System.out.print(Byte.toUnsignedInt(b)+" ");
//            count--;
//        }
//        try {
//            FileOutputStream fos = new FileOutputStream(new File("C:\\Users\\Udith Arosha\\Desktop\\output.txt"));
//            byte[] outBytes = new byte[bytes.size()];
//            fos.write(outBytes);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /*
     * Calls the relevant code generation method
     * based on holder type
     */
    public ArrayList<Byte> generateByteList(VBox editorBox) {

        int numSteps = editorBox.getChildren().size();
        ArrayList<Byte> byteList = new ArrayList<>();

        for (int i = 0; i < numSteps; i++) {
            Holder h = (Holder) editorBox.getChildren().get(i);

            ArrayList<Byte> bytes;
            if (h.getClass().getName().contains("ConditionBlock")) {
                ConditionBlock cb = (ConditionBlock) editorBox.getChildren().get(i);
                bytes = generateConditionBlockCode(cb);
            } else if (h.getClass().getName().contains("RepeatBlock")) {
                RepeatBlock rb = (RepeatBlock) editorBox.getChildren().get(i);
                bytes = generateRepeatBlockCode(rb);
            } else if (h.getClass().getName().contains("IfBlock")) {
                IfBlock ib = (IfBlock) editorBox.getChildren().get(i);
                bytes = generateIfBlockCode(ib);
            } else {
                bytes = generateBlockCode(h);
            }

            if (bytes != null) {
                byteList.addAll(bytes);
            }
        }
        return byteList;
    }

    /*
     * Generates code for normal holder and 
     * returns as an ArrayList of bytes
     */
    public ArrayList<Byte> generateBlockCode(Holder h) {
        if (h.getActions().getChildren().size() == 0) {
            return null;
        }
        Block acBlock = (Block) h.getActions().getChildren().get(0);

        ArrayList<Byte> cmdArr = new ArrayList<>();

        //executing action
        char instruction = 'b';
        int address = acBlock.getCapability().getDevice().getAddress();
        char cmdChar = acBlock.getCapability().getExeCommand().charAt(0);
        char cmdChar1 = acBlock.getCapability().getExeCommand().charAt(1);
        cmdArr.add((byte) instruction);
        cmdArr.add((byte) address);
        cmdArr.add((byte) cmdChar);
        cmdArr.add((byte) cmdChar1);

        //prepending command length parameter
        cmdArr.add(0, (byte) (cmdArr.size() + 1));
        return cmdArr;
    }

    /*
     * Generates code for conditioned holder and 
     * returns as an ArrayList of bytes
     */
    public ArrayList<Byte> generateConditionBlockCode(ConditionBlock cb) {
        if ((cb.getActions().getChildren().size() == 0) || (cb.getCondition().getChildren().size() == 0)) {
            return null;
        }

        Block acBlock = (Block) cb.getActions().getChildren().get(0);
        Block conBlock = (Block) cb.getCondition().getChildren().get(0);

        ArrayList<Byte> cmdArr1 = new ArrayList<>();
        ArrayList<Byte> cmdArr2 = new ArrayList<>();
        ArrayList<Byte> cmdArr3 = new ArrayList<>();

        //starting action
        char instruction = 'b';
        int address = acBlock.getCapability().getDevice().getAddress();
        char cmdChar = acBlock.getCapability().getExeCommand().charAt(0);
        char cmdChar1 = acBlock.getCapability().getExeCommand().charAt(1);

        cmdArr1.add((byte) instruction);
        cmdArr1.add((byte) address);
        cmdArr1.add((byte) cmdChar);
        cmdArr1.add((byte) cmdChar1);
        //prepending command length parameter
        cmdArr1.add(0, (byte) (cmdArr1.size() + 1));

        //checking condition or sense
        instruction = 'c';
        address = conBlock.getCapability().getDevice().getAddress();
        cmdChar = conBlock.getCapability().getExeCommand().charAt(0);
        cmdChar1 = conBlock.getCapability().getExeCommand().charAt(1);
        char compType = conBlock.getCapability().getCompType().charAt(0);

        cmdArr2.add((byte) instruction);
        cmdArr2.add((byte) address);
        cmdArr2.add((byte) compType);
        short jumpAdd = 0;
        byte[] jumpArr = shortToByteArray(jumpAdd);
        for (byte b : jumpArr) {
            cmdArr2.add(b);
        }
        short respSize = Short.parseShort(conBlock.getCapability().getRespSize());
        byte[] respArr = shortToByteArray(respSize);
        cmdArr2.add(respArr[0]);
//        for(byte b:respArr){
//            cmdArr2.add(b);
//        }
        int refVal = Integer.parseInt(conBlock.getCapability().getRefValue());
        if (conBlock.getCapability().getType().equals(Capability.CAP_CONDITION)) {
            refVal = Integer.parseInt(conBlock.getTextField().getText());
        }
        byte[] refArr = intToByteArray(refVal);
        for (int i = 0; i < respSize; i++) {
            cmdArr2.add(refArr[i]);
        }
        cmdArr2.add((byte) cmdChar);
        cmdArr2.add((byte) cmdChar1);
        //prepending command length parameter
        cmdArr2.add(0, (byte) (cmdArr2.size() + 1));

        //terminating action
        instruction = 'b';
        address = acBlock.getCapability().getDevice().getAddress();
        cmdChar = acBlock.getCapability().getStopCommand().charAt(0);
        cmdChar1 = acBlock.getCapability().getStopCommand().charAt(1);
        cmdArr3.add((byte) instruction);
        cmdArr3.add((byte) address);
        cmdArr3.add((byte) cmdChar);
        cmdArr3.add((byte) cmdChar1);
        //prepending command length parameter
        cmdArr3.add(0, (byte) (cmdArr3.size() + 1));

        //merging lists together
        cmdArr1.addAll(cmdArr2);
        cmdArr1.addAll(cmdArr3);
        return cmdArr1;
    }

    /*
     * Recursively generates code for repeat holder and 
     * returns as an ArrayList of bytes
     */
    public ArrayList<Byte> generateRepeatBlockCode(RepeatBlock rb) {
        if ((rb.getActions().getChildren().size() == 0) || (rb.getCondition().getChildren().size() == 0)
                || rb.getHolders().getChildren().size() == 0) {
            return null;
        }
        Block conBlock = (Block) rb.getCondition().getChildren().get(0);

        ArrayList<Byte> childArr = generateByteList(rb.getHolders());
        ArrayList<Byte> cmdArr = new ArrayList<>();
        //checking condition or sense
        char instruction = 'c';
        int address = conBlock.getCapability().getDevice().getAddress();
        char cmdChar = conBlock.getCapability().getExeCommand().charAt(0);
        char cmdChar1 = conBlock.getCapability().getExeCommand().charAt(1);
        char compType = conBlock.getCapability().getCompType().charAt(0);

        switch (conBlock.getCapability().getCompType().charAt(0)) {

            case '=':
                compType = '!';
                break;
            case '!':
                compType = '=';
                break;
            case '<':
                compType = '.';
                break;
            case '>':
                compType = ',';
                break;
            case '.':
                compType = '<';
                break;
            case ',':
                compType = '>';
                break;
        }

        cmdArr.add((byte) instruction);
        cmdArr.add((byte) address);
        cmdArr.add((byte) compType);

        short respSize = Short.parseShort(conBlock.getCapability().getRespSize());
        byte[] respArr = shortToByteArray(respSize);
        cmdArr.add(respArr[0]);
//        for(byte b:respArr){
//            cmdArr.add(b);
//        }
        int refVal = Integer.parseInt(conBlock.getCapability().getRefValue());
        if (conBlock.getCapability().getType().equals(Capability.CAP_CONDITION)) {
            refVal = Integer.parseInt(conBlock.getTextField().getText());
        }
        byte[] refArr = intToByteArray(refVal);
        for (int i = 0; i < respSize; i++) {
            cmdArr.add(refArr[i]);
        }
        cmdArr.add((byte) cmdChar);
        cmdArr.add((byte) cmdChar1);
        short jumpAdd = (short) (childArr.size() + 4 + cmdArr.size() + 3);
        byte[] jumpArr = shortToByteArray(jumpAdd);
        int counter = 3;
        for (byte b : jumpArr) {
            cmdArr.add(counter++, b);
        }
        //prepending command length parameter
        cmdArr.add(0, (byte) (cmdArr.size() + 1));

        //jump back instruction
        ArrayList<Byte> jumpBytes = new ArrayList<>();
        instruction = 'j';
        jumpAdd = (short) (-childArr.size() - cmdArr.size());
        jumpBytes.add((byte) instruction);
        jumpArr = shortToByteArray(jumpAdd);
        for (byte b : jumpArr) {
            jumpBytes.add(b);
        }
        //prepending command length parameter
        jumpBytes.add(0, (byte) (jumpBytes.size() + 1));

        //merging lists
        cmdArr.addAll(childArr);
        cmdArr.addAll(jumpBytes);

        return cmdArr;
    }

    /*
     * Recursively generates code for If-Else holder and 
     * returns as an ArrayList of bytes
     */
    public ArrayList<Byte> generateIfBlockCode(IfBlock ib) {
        if ((ib.getActions().getChildren().size() == 0) || (ib.getCondition().getChildren().size() == 0)
                || ib.getIfHolders().getChildren().size() == 0) {
            return null;
        }
        Block conBlock = (Block) ib.getCondition().getChildren().get(0);

        ArrayList<Byte> ifArr = generateByteList(ib.getIfHolders());
        ArrayList<Byte> elseArr = generateByteList(ib.getElseHolders());

        ArrayList<Byte> cmdArr = new ArrayList<>();
        //checking condition or sense
        char instruction = 'c';
        int address = conBlock.getCapability().getDevice().getAddress();
        char cmdChar = conBlock.getCapability().getExeCommand().charAt(0);
        char cmdChar1 = conBlock.getCapability().getExeCommand().charAt(1);

        char compType = conBlock.getCapability().getCompType().charAt(0);
        cmdArr.add((byte) instruction);
        cmdArr.add((byte) address);
        cmdArr.add((byte) compType);
        short respSize = Short.parseShort(conBlock.getCapability().getRespSize());
        byte[] respArr = shortToByteArray(respSize);
        cmdArr.add(respArr[0]);
        int refVal = Integer.parseInt(conBlock.getCapability().getRefValue());
        if (conBlock.getCapability().getType().equals(Capability.CAP_CONDITION)) {
            refVal = Integer.parseInt(conBlock.getTextField().getText());
        }
        byte[] refArr = intToByteArray(refVal);
        for (int i = 0; i < respSize; i++) {
            cmdArr.add(refArr[i]);
        }
        cmdArr.add((byte) cmdChar);
        cmdArr.add((byte) cmdChar1);
        short jumpAdd = (short) (ifArr.size() + 4 + cmdArr.size() + 3);
        byte[] jumpArr = shortToByteArray(jumpAdd);
        int counter = 3;
        for (byte b : jumpArr) {
            cmdArr.add(counter++, b);

        }
        //prepending command length parameter
        cmdArr.add(0, (byte) (cmdArr.size() + 1));

        //jump forward instruction
        ArrayList<Byte> jumpBytes = new ArrayList<>();
        instruction = 'j';
        jumpAdd = (short) (elseArr.size() + 4);
        jumpBytes.add((byte) instruction);
        jumpArr = shortToByteArray(jumpAdd);
        for (byte b : jumpArr) {
            jumpBytes.add(b);
        }
        //prepending command length parameter
        jumpBytes.add(0, (byte) (jumpBytes.size() + 1));

        //merging lists
        cmdArr.addAll(ifArr);
        cmdArr.addAll(jumpBytes);
        cmdArr.addAll(elseArr);

        return cmdArr;
    }
    
    /*
     * Generates code for real-time capability demostration
     */
    public byte[] generateTestBlockCode(Block acBlock) {

        ArrayList<Byte> cmdArr = new ArrayList<>();

        //executing action
        char instruction = 'b';
        int address = acBlock.getCapability().getDevice().getAddress();
        char cmdChar = acBlock.getCapability().getTestCommand().charAt(0);
        char cmdChar1 = acBlock.getCapability().getTestCommand().charAt(1);

        cmdArr.add((byte) instruction);
        cmdArr.add((byte) address);
        cmdArr.add((byte) cmdChar);
        cmdArr.add((byte) cmdChar1);

        //prepending command length parameter
        cmdArr.add(0, (byte) (cmdArr.size() + 1));

        byte[] byteArray = new byte[cmdArr.size()];

        for (int i = 0; i < cmdArr.size(); i++) {
            byteArray[i] = cmdArr.get(i);
        }

        return byteArray;

    }

    /*
     * Generates code for device identification
     */
    public byte[] generateTestDeviceCode(Device device) {

        ArrayList<Byte> cmdArr = new ArrayList<>();

        //executing action
        char instruction = 'b';
        int address = device.getAddress();
        char cmdChar = 's';
        cmdArr.add((byte) instruction);
        cmdArr.add((byte) address);
        cmdArr.add((byte) cmdChar);

        //prepending command length parameter
        cmdArr.add(0, (byte) (cmdArr.size() + 1));

        byte[] byteArray = new byte[cmdArr.size()];

        for (int i = 0; i < cmdArr.size(); i++) {
            byteArray[i] = cmdArr.get(i);
        }

        return byteArray;

    }

    /////////// SUPPORT FUNCTIONS //////////////
    
    /*
     * Converts a short into 2-byte array
     */
    private byte[] shortToByteArray(short i) {
        ByteBuffer bbf = ByteBuffer.allocate(2)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putShort(i);
        return bbf.array();
    }

    /*
     * Converts an integer into 4-byte array
     */
    private byte[] intToByteArray(int i) {
        ByteBuffer bbf = ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(i);
        return bbf.array();
    }
}
