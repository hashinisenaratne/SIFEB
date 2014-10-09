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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class FileHandler {

    public static void main(String[] args) {

        FileHandler fh = new FileHandler();
        fh.writeToDeviceFile("dev_12");
    }

    public void writeToDeviceFile(String fileName) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Sifeb");
            doc.appendChild(rootElement);

            Element device = doc.createElement("Device");
            rootElement.appendChild(device);

            Element id = doc.createElement("Id");
            id.appendChild(doc.createTextNode("1234"));
            device.appendChild(id);
            
            Element name = doc.createElement("Names");
            device.appendChild(name);

            Element usName = doc.createElement("en_US");
            usName.appendChild(doc.createTextNode("go forward"));
            name.appendChild(usName);

            Element lkName = doc.createElement("si_LK");
            lkName.appendChild(doc.createTextNode("go forward"));
            name.appendChild(lkName);
            
            Element address = doc.createElement("Address");
            address.appendChild(doc.createTextNode("12"));
            device.appendChild(address);

            Element type = doc.createElement("Type");
            type.appendChild(doc.createTextNode("type"));
            device.appendChild(type);

            Element image = doc.createElement("Image");
            image.appendChild(doc.createTextNode("Mwheels.png"));
            device.appendChild(image);

            Element capabilities = doc.createElement("Capabilities");
            device.appendChild(capabilities);

            // Element cap1 = doc.createElement("capability_1");
            for (int i = 1; i < 6; i++) {
                String capS = "capability_" + i;
                String capST = "cap_00" + i;
                Element cap1 = doc.createElement(capS);
                cap1.appendChild(doc.createTextNode(capST));
                capabilities.appendChild(cap1);
            }
            // cap1.appendChild(doc.createTextNode("cap_001"));
            //  capabilities.appendChild(cap1);

//            Element cap2 = doc.createElement("capability_2");
//            cap2.appendChild(doc.createTextNode("cap-0034"));
//            capabilities.appendChild(cap2);
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File file = new File("src/com/sifeb/ve/files/devices/" + fileName + ".xml");
            StreamResult result = new StreamResult(file);            //new File("C:\\file.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    public Element readFromDeviceFile(String fileName) {

        Element eElement = null;

        File file = new File("src/com/sifeb/ve/files/devices/" + fileName + ".xml");

        //  Image img=new Image()
        try {

            //File fXmlFile = new File("/Users/mkyong/staff.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            //optional, but recommended
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("Device");
            Node nNode = nList.item(0);
            eElement = (Element) nNode;
            System.out.println("----------------------------");

//            for (int temp = 0; temp < nList.getLength(); temp++) {
//
//                System.out.println("list " + nList.getLength());
//                Node nNode = nList.item(temp);
//
//                System.out.println("\nCurrent Element :" + nNode.getNodeName());
//
//                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//
//                    Element eElement = (Element) nNode;
//
////                    System.out.println("Staff id : " + eElement.getAttribute("id"));
////                    System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
////                    System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
////                    System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
////                    System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
//                }
//
//                // return 
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eElement;
    }

    public void writeToCapabilityFile() {

        String[] ids = {"cap_001", "cap_002", "cap_003", "cap_004", "cap_005", "cap_006", "cap_007"};
        String[] actionNames_en = {"Go Forward", "Reverse", "Turn Left", "Turn Right", "Stop", "Light ON", "Light OFF"};
        String[] actionNames_si = {"ඉදිරියට යන්න", "පසුපසට යන්න", "වමට හැරෙන්න", "දකුණට හැරෙන්න", "නවතින්න", "Light ON", "Light OFF"};
        String[] actionCmd = {"b", "c", "e", "d", "", "l", ""};
        String[] buttonList = {"true", "true", "true", "true", "false", "true", "false"};
        //String[] imageList = {"forward", "reverse", "left", "right", "stop", "true", "false"};

        for (int i = 0; i < 7; i++) {

            String types;

            if (i < 3 || i > 5) {
                types = "actionC";
            } else {
                types = "action";
            }

            try {

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                // root elements
                Document doc = docBuilder.newDocument();
                Element rootElement = doc.createElement("Sifeb");
                doc.appendChild(rootElement);

                Element device = doc.createElement("Capability");
                rootElement.appendChild(device);

                Element firstname = doc.createElement("Id");
                firstname.appendChild(doc.createTextNode(ids[i]));
                device.appendChild(firstname);

                Element name = doc.createElement("Names");
                device.appendChild(name);

                Element usName = doc.createElement("en_US");
                usName.appendChild(doc.createTextNode(actionNames_en[i]));
                name.appendChild(usName);

                Element lkName = doc.createElement("si_LK");
                lkName.appendChild(doc.createTextNode(actionNames_si[i]));
                name.appendChild(lkName);

                Element type = doc.createElement("Type");
                type.appendChild(doc.createTextNode(types));
                device.appendChild(type);

                Element address = doc.createElement("Command");
                address.appendChild(doc.createTextNode(actionCmd[i]));
                device.appendChild(address);

                Element button = doc.createElement("HasTestButton");
                button.appendChild(doc.createTextNode(buttonList[i]));
                device.appendChild(button);

                Element image = doc.createElement("Image");
                image.appendChild(doc.createTextNode("Action.png"));
                device.appendChild(image);

//            Element staticImage = doc.createElement("static_Image");
//            staticImage.appendChild(doc.createTextNode("ImageName"));
//            images.appendChild(staticImage);
//
//            Element dyncImage = doc.createElement("dync_Image");
//            dyncImage.appendChild(doc.createTextNode("img name"));
//            images.appendChild(dyncImage);
                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                File file = new File("src/com/sifeb/ve/files/capabilities/" + ids[i] + ".xml");
                StreamResult result = new StreamResult(file);            //new File("C:\\file.xml"));

                transformer.transform(source, result);

                System.out.println("File saved!");

            } catch (ParserConfigurationException | TransformerException pce) {
                pce.printStackTrace();
            }

        }
    }

    public Element readFromCapabilityFile(String fileName) {

        Element eElement = null;
        File file = new File("src/com/sifeb/ve/files/capabilities/" + fileName + ".xml");
        try {

            //File fXmlFile = new File("/Users/mkyong/staff.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            //optional, but recommended
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("Capability");
            Node nNode = nList.item(0);
            eElement = (Element) nNode;
            System.out.println("----------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return eElement;
    }

}
