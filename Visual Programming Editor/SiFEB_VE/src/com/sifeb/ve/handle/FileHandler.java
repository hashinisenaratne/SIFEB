/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.handle;

/**
 *
 * @author Pubudu
 */
import com.sifeb.ve.Capability;
import com.sifeb.ve.Device;
import com.sifeb.ve.FeedBackLogger;
import com.sifeb.ve.GameList;
import com.sifeb.ve.resources.SifebUtil;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FileHandler {

//    private final String CAPABILITY_FOLDER = "src/com/sifeb/ve/files/capabilities/";
//    private final String DEVICE_FOLDER = "src/com/sifeb/ve/files/devices/";
    public static void main(String[] args) {

        FileHandler fh = new FileHandler();
//        fh.generateTestDeviceFiles();
//        fh.generateTestCapabilityFiles();
//        fh.readFromCapabilityFile("cap_001");
        fh.writeToGameFile("game_001");
//        Element d=fh.readFromGameFile("game_001");
//        System.out.println(d.getElementsByTagName("Id").item(0).getTextContent());

        //fh.writeToDeviceFile("dev_12");
    }

    public boolean writeToDeviceFile(String devID, Map<Locale, String> devNames, String devType, String[] devCaps, String imgName) {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Sifeb");
            doc.appendChild(rootElement);
            Element device = doc.createElement("Device");
            rootElement.appendChild(device);
            Element id = doc.createElement("Id");
            id.appendChild(doc.createTextNode(devID));
            device.appendChild(id);
            Element names = doc.createElement("Names");
            device.appendChild(names);

            for (Map.Entry<Locale, String> entry : devNames.entrySet()) {
                Element name = doc.createElement("Name");
                Element locale = doc.createElement("Locale");
                Element nameStr = doc.createElement("Value");

                locale.appendChild(doc.createTextNode(entry.getKey().toString()));
                nameStr.appendChild(doc.createTextNode(entry.getValue()));

                name.appendChild(locale);
                name.appendChild(nameStr);
                names.appendChild(name);
            }

            Element type = doc.createElement("Type");
            type.appendChild(doc.createTextNode(devType));
            device.appendChild(type);
            Element image = doc.createElement("Image");
            image.appendChild(doc.createTextNode(imgName));
            device.appendChild(image);
            Element capabilities = doc.createElement("Capabilities");
            device.appendChild(capabilities);

            for (int j = 0; j < devCaps.length; j++) {
                Element cap = doc.createElement("capability");
                cap.appendChild(doc.createTextNode(devCaps[j]));
                capabilities.appendChild(cap);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File file = new File(SifebUtil.DEV_FILE_DIR + devID + ".xml");
            StreamResult result = new StreamResult(file);            
            transformer.transform(source, result);

            return true;

        } catch (ParserConfigurationException | TransformerException pce) {
            return false;
        }
    }

    public Device readFromDeviceFile(String fileName, String address) {

        Element eElement = null;
        File file = new File(SifebUtil.DEV_FILE_DIR + fileName + ".xml");
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("Device");
            Node nNode = nList.item(0);
            eElement = (Element) nNode;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return getDevFromElement(eElement, address);
    }

    private Device getDevFromElement(Element devElement, String address) {

        String devId = devElement.getElementsByTagName("Id").item(0).getTextContent();
        NodeList nodeList = devElement.getElementsByTagName("Names").item(0).getChildNodes();
        Map<Locale, String> actNames = new HashMap<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            NodeList nameNodes = nodeList.item(i).getChildNodes();
            String locale = nameNodes.item(0).getTextContent();
            String name = nameNodes.item(1).getTextContent();
            actNames.put(new Locale(locale.split("_")[0], locale.split("_")[1]), name);
        }

        String type = devElement.getElementsByTagName("Type").item(0).getTextContent();
        String image = devElement.getElementsByTagName("Image").item(0).getTextContent();
        // create device
        Device device = new Device(devId, actNames, Integer.parseInt(address), type, image);

        NodeList capList = devElement.getElementsByTagName("Capabilities").item(0).getChildNodes();

        for (int j = 0; j < capList.getLength(); j++) {
            String capId = capList.item(j).getTextContent();
            Capability cap = readFromCapabilityFile(capId);
            cap.setDevice(device);
            device.addCapability(cap);
        }
        return device;
    }

    public boolean writeToCapabilityFile(String capID, Map<Locale, String> capNames, String capType, String command, String imageName, boolean hasTest) {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Sifeb");
            doc.appendChild(rootElement);
            Element capability = doc.createElement("Capability");
            rootElement.appendChild(capability);
            Element id = doc.createElement("Id");
            id.appendChild(doc.createTextNode(capID));
            capability.appendChild(id);
            Element names = doc.createElement("Names");
            capability.appendChild(names);

            for (Map.Entry<Locale, String> entry : capNames.entrySet()) {
                Element name = doc.createElement("Name");
                Element locale = doc.createElement("Locale");
                Element nameStr = doc.createElement("Value");
                locale.appendChild(doc.createTextNode(entry.getKey().toString()));
                nameStr.appendChild(doc.createTextNode(entry.getValue()));
                name.appendChild(locale);
                name.appendChild(nameStr);
                names.appendChild(name);
            }

            Element type = doc.createElement("Type");
            type.appendChild(doc.createTextNode(capType));
            capability.appendChild(type);
            Element cmd = doc.createElement("Command");
            cmd.appendChild(doc.createTextNode(command));
            capability.appendChild(cmd);

            Element button = doc.createElement("HasTestButton");
            button.appendChild(doc.createTextNode(Boolean.toString(hasTest)));
            capability.appendChild(button);
            Element image = doc.createElement("Image");
            image.appendChild(doc.createTextNode(imageName));
            capability.appendChild(image);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File file = new File(SifebUtil.CAP_FILE_DIR + capID + ".xml");
            StreamResult result = new StreamResult(file);           
            transformer.transform(source, result);
            return true;

        } catch (ParserConfigurationException | TransformerException pce) {
            return false;
        }
    }

    public Capability readFromCapabilityFile(String capID) {

        Element eElement = null;
        File file = new File(SifebUtil.CAP_FILE_DIR + capID + ".xml");
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Capability");
            Node nNode = nList.item(0);
            eElement = (Element) nNode;

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return getCapFromElement(eElement);
    }

    private Capability getCapFromElement(Element el) {
        String capId = el.getElementsByTagName("Id").item(0).getTextContent();
        NodeList nodeList = el.getElementsByTagName("Names").item(0).getChildNodes();
        Map<Locale, String> actNames = new HashMap<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            NodeList nameNodes = nodeList.item(i).getChildNodes();
            String locale = nameNodes.item(0).getTextContent();
            String name = nameNodes.item(1).getTextContent();
            actNames.put(new Locale(locale.split("_")[0], locale.split("_")[1]), name);
        }
        String command = el.getElementsByTagName("Command").item(0).getTextContent();
        String type = el.getElementsByTagName("Type").item(0).getTextContent();
        String image = el.getElementsByTagName("Image").item(0).getTextContent();
        boolean hasTestButton = Boolean.parseBoolean(el.getElementsByTagName("HasTestButton").item(0).getTextContent());
        Capability cap = new Capability(capId, actNames, null, type, command, image, hasTestButton);

        return cap;
    }

    public void writeToGameFile(String fileName) {
        
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Sifeb");
            doc.appendChild(rootElement);
            Element game = doc.createElement("Game");
            rootElement.appendChild(game);

            Element id = doc.createElement("Id");
            id.appendChild(doc.createTextNode("001"));
            game.appendChild(id);
            Element stories = doc.createElement("Stories");
            game.appendChild(stories);

            for (int i = 1; i < 10; i++) {
                Element cap1 = doc.createElement("story");
                Element image = doc.createElement("Image");
                image.appendChild(doc.createTextNode("Mwheels"));
                Element text = doc.createElement("Text");
                text.appendChild(doc.createTextNode("STEP " + i + ":\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eu."));
                cap1.appendChild(image);
                cap1.appendChild(text);
                stories.appendChild(cap1);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File file = new File(SifebUtil.GAME_FILE_DIR + fileName + ".xml");
            StreamResult result = new StreamResult(file);            
            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }

    }

    public Element readFromGameFile(String fileName) {

        Element element = null;
        File file = new File(SifebUtil.GAME_FILE_DIR + fileName + ".xml");

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("Game");
            Node nNode = nList.item(0);
            element = (Element) nNode;

        } catch (Exception e) {
            element = null;
            e.printStackTrace();
        }

        return element;
    }

    public void writeToEditorFile(String filePath, Document doc) {

        boolean success = true;
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File file = new File(filePath);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

        } catch (TransformerException pce) {
            success = false;
            pce.printStackTrace();
        } finally {

            if (success) {
                FeedBackLogger.sendGoodMessage("File Saved Successfully!!!");
            } else {
                FeedBackLogger.sendBadMessage("File Didn't Saved!!");
            }

        }

    }

    public Element readFromEditorFile(String filePath) {
        Element element = null;
        File file = new File(filePath);

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("MainEditor");
            Node nNode = nList.item(0);
            element = (Element) nNode;

        } catch (Exception e) {
            element = null;
            e.printStackTrace();
        }

        return element;
    }

    public GameList readFromGameListFile(int level) {

        Element eElement = null;
        File file = new File(SifebUtil.GAME_FILE_DIR + "game_list.xml");
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            //optional, but recommended
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Level" + level);
            Node nNode = nList.item(0);
            eElement = (Element) nNode;

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return getGameListFromElement(eElement, level);
    }

    private GameList getGameListFromElement(Element el, int level) {
        NodeList nodeList = el.getElementsByTagName("Games").item(0).getChildNodes();
        GameList gamelist = new GameList(level);

        for (int i = 0; i < nodeList.getLength(); i++) {
            NodeList nameNodes = nodeList.item(i).getChildNodes();
            String id = nameNodes.item(0).getTextContent();
            String file = nameNodes.item(1).getTextContent();
            gamelist.addGame(id, file);
        }

        return gamelist;
    }

    /////////////////////////////////////////////////////////////
    // Following methods are used to generate sample xml files //
    /////////////////////////////////////////////////////////////
    private void generateTestCapabilityFiles() {

        String[] ids = {"cap_001", "cap_002", "cap_003", "cap_004", "cap_005", "cap_006", "cap_007", "cap_008", "cap_009"};
        String[] actionNames_en = {"Go Forward", "Reverse", "Turn Left", "Turn Right", "Stop", "Light ON", "Light OFF", "No Object", "See Object"};
        String[] actionNames_si = {"ඉදිරියට යන්න", "පසුපසට යන්න", "වමට හැරෙන්න", "දකුණට හැරෙන්න", "නවතින්න", "Light ON", "Light OFF", "No Object", "See Object"};
        String[] actionCmd = {"b", "c", "e", "d", "", "l", "", "", ""};
        String[] buttonList = {"true", "true", "true", "true", "false", "true", "false", "false", "false"};
        //String[] imageList = {"forward", "reverse", "left", "right", "stop", "true", "false"};

        for (int i = 0; i < 9; i++) {

            String types;

            if (i >= 7) {
                types = Capability.CAP_SENSE;
            } else if (i < 2 || i > 5) {
                types = Capability.CAP_ACTION_C;
            } else {
                types = Capability.CAP_ACTION;
            }

            Map<String, String> nameList = new HashMap<>();
            nameList.put("en_US", actionNames_en[i]);
            nameList.put("si_LK", actionNames_si[i]);

            try {

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                // root elements
                Document doc = docBuilder.newDocument();
                Element rootElement = doc.createElement("Sifeb");
                doc.appendChild(rootElement);

                Element capability = doc.createElement("Capability");
                rootElement.appendChild(capability);

                Element id = doc.createElement("Id");
                id.appendChild(doc.createTextNode(ids[i]));
                capability.appendChild(id);

                Element names = doc.createElement("Names");
                capability.appendChild(names);

                for (Map.Entry<String, String> entry : nameList.entrySet()) {
                    Element name = doc.createElement("Name");
                    Element locale = doc.createElement("Locale");
                    Element nameStr = doc.createElement("Value");

                    locale.appendChild(doc.createTextNode(entry.getKey()));
                    nameStr.appendChild(doc.createTextNode(entry.getValue()));

                    name.appendChild(locale);
                    name.appendChild(nameStr);
                    names.appendChild(name);
                }

                Element type = doc.createElement("Type");
                type.appendChild(doc.createTextNode(types));
                capability.appendChild(type);

                Element cmd = doc.createElement("Command");
                cmd.appendChild(doc.createTextNode(actionCmd[i]));
                capability.appendChild(cmd);

                Element button = doc.createElement("HasTestButton");
                button.appendChild(doc.createTextNode(buttonList[i]));
                capability.appendChild(button);

                Element image = doc.createElement("Image");
                image.appendChild(doc.createTextNode(ids[i]));
                capability.appendChild(image);

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                File file = new File(SifebUtil.CAP_FILE_DIR + ids[i] + ".xml");
                StreamResult result = new StreamResult(file);            //new File("C:\\file.xml"));

                transformer.transform(source, result);

                System.out.println("File saved!");

            } catch (ParserConfigurationException | TransformerException pce) {
                pce.printStackTrace();
            }

        }
    }

    private void generateTestDeviceFiles() {
        String[] ids = {"dev_10", "dev_11", "dev_12"};
        String[] deviceNames_en = {"Wheels", "Sonar", "Light"};
        String[] deviceNames_si = {"රෝද", "අතිධ්වනි", "පහන්",};
        String[] types = {Device.DEV_ACTUATOR, Device.DEV_SENSOR, Device.DEV_ACTUATOR};
        String[][] caps = {{"cap_001", "cap_002", "cap_003", "cap_004", "cap_005"},
        {"cap_008", "cap_009"},
        {"cap_006", "cap_007"}
        };

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            for (int i = 0; i < 3; i++) {

                Map<String, String> nameList = new HashMap<>();
                nameList.put("en_US", deviceNames_en[i]);
                nameList.put("si_LK", deviceNames_si[i]);
                String[] cp = caps[i];

                // root elements
                Document doc = docBuilder.newDocument();
                Element rootElement = doc.createElement("Sifeb");
                doc.appendChild(rootElement);

                Element device = doc.createElement("Device");
                rootElement.appendChild(device);

                Element id = doc.createElement("Id");
                id.appendChild(doc.createTextNode(ids[i]));
                device.appendChild(id);

                Element names = doc.createElement("Names");
                device.appendChild(names);

                for (Map.Entry<String, String> entry : nameList.entrySet()) {
                    Element name = doc.createElement("Name");
                    Element locale = doc.createElement("Locale");
                    Element nameStr = doc.createElement("Value");

                    locale.appendChild(doc.createTextNode(entry.getKey()));
                    nameStr.appendChild(doc.createTextNode(entry.getValue()));

                    name.appendChild(locale);
                    name.appendChild(nameStr);
                    names.appendChild(name);
                }

                Element type = doc.createElement("Type");
                type.appendChild(doc.createTextNode(types[i]));
                device.appendChild(type);

                Element image = doc.createElement("Image");
                image.appendChild(doc.createTextNode(ids[i]));
                device.appendChild(image);

                Element capabilities = doc.createElement("Capabilities");
                device.appendChild(capabilities);

                for (int j = 0; j < cp.length; j++) {
                    Element cap = doc.createElement("capability");
                    cap.appendChild(doc.createTextNode(cp[j]));
                    capabilities.appendChild(cap);
                }
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                File file = new File(SifebUtil.DEV_FILE_DIR + ids[i] + ".xml");
                StreamResult result = new StreamResult(file);            //new File("C:\\file.xml"));

                // Output to console for testing
                // StreamResult result = new StreamResult(System.out);
                transformer.transform(source, result);

                System.out.println("File saved!");
            }
        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }
}
