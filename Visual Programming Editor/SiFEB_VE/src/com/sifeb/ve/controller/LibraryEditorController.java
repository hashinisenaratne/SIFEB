/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.controller;

import com.sifeb.ve.Capability;
import com.sifeb.ve.handle.FileHandler;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Udith Arosha
 */
public class LibraryEditorController implements Initializable {

    @FXML
    ChoiceBox<String> capTypeSelect;
    @FXML
    Tooltip capIdTT;
    @FXML
    Button capSaveBtn;
    @FXML
    TextField capIdTextBox;
    @FXML
    TextField capNameTextBox;
    @FXML
    CheckBox capHasTest;
    @FXML
    TextField capCmdTextBox;
    @FXML
    ImageView capStaticImgView;
    @FXML
    ImageView capDynamicImgView;
    @FXML
    Button staticOpenBtn;
    @FXML
    Button dynamicOpenBtn;

    @FXML
    ComboBox<ComboEntry> capOpenBtn;
    @FXML
    Button capClrBtn;

    final File CAPABILITY_FOLDER = new File("src/com/sifeb/ve/files/capabilities/");
    final File DEVICE_FOLDER = new File("src/com/sifeb/ve/files/devices/");
    final String IMG_FOLDER = "src/com/sifeb/ve/images/";
    final String S_IMG_FOLDER = "src/com/sifeb/ve/images/static/";
    final String D_IMG_FOLDER = "src/com/sifeb/ve/images/dynamic/";
    final String[] capTypes = new String[]{"action", "actionC", "sense", "condition"};
    FileHandler fileHandler;
    File capStaticImg;
    File capDynamicImg;
    Map<String, Capability> capList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        fileHandler = new FileHandler();

        capTypeSelect.getItems().addAll(capTypes);
        capTypeSelect.getSelectionModel().selectFirst();

        capList = new HashMap<>();
        File[] capFiles = CAPABILITY_FOLDER.listFiles();

