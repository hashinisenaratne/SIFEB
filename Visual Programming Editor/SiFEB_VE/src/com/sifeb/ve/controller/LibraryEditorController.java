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
import java.text.ParseException;
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
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
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
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
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
    TextField capName2TextBox;
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
    TextField devName2TextBox;
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
        "== (Response Value == Reference Value)",
        "!= (Response Value != Reference Value)",
        "< (Response Value < Reference Value)",
        "<= (Response Value <= Reference Value)",
        "> (Response Value > Reference Value)",
        ">= (Response Value >= Reference Value)"
    };
    FileHandler fileHandler;
    File capStaticImg;
    File capDynamicImg;
    File devImg;
    ArrayList<Device> devList;
    boolean isNewCap;
    boolean isNewDev;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        fileHandler = new FileHandler();
        setEventHandlers();
        setInputHandlers();

        capTypeSelect.getItems().addAll(capTypes);
        capTypeSelect.getSelectionModel().selectFirst();

        devTypeSelect.getItems().addAll(devTypes);
        devTypeSelect.getSelectionModel().selectFirst();

        compTypeSelect.getItems().addAll(compTypes);
        compTypeSelect.getSelectionModel().selectFirst();

        devList = new ArrayList<>();
        isNewCap = true;
        isNewDev = true;

        refreshDevList();
    }

    private void refreshDevList() {
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
    }

    private void setInputHandlers() {
        capTestTextBox.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (capTestTextBox.getText().length() >= 1) {
                        capTestTextBox.setText(capTestTextBox.getText().substring(0, 1));
                    }
                }
            }
        });
        capCmdTextBox.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (capCmdTextBox.getText().length() >= 1) {
                        capCmdTextBox.setText(capCmdTextBox.getText().substring(0, 1));
                    }
                }
            }
        });
        capStopCmdTextBox.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (capStopCmdTextBox.getText().length() >= 1) {
                        capStopCmdTextBox.setText(capStopCmdTextBox.getText().substring(0, 1));
                    }
                }
            }
        });
    }

    private void setEventHandlers() {
        capEditBtn.setOnAction((event) -> {
            Capability cp = showCapSelector("Select Capability to edit");
            if (cp != null) {
                fillCapForm(cp);
            }
        });

        capDelBtn.setOnAction((event) -> {
            Capability cp = showCapSelector("Select Capability to remove");
            if (cp != null) {
                removeCapability(cp);
            }
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

        if (response == Dialog.Actions.YES) {
            boolean success;
            success = fileHandler.removeCapabilityFile(cap);

            if (success) {
                Device d = cap.getDevice();
                d.removeCapability(cap);
                fileHandler.writeToDeviceFile(d);
                showSuccessMessage("Capability Removed", "Capability removed successfully!");
            }
        }
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

        int devIdx = devList.indexOf(cap.getDevice());
        devSelect.getSelectionModel().select(devIdx);
        devSelect.setDisable(true);
        capIdTextBox.setText(cap.getCapID().substring(4));
        capIdTextBox.setDisable(true);

        capNameTextBox.setText(cap.getCapName(Locale.US));
        capName2TextBox.setText(cap.getCapName(new Locale("si", "LK")));
        capTypeSelect.getSelectionModel().select(cap.getType());

        capHasTest.setSelected(cap.isHasTest());
        capTestTextBox.setText(cap.getTestCommand());

        capCmdTextBox.setText(cap.getExeCommand());
        capStopCmdTextBox.setText(cap.getStopCommand());
        capRefValTextBox.setText(cap.getRefValue());
        capRespSizeTextBox.setText(cap.getRespSize());

        String compType = cap.getCompType();
        switch (compType) {
            case "=":
                compTypeSelect.getSelectionModel().select(0);
                break;
            case "!":
                compTypeSelect.getSelectionModel().select(1);
                break;
            case "<":
                compTypeSelect.getSelectionModel().select(2);
                break;
            case ",":
                compTypeSelect.getSelectionModel().select(3);
                break;
            case ">":
                compTypeSelect.getSelectionModel().select(4);
                break;
            case ".":
                compTypeSelect.getSelectionModel().select(5);
                break;
        }

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
        devSelect.setDisable(false);
        capIdTextBox.setText("");
        capNameTextBox.setText("");
        capName2TextBox.setText("");
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
        String capName2 = capName2TextBox.getText();
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
                compType = "=";
                break;
            case 1:
                compType = "!";
                break;
            case 2:
                compType = "<";
                break;
            case 3:
                compType = ",";
                break;
            case 4:
                compType = ">";
                break;
            case 5:
                compType = ".";
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

        if (capType.equals(Capability.CAP_SENSE) || capType.equals(Capability.CAP_CONDITION)) {
            if (capRespSize.isEmpty()) {
                showErrorMessage(ERROR_TITLE, "Please enter a valid response size");
                return;
            } else {
                try {
                    int respVal = Short.parseShort(capRespSize);
                    if (respVal < 0) {
                        showErrorMessage(ERROR_TITLE, "Response size should be positive");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    showErrorMessage(ERROR_TITLE, "Response size should be a 16-bit integer");
                    return;
                }
            }
        }
        if (capType.equals(Capability.CAP_SENSE)) {
            if (capRefVal.isEmpty()) {
                showErrorMessage(ERROR_TITLE, "Please enter a valid reference value");
                return;
            } else {
                try {
                    int refVal = Integer.parseInt(capRefVal);
                } catch (NumberFormatException ex) {
                    showErrorMessage(ERROR_TITLE, "Reference value should be an integer");
                    return;
                }
            }

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
            if (capName2.isEmpty()) {
                names.put(new Locale("si", "LK"), capName);
            } else {
                names.put(new Locale("si", "LK"), capName2);
            }
            Capability newCap = new Capability(capIDFull, names, null, capType, capTestCmd, capCmd, capStopCmd, compType, capRespSize, capRefVal, capIDFull, hasTest);
            boolean isSuccess = fileHandler.writeToCapabilityFile(newCap);

            Device d = devList.get(devIdx);
            d.addCapability(newCap);
            newCap.setDevice(d);
            if (isNewEntry) {               
                isSuccess = fileHandler.writeToDeviceFile(d);
            } else {                
                for (Capability cp : d.getCapabilities()) {
                    if (cp.getCapID().equals(newCap.getCapID())) {
                        d.removeCapability(cp);
                        break;
                    }
                }
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

    private Capability showCapSelector(String message) {
        Dialog dlg = new Dialog(libTabPane.getScene().getWindow(), "Select Capability", true);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        ChoiceBox<Device> devs = new ChoiceBox<>();
        ChoiceBox<Capability> caps = new ChoiceBox<>();
        devs.getItems().addAll(devList);
        devs.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                caps.getItems().clear();
                for (Capability cp : devs.getItems().get((int) newValue).getCapabilities()) {
                    caps.getItems().add(cp);
                }
                caps.getSelectionModel().selectFirst();
            }

        });
        devs.getSelectionModel().selectFirst();

        grid.add(new Label("Device"), 0, 0);
        grid.add(devs, 1, 0);
        grid.add(new Label("Capability"), 0, 1);
        grid.add(caps, 1, 1);

        dlg.setMasthead(message);
        dlg.setContent(grid);
        dlg.getActions().addAll(Dialog.Actions.OK, Dialog.Actions.CANCEL);

        Action response = dlg.show();

        if (response == Dialog.Actions.OK) {
            return caps.getSelectionModel().getSelectedItem();
        }

        return null;
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
        String devName2 = devName2TextBox.getText();
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
            if (devName2.isEmpty()) {
                names.put(new Locale("si", "LK"), devName);
            } else {
                names.put(new Locale("si", "LK"), devName2);
            }

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
        devName2TextBox.setText("");
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

    private void fillDevForm(Device dev) {
        clearDevForm();
        isNewDev = false;

        devIdTextBox.setText(dev.getDeviceID().substring(4));
        devIdTextBox.setDisable(true);

        devNameTextBox.setText(dev.getDeviceName(Locale.US));
        devName2TextBox.setText(dev.getDeviceName(new Locale("si", "LK")));
        devTypeSelect.getSelectionModel().select(dev.getType());

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
