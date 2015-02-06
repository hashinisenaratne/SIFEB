/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.controller;

import com.sifeb.ve.Capability;
import com.sifeb.ve.Device;
import com.sifeb.ve.MainApp;
import com.sifeb.ve.handle.FileHandler;
import com.sifeb.ve.resources.SifebUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

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
    TextField capTestTextBox;
    @FXML
    ImageView capStaticImgView;
    @FXML
    ImageView capDynamicImgView;
    @FXML
    Button staticOpenBtn;
    @FXML
    Button dynamicOpenBtn;
    @FXML
    Button capEditBtn;
    @FXML
    Button capClrBtn;
    @FXML
    Button capDelBtn;
    @FXML
    ProgressIndicator capWait;
    @FXML
    GridPane capGrid;
    @FXML
    Tab capabilityTab;
    @FXML
    Tab deviceTab;
    @FXML
    TabPane libTabPane;

    @FXML
    ChoiceBox<String> devTypeSelect;
    @FXML
    Tooltip devIdTT;
    @FXML
    Button devSaveBtn;
    @FXML
    TextField devIdTextBox;
    @FXML
    TextField devNameTextBox;
    @FXML
    Button devEditBtn;
    @FXML
    Button devClrBtn;
    @FXML
    Button devDelBtn;
    @FXML
    ProgressIndicator devWait;
    @FXML
    Button devCapBtn;
    @FXML
    GridPane devGrid;
    @FXML
    ImageView devImgView;
    @FXML
    Button devImgBtn;
    @FXML
    AnchorPane devPane;
    @FXML
    AnchorPane capPane;
    @FXML
    ChoiceBox<String> compTypeSelect;
    @FXML
    TextField capRespSizeTextBox;
    @FXML
    TextField capRefValTextBox;
    @FXML
    ChoiceBox<String> devSelect;
    @FXML
    TextField capCmdTextBox;
    @FXML
    TextField capStopCmdTextBox;

    final File CAPABILITY_FOLDER = new File(SifebUtil.CAP_FILE_DIR);
    final File DEVICE_FOLDER = new File(SifebUtil.DEV_FILE_DIR);
    final String[] capTypes = new String[]{Capability.CAP_ACTION, Capability.CAP_ACTION_C, Capability.CAP_SENSE, Capability.CAP_CONDITION};
    final String[] devTypes = new String[]{Device.DEV_ACTUATOR, Device.DEV_SENSOR};
    final String[] compTypes = new String[]{
        "< (Response Value < Reference Value)",
        "= (Response Value = Reference Value)",
        "> (Response Value > Reference Value)"
    };
    FileHandler fileHandler;
    File capStaticImg;
    File capDynamicImg;
    File devImg;
    Map<String, Capability> capList;
    ArrayList<Device> devList;
    boolean capModified;
    boolean devModified;
    boolean isNewCap;
    boolean isNewDev;
    String[] devCapIds;

    ArrayList<SelectedEntry> selectedCaps = new ArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        fileHandler = new FileHandler();
        setEventHandlers();

        capTypeSelect.getItems().addAll(capTypes);
        capTypeSelect.getSelectionModel().selectFirst();

        devTypeSelect.getItems().addAll(devTypes);
        devTypeSelect.getSelectionModel().selectFirst();

        compTypeSelect.getItems().addAll(compTypes);
        compTypeSelect.getSelectionModel().selectFirst();

        capList = new HashMap<>();
        devList = new ArrayList<>();
        capModified = true;
        devModified = true;
        isNewCap = true;
        isNewDev = true;
        refreshCapList();
        refreshSelectedCaps();
        refreshDevList();
        devCapIds = new String[0];

    }

    private void refreshSelectedCaps() {
        selectedCaps.clear();
        if (isNewDev) {
            for (Capability cap : capList.values()) {
                SelectedEntry se = new SelectedEntry(cap.getCapID(), cap.toString());
                selectedCaps.add(se);
            }
        } else {
            for (Capability cap : capList.values()) {
                String id = cap.getCapID();
                SelectedEntry se = new SelectedEntry(id, cap.toString());
                if (Arrays.asList(devCapIds).contains(id)) {
                    se.setSelected(true);
                }
                selectedCaps.add(se);
            }
        }
    }

    private void refreshCapList() {
        if (capModified) {
            capList.clear();
            File[] capFiles = CAPABILITY_FOLDER.listFiles();

            for (File capFile : capFiles) {
                String fileName = capFile.getName();
                String capID = fileName.substring(0, fileName.length() - 4);

                Capability cap = fileHandler.readFromCapabilityFile(capID);
                if ((!cap.getType().equals(Capability.CAP_CONTROL)) && (!cap.getType().equals(Capability.CAP_IFELSE))) {
                    capList.put(capID, cap);
                }
            }
            capModified = false;
            refreshSelectedCaps();
        }
    }

    private void refreshDevList() {
        if (devModified) {
            devList.clear();
            File[] devFiles = DEVICE_FOLDER.listFiles();

            devSelect.getItems().clear();
            for (File devFile : devFiles) {
                String fileName = devFile.getName();
                String devID = fileName.substring(0, fileName.length() - 4);

                Device d = fileHandler.readFromDeviceFile(devID, "0");
                devList.add(d);
                devSelect.getItems().add(d.toString());
            }
            devModified = false;
        }
    }

    private void setEventHandlers() {
        capEditBtn.setOnAction((event) -> {
            refreshCapList();
            Optional<Capability> response = Dialogs.create()
                    .title("Edit capability")
                    .masthead("Select a capability to Edit")
                    .message("Select Capability:")
                    .showChoices(capList.values());

            response.ifPresent(chosen -> fillCapForm(chosen));

        });

        capDelBtn.setOnAction((event) -> {
            refreshCapList();
            Optional<Capability> response = Dialogs.create()
                    .title("Edit capability")
                    .masthead("Select a capability to Edit")
                    .message("Select Capability:")
                    .showChoices(capList.values());

            response.ifPresent(chosen -> removeCapability(chosen));

        });

        capClrBtn.setOnAction((event) -> {
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
            saveCapability(isNewCap);
        });

        staticOpenBtn.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter filterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(filterPNG);

            File capStaticTemp = fileChooser.showOpenDialog(null);
            if (capStaticTemp != null) {
                try {
                    BufferedImage bImage = ImageIO.read(capStaticTemp);
                    Image sImg = SwingFXUtils.toFXImage(bImage, null);
                    if (validateImgDimension(sImg, false)) {
                        capStaticImg = capStaticTemp;
                        capStaticImgView.setImage(sImg);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LibraryEditorController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        dynamicOpenBtn.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter filterPNG = new FileChooser.ExtensionFilter("GIF files (*.gif)", "*.gif");
            fileChooser.getExtensionFilters().add(filterPNG);

            File capDynamicTemp = fileChooser.showOpenDialog(null);
            if (capDynamicTemp != null) {
                try {
                    BufferedImage bImage = ImageIO.read(capDynamicTemp);
                    Image sImg = SwingFXUtils.toFXImage(bImage, null);
                    if (validateImgDimension(sImg, false)) {
                        capDynamicImg = capDynamicTemp;
                        capDynamicImgView.setImage(sImg);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LibraryEditorController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        devSaveBtn.setOnAction((event) -> {
            saveDevice(isNewDev);
        });

        devClrBtn.setOnAction((event) -> {
            clearDevForm();
        });

//        devCapBtn.setOnAction((event) -> {
//            showCapSelection(isNewDev);
//        });
        devImgBtn.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter filterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(filterPNG);

            File devImgTemp = fileChooser.showOpenDialog(null);
            if (devImgTemp != null) {
                try {
                    BufferedImage bImage = ImageIO.read(devImgTemp);
                    Image img = SwingFXUtils.toFXImage(bImage, null);
                    if (validateImgDimension(img, true)) {
                        devImg = devImgTemp;
                        devImgView.setImage(img);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LibraryEditorController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        devImgView.setOnMouseEntered((event) -> {
            if (devImgView.getImage() != null) {
                devImgView.setFitWidth(devImgView.getImage().getWidth());
                devImgView.setFitHeight(devImgView.getImage().getHeight());
                devImgView.setEffect(new DropShadow(5, Color.BLACK));
            }
        });

        devImgView.setOnMouseExited((event) -> {
            devImgView.setFitWidth(30);
            devImgView.setFitHeight(30);
            devImgView.setEffect(null);
        });

        devEditBtn.setOnAction((event) -> {
            Optional<Device> response = Dialogs.create()
                    .title("Edit device")
                    .masthead("Select a device to Edit")
                    .message("Select Device:")
                    .showChoices(devList);

            response.ifPresent(chosen -> fillDevForm(chosen));

        });

        devDelBtn.setOnAction((event) -> {
            Optional<Device> response = Dialogs.create()
                    .title("Remove device")
                    .masthead("Select a device to Remove")
                    .message("Select Device:")
                    .showChoices(devList);

            response.ifPresent(chosen -> removeDevice(chosen));

        });

        capTypeSelect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                String selected = capTypeSelect.getItems().get((int) newValue);
                switch (selected) {
                    case Capability.CAP_SENSE:
                        capStopCmdTextBox.setDisable(true);
                        capRefValTextBox.setDisable(false);
                        capRespSizeTextBox.setDisable(false);
                        compTypeSelect.setDisable(false);
                        break;
                    case Capability.CAP_CONDITION:
                        capStopCmdTextBox.setDisable(true);
                        capRefValTextBox.setDisable(true);
                        capRespSizeTextBox.setDisable(false);
                        compTypeSelect.setDisable(false);
                        break;
                    case Capability.CAP_ACTION_C:
                        capStopCmdTextBox.setDisable(false);
                        capRefValTextBox.setDisable(true);
                        capRespSizeTextBox.setDisable(true);
                        compTypeSelect.setDisable(true);
                        break;
                    default:
                        capStopCmdTextBox.setDisable(true);
                        capRefValTextBox.setDisable(true);
                        capRespSizeTextBox.setDisable(true);
                        compTypeSelect.setDisable(true);
                        break;
                }
            }
        });
        devSelect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if ((int) newValue != -1) {
                    Device selected = devList.get((int) newValue);
                    int numCaps = selected.getCapabilities().size();
                    String newCapId = selected.getDeviceID().substring(4) + "_" + String.format("%03d", numCaps + 1);
                    capIdTextBox.setText(newCapId);
                }
            }
        });

        capHasTest.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                capTestTextBox.setDisable(!newValue);
            }

        });
    }

    private void removeCapability(Capability cap) {
        //confirm remove operation
        Action response = Dialogs.create()
                .title("Confirm Delete")
                .masthead("Do you really want to remove this capability?")
                .message("Capability ID\t\t: " + cap.getCapID()
                        + "\nCapability Name\t: " + cap.getCapName()
                        + "\nCapability Type\t: " + cap.getType())
                .actions(Dialog.Actions.YES, Dialog.Actions.NO)
                .showConfirm();
    }

    private void removeDevice(Device d) {
        //confirm remove operation
        Action response = Dialogs.create()
                .title("Confirm Delete")
                .masthead("Do you really want to remove this device?")
                .message("Device ID\t\t: " + d.getDeviceID()
                        + "\nDevice Name\t: " + d.getDeviceName()
                        + "\nDevice Type\t: " + d.getType())
                .actions(Dialog.Actions.YES, Dialog.Actions.NO)
                .showConfirm();

        if (response == Dialog.Actions.YES) {
            boolean success;
            for (Capability cp : d.getCapabilities()) {
                success = fileHandler.removeCapabilityFile(cp);
            }
            success = fileHandler.removeDeviceFile(d);
            if (success) {
                int idx = devList.indexOf(d);
                devList.remove(idx);
                devSelect.getItems().remove(idx);

                showSuccessMessage("Device Removed", "Device removed successfully!");
            }
        }
    }

    private boolean validateImgDimension(Image img, boolean isDevice) {
        String ERROR_TITLE = "Invalid Image Dimensions";

        double height = img.getHeight();
        double width = img.getWidth();
        boolean retVal = true;

        if (isDevice) {
            retVal = (height == 30);
            retVal = (width == 40);
            if (!retVal) {
                showErrorMessage(ERROR_TITLE, "Image dimensions should be 40x30");
            }
        } else {
            String capType = capTypeSelect.getValue();
            int idx = Arrays.asList(capTypes).indexOf(capType);

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
        }
        return retVal;

    }

    private void fillCapForm(Capability cap) {
        clearCapForm();
        isNewCap = false;

        capIdTextBox.setText(cap.getCapID().substring(4));
        capIdTextBox.setDisable(true);

        capNameTextBox.setText(cap.getCapName(Locale.US));
        capTypeSelect.getSelectionModel().select(cap.getType());

        capHasTest.setSelected(cap.isHasTest());
        capTestTextBox.setText(cap.getTestCommand());

        capStaticImg = new File(SifebUtil.STATIC_IMG_DIR + cap.getCapID() + ".png");
        if (capStaticImg.exists()) {
            capStaticImgView.setImage(cap.getStaticImage());
        } else {
            capStaticImg = null;
        }
        capDynamicImg = new File(SifebUtil.DYNAMIC_IMG_DIR + cap.getCapID() + ".gif");
        if (capDynamicImg.exists()) {
            capDynamicImgView.setImage(cap.getDynamicImage());
        } else {
            capDynamicImg = null;
        }

    }

    private void clearCapForm() {
        devSelect.getSelectionModel().clearSelection();
        capIdTextBox.setText("");
        capNameTextBox.setText("");
        capTypeSelect.getSelectionModel().selectFirst();

        capHasTest.setSelected(false);
        capTestTextBox.setText("");

        capCmdTextBox.setText("");
        capStopCmdTextBox.setText("");
        capRespSizeTextBox.setText("");
        capRefValTextBox.setText("");

        compTypeSelect.getSelectionModel().selectFirst();

        capStaticImg = null;
        capStaticImgView.setImage(null);

        capDynamicImg = null;
        capDynamicImgView.setImage(null);
        isNewCap = true;
    }

    private void saveCapability(boolean isNewEntry) {

        String ERROR_TITLE = "Invalid Input";

        int devIdx = devSelect.getSelectionModel().getSelectedIndex();
        if (devIdx == -1) {
            showErrorMessage(ERROR_TITLE, "Please select a device");
            return;
        }

        String capID = capIdTextBox.getText();
        String capName = capNameTextBox.getText();
        String capType = capTypeSelect.getValue();
        boolean hasTest = capHasTest.isSelected();
        String capTestCmd = "";
        if (hasTest) {
            capTestCmd = capTestTextBox.getText();
        }
        String capCmd = capCmdTextBox.getText();
        String capStopCmd = capStopCmdTextBox.getText();
        String capRespSize = capRespSizeTextBox.getText();
        String capRefVal = capRefValTextBox.getText();
        int compTypeIdx = compTypeSelect.getSelectionModel().getSelectedIndex();
        String compType = "";

        switch (compTypeIdx) {
            case 0:
                compType = "<";
                break;
            case 1:
                compType = "=";
                break;
            case 2:
                compType = ">";
                break;
        }

        if (!capType.equals(Capability.CAP_ACTION_C)) {
            capStopCmd = "";
        }
        if (!capType.equals(Capability.CAP_SENSE)) {
            capRefVal = "";
        }
        if (!(capType.equals(Capability.CAP_SENSE) || capType.equals(Capability.CAP_CONDITION))) {
            capRespSize = "";
            compType = "";
        }

        if ((capName == null) || (capName.isEmpty())) {
            showErrorMessage(ERROR_TITLE, "Please enter a valid capability name");
            return;
        }
        if (hasTest && capTestCmd.isEmpty()) {
            showErrorMessage(ERROR_TITLE, "Please enter a valid test command or untick \'Has TEST Button\'");
            return;
        }

        if (capCmd.isEmpty()) {
            showErrorMessage(ERROR_TITLE, "Please enter a valid command");
            return;
        }
        if (capType.equals(Capability.CAP_ACTION_C) && (capStopCmd.isEmpty())) {
            showErrorMessage(ERROR_TITLE, "Please enter a valid termination command");
            return;
        }
        if (capType.equals(Capability.CAP_SENSE) && (capRefVal.isEmpty())) {
            showErrorMessage(ERROR_TITLE, "Please enter a valid reference value");
            return;
        }
        if ((capType.equals(Capability.CAP_SENSE) || capType.equals(Capability.CAP_CONDITION)) && (capRespSize.isEmpty())) {
            showErrorMessage(ERROR_TITLE, "Please enter a valid response size");
            return;
        }

        boolean imgValid;
        if (capStaticImg == null) {
            showErrorMessage(ERROR_TITLE, "Please select a .png image for static image");
            return;
        } else {
            imgValid = validateImgDimension(capStaticImgView.getImage(), false);
        }

        if (capDynamicImg != null) {
            imgValid = validateImgDimension(capDynamicImgView.getImage(), false);
        }

        if (!imgValid) {
            return;
        }

        setCapWait(true);
        String capIDFull = "cap_" + capID;
        Map<Locale, String> names = new HashMap<>();

        try {
            //uploading images to the relevant directories
            Files.copy(capStaticImg.toPath(), new File(SifebUtil.STATIC_IMG_DIR + capIDFull + ".png").toPath(), StandardCopyOption.REPLACE_EXISTING);
            if (capDynamicImg != null) {
                Files.copy(capDynamicImg.toPath(), new File(SifebUtil.DYNAMIC_IMG_DIR + capIDFull + ".gif").toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            names.put(Locale.US, capName);
            Capability newCap = new Capability(capIDFull, names, null, capType, capTestCmd, capCmd, capStopCmd, compType, capRespSize, capRefVal, capIDFull, hasTest);
            boolean isSuccess = fileHandler.writeToCapabilityFile(newCap);

            if (isNewEntry) {
                Device d = devList.get(devIdx);
                d.addCapability(newCap);
                isSuccess = fileHandler.writeToDeviceFile(d);
            }

            if (isSuccess) {
                showSuccessMessage("Success", "Capability saved successfully!");
                clearCapForm();
            } else {
                showErrorMessage("Error", "Capability not saved. Please try again.");
            }

        } catch (IOException ex) {
            Logger.getLogger(LibraryEditorController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            setCapWait(false);
        }
    }

    private void setCapWait(boolean status) {
        capGrid.setDisable(status);
        capSaveBtn.setDisable(status);
        capClrBtn.setDisable(status);
        capWait.setVisible(status);
    }

    private void setDevWait(boolean status) {
        devGrid.setDisable(status);
        devSaveBtn.setDisable(status);
        devClrBtn.setDisable(status);
        devWait.setVisible(status);
    }

    private void saveDevice(boolean isNewEntry) {

        String ERROR_TITLE = "Invalid Input";

        String devID = devIdTextBox.getText();
        String devName = devNameTextBox.getText();
        String devType = devTypeSelect.getValue();

        if (isNewEntry) {
            if ((devID == null) || (devID.isEmpty())) {
                showErrorMessage(ERROR_TITLE, "Please enter a valid device ID");
                return;
            } else {
                for (Device d : devList) {
                    if (d.getDeviceID().equals("dev_" + devID)) {
                        showErrorMessage(ERROR_TITLE, "Device ID is already existing");
                        return;
                    }
                }
            }
        }
        if ((devName == null) || (devName.isEmpty())) {
            showErrorMessage(ERROR_TITLE, "Please enter a valid device name");
            return;
        }

        boolean imgValid = true;
        if (devImg == null) {
            showErrorMessage(ERROR_TITLE, "Please select a .png image for device image");
            return;
        } else {
            imgValid = validateImgDimension(devImgView.getImage(), true);
        }

        if (!imgValid) {
            return;
        }

        setDevWait(true);
        String devIDFull = "dev_" + devID;
        Map<Locale, String> names = new HashMap<>();

        try {
            //uploading images to the relevant directories
            Files.copy(devImg.toPath(), new File(SifebUtil.DEVICE_IMG_DIR + devIDFull + ".png").toPath(), StandardCopyOption.REPLACE_EXISTING);
            names.put(Locale.US, devName);

            Device newDev = new Device(devIDFull, names, 0, devType, devIDFull);
            if (!isNewEntry) {
                for (Device d : devList) {
                    if (d.getDeviceID().equals(devIDFull)) {
                        newDev.addCapabilities(d.getCapabilities());
                        int id = devList.indexOf(d);
                        devList.remove(id);
                        devSelect.getItems().remove(id);
                        break;
                    }
                }
            }

            boolean isSuccess = fileHandler.writeToDeviceFile(newDev);

            if (isSuccess) {
                devList.add(newDev);
                devSelect.getItems().add(newDev.toString());
                showSuccessMessage("Success", "Device saved successfully!");
                clearDevForm();
            } else {
                showErrorMessage("Error", "Device not saved. Please try again.");
            }

        } catch (IOException ex) {
            Logger.getLogger(LibraryEditorController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            setDevWait(false);
        }
    }

    private void clearDevForm() {
        devIdTextBox.setText("");
        devIdTextBox.setEditable(true);

        devNameTextBox.setText("");
        devTypeSelect.getSelectionModel().selectFirst();

        devImg = null;
        devImgView.setImage(null);
        isNewDev = true;
    }

    private void showErrorMessage(String title, String message) {
        Dialogs.create()
                .title(title)
                .masthead(title)
                .message(message)
                .showError();
    }

    private void showSuccessMessage(String title, String message) {
        Dialogs.create()
                .title(title)
                .masthead(title)
                .message(message)
                .showInformation();
    }

    private void showCapSelection(boolean newDev) {
        Dialog dlg = new Dialog(null, "Select Capabilities");
        dlg.setResizable(false);
        dlg.setMasthead("Select capabilities for the device");

        VBox capCheckList = new VBox();
        capCheckList.setSpacing(5);

        for (int i = 0; i < selectedCaps.size(); i++) {
            SelectedEntry se = selectedCaps.get(i);
            CheckBox cb = new CheckBox(se.getName());
            cb.setId(Integer.toString(i));
            cb.setSelected(se.isSelected());
            cb.setMnemonicParsing(false);
            capCheckList.getChildren().add(cb);
        }

        dlg.setContent(capCheckList);

        Action addNewCap = new AbstractAction("Add new ...") {

            @Override
            public void handle(ActionEvent event) {
                Dialog d = (Dialog) event.getSource();
                libTabPane.getSelectionModel().select(capabilityTab);
                d.hide();
            }

        };

        dlg.getActions().addAll(addNewCap, Dialog.Actions.OK, Dialog.Actions.CANCEL);
        Action act = dlg.show();
        if (act == Dialog.Actions.OK) {
            ObservableList cbs = capCheckList.getChildren();
            for (Object cbo : cbs) {
                CheckBox cb = (CheckBox) cbo;
                int id = Integer.parseInt(cb.getId());
                SelectedEntry se = selectedCaps.get(id);
                se.setSelected(cb.isSelected());
            }
        }
    }

    private void fillDevForm(Device dev) {
        clearDevForm();
        isNewDev = false;

        devIdTextBox.setText(dev.getDeviceID().substring(4));
        devIdTextBox.setDisable(true);

        devNameTextBox.setText(dev.getDeviceName(Locale.US));
        devTypeSelect.getSelectionModel().select(dev.getType());

//        devCapIds = new String[dev.getCapabilities().size()];
//        for (int i = 0; i < dev.getCapabilities().size(); i++) {
//            devCapIds[i] = dev.getCapabilities().get(i).getCapID();
//        }
//        refreshSelectedCaps();
        devImg = new File(SifebUtil.DEVICE_IMG_DIR + dev.getDeviceID() + ".png");
        if (devImg.exists()) {
            devImgView.setImage(dev.getImage());
        } else {
            devImg = null;
        }
    }

    @FXML
    private void goToLibEditor(ActionEvent event) {
    }

    @FXML
    private void goToHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(MainApp.HomeFile));
            MainApp.setPane((Pane) loader.load());
            Scene scene = new Scene(MainApp.getPane());
            MainApp.getStage().setScene(scene);
            MainApp.getStage().setMaximized(false);
            MainApp.getStage().setResizable(false);
            MainApp.getStage().setWidth(800);
            MainApp.getStage().setHeight(600);
            MainApp.getStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ComboEntry {

    private final String id;
    private final String name;

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

class SelectedEntry {

    private String id;
    private String name;
    private boolean selected;

    public SelectedEntry(String id, String name) {
        this.id = id;
        this.name = name;
        this.selected = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