        for (File capFile : capFiles) {
            String fileName = capFile.getName();
            String capID = fileName.substring(0, fileName.length() - 4);

            Capability cap = fileHandler.readFromCapabilityFile(capID);
            capList.put(capID, cap);
//            if (Arrays.asList(capTypes).contains(cap.getType())) {
//                capOpenBtn.getItems().add(new ComboEntry(cap.getCapID(), cap.getCapName(Locale.US)));
//            }
        }
        setEventHandlers();
    }

    private void setEventHandlers() {
//        capOpenBtn.setOnAction((event) -> {
//            String capID = capOpenBtn.getSelectionModel().getSelectedItem().getId();
//            Element el = fileHandler.readFromCapabilityFile(capID);
//            Capability selectedCap = getCapFromElement(el);
//            fillCapForm(selectedCap);
//
//        });

        capClrBtn.setOnAction((event) -> {
            capOpenBtn.setValue(null);
            clearCapForm();
        });

        capStaticImgView.setOnMouseEntered((event) -> {
            if (capStaticImgView.getImage() != null) {
                capStaticImgView.setFitWidth(capStaticImgView.getImage().getWidth());
                capStaticImgView.setFitHeight(capStaticImgView.getImage().getHeight());
                capStaticImgView.setEffect(new DropShadow(5, Color.BLACK));
            }
        });

        capStaticImgView.setOnMouseExited((event) -> {
            capStaticImgView.setFitWidth(30);
            capStaticImgView.setFitHeight(30);
            capStaticImgView.setEffect(null);
        });

        capDynamicImgView.setOnMouseEntered((event) -> {
            if (capDynamicImgView.getImage() != null) {
                capDynamicImgView.setFitWidth(capStaticImgView.getImage().getWidth());
                capDynamicImgView.setFitHeight(capStaticImgView.getImage().getHeight());
                capDynamicImgView.setEffect(new DropShadow(5, Color.BLACK));
            }
        });

        capDynamicImgView.setOnMouseExited((event) -> {
            capDynamicImgView.setFitWidth(30);
            capDynamicImgView.setFitHeight(30);
            capDynamicImgView.setEffect(null);
        });

        capSaveBtn.setOnAction((event) -> {
            saveCapability(true);
        });

        staticOpenBtn.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter filterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(filterPNG);

            capStaticImg = fileChooser.showOpenDialog(null);
            try {
                BufferedImage bImage = ImageIO.read(capStaticImg);
                Image sImg = SwingFXUtils.toFXImage(bImage, null);
                if (validateImgDimension(sImg)) {
                    capStaticImgView.setImage(sImg);
                } else {
                    capStaticImg = null;
                }
            } catch (IOException ex) {
                Logger.getLogger(LibraryEditorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        dynamicOpenBtn.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter filterPNG = new FileChooser.ExtensionFilter("GIF files (*.gif)", "*.gif");
            fileChooser.getExtensionFilters().add(filterPNG);

            capDynamicImg = fileChooser.showOpenDialog(null);
            try {
                BufferedImage bImage = ImageIO.read(capDynamicImg);
                Image sImg = SwingFXUtils.toFXImage(bImage, null);
                if (validateImgDimension(sImg)) {
                    capDynamicImgView.setImage(sImg);
                } else {
                    capDynamicImg = null;
                }
            } catch (IOException ex) {
                Logger.getLogger(LibraryEditorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private boolean validateImgDimension(Image img) {
        String ERROR_TITLE = "Invalid Image Dimensions";

        String capType = capTypeSelect.getValue();
        int idx = Arrays.asList(capTypes).indexOf(capType);
        double height = img.getHeight();
        double width = img.getWidth();
        boolean retVal = true;

        if (idx <= 1) {
            retVal = (height == 40);
            retVal = (width == 60);
            if (!retVal) {
                showErrorMessage(ERROR_TITLE, "Image dimensions should be 60x40");
            }
        } else {
            retVal = (height == 30);
            retVal = (width == 40);
            if (!retVal) {
                showErrorMessage(ERROR_TITLE, "Image dimensions should be 40x30");
            }
        }

        return retVal;

    }

    private void fillCapForm(Capability cap) {
        capIdTextBox.setText(cap.getCapID());
        capIdTextBox.setEditable(false);

        capNameTextBox.setText(cap.getCapName(Locale.US));
        capTypeSelect.getSelectionModel().select(cap.getType());

        capHasTest.setSelected(cap.isHasTest());
        capCmdTextBox.setText(cap.getCommand());

        capStaticImgView.setImage(cap.getStaticImage());
        capDynamicImgView.setImage(cap.getDynamicImage());
    }

    private void clearCapForm() {
        capIdTextBox.setText("");
        capIdTextBox.setEditable(true);

        capNameTextBox.setText("");
        capTypeSelect.getSelectionModel().selectFirst();

        capHasTest.setSelected(false);
        capCmdTextBox.setText("");
        
        capStaticImg = null;
        capStaticImgView.setImage(null);
        
        capDynamicImg = null;
        capDynamicImgView.setImage(null);
    }    

    private void saveCapability(boolean isNewEntry) {
        String ERROR_TITLE = "Invalid Input";

        String capID = capIdTextBox.getText();
        String capName = capNameTextBox.getText();
        String capType = capTypeSelect.getValue();
        boolean hasTest = capHasTest.isSelected();
        String capCmd = capCmdTextBox.getText();
        if (isNewEntry) {
            if ((capID == null) || (capID.isEmpty())) {
                showErrorMessage(ERROR_TITLE, "Please enter a valid capability ID");
                return;
            } else if (capList.containsKey("cap_" + capID)) {
                showErrorMessage(ERROR_TITLE, "Capability ID is already existing");
                return;
            }
        }
        if ((capName == null) || (capName.isEmpty())) {
            showErrorMessage(ERROR_TITLE, "Please enter a valid capability name");
            return;
        }
        if (hasTest && capCmd.isEmpty()) {
            showErrorMessage(ERROR_TITLE, "Please enter a valid command or untick \'Has TEST Button\'");
            return;
        }

        if (capStaticImg == null) {
            showErrorMessage(ERROR_TITLE, "Please select a .png image for static image");
            return;
        }
        
        String capIDFull = "cap_"+capID;
        Map<Locale,String> names = new HashMap<>();
         
        try {
            //uploading images to the relevant directories
            Files.copy(capStaticImg.toPath(), new File(S_IMG_FOLDER+capIDFull+".png").toPath(), StandardCopyOption.REPLACE_EXISTING);
            if(capDynamicImg!=null){
                Files.copy(capDynamicImg.toPath(), new File(D_IMG_FOLDER+capIDFull+".gif").toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                names.put(Locale.US, capName);
                boolean isSuccess = fileHandler.writeToCapabilityFile(capIDFull, names, capType, capCmd, capIDFull, hasTest);
                if(isSuccess){
                    showSuccessMessage("Success", "Capability saved successfully!");
                    clearCapForm();
                }
                else{
                    showErrorMessage("Error", "Capability not saved. Please try again.");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(LibraryEditorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    private void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE,new ImageIcon(IMG_FOLDER+"sad.png"));
    }
    
    private void showSuccessMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE,new ImageIcon(IMG_FOLDER+"happy.png"));
    }
}

class ComboEntry {

    private String id;
    private String name;

    public ComboEntry(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }

}
