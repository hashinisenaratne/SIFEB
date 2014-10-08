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

        //  writeToFile(new File("output.xml"));
        // readDeviceFile(new File("output.xml"));
        writeToDeviceFile(new File("dev_12.xml"));
        writeToCapabilityFile(new File("cap_012.xml"));
      //  Element el = readFromDeviceFile(new File("device1.xml"));
      //  Element e2 = readFromCapabilityFile(new File("capability-001.xml"));
        
        BlockCreator blockC = new BlockCreator();
        blockC.createBlock("12");
        //System.out.println("++ " + el.getElementsByTagName("Names"));
    }

    public static void writeToDeviceFile(File file) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Sifeb");
            doc.appendChild(rootElement);

            // staff elements
            Element device = doc.createElement("Device");
            rootElement.appendChild(device);

            // set attribute to staff element
//            Attr attr = doc.createAttribute("id");
//            attr.setValue("1");
//            staff.setAttributeNode(attr);
            // shorten way
            // staff.setAttribute("id", "1");
            // firstname elements
            Element firstname = doc.createElement("Id");
            firstname.appendChild(doc.createTextNode("1234"));
            device.appendChild(firstname);

            // lastname elements
            Element name = doc.createElement("Names");
            device.appendChild(name);

            Element usName = doc.createElement("en_US");
            usName.appendChild(doc.createTextNode("go forward"));
            name.appendChild(usName);

            Element lkName = doc.createElement("si_LK");
            lkName.appendChild(doc.createTextNode("go forward"));
            name.appendChild(lkName);
          //  lastname.appendChild(doc.createTextNode("im"));

            // nickname elements
            Element address = doc.createElement("Address");
            address.appendChild(doc.createTextNode("12"));
            device.appendChild(address);

            // salary elements
            Element type = doc.createElement("Type");
            type.appendChild(doc.createTextNode("type"));
            device.appendChild(type);

            Element image = doc.createElement("Image");
            image.appendChild(doc.createTextNode("imageName"));
            device.appendChild(image);
            

//            Element staticImage = doc.createElement("static_Image");
//            staticImage.appendChild(doc.createTextNode("ImageName"));
//            images.appendChild(staticImage);
//
//            Element dyncImage = doc.createElement("dync_Image");
//            dyncImage.appendChild(doc.createTextNode("img name"));
//            images.appendChild(dyncImage);

            Element capabilities = doc.createElement("Capabilities");
            device.appendChild(capabilities);

            Element cap1 = doc.createElement("capability_1");
            cap1.appendChild(doc.createTextNode("cap_012"));
            capabilities.appendChild(cap1);

//            Element cap2 = doc.createElement("capability_2");
//            cap2.appendChild(doc.createTextNode("cap-0034"));
//            capabilities.appendChild(cap2);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);            //new File("C:\\file.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    public static Element readFromDeviceFile(File fXmlFile) {

        Element eElement = null;

        try {

            //File fXmlFile = new File("/Users/mkyong/staff.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

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

    public static void writeToCapabilityFile(File file) {
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
            firstname.appendChild(doc.createTextNode("1234"));
            device.appendChild(firstname);

            Element name = doc.createElement("Names");
            device.appendChild(name);

            Element usName = doc.createElement("en_US");
            usName.appendChild(doc.createTextNode("go forward"));
            name.appendChild(usName);

            Element lkName = doc.createElement("si_LK");
            lkName.appendChild(doc.createTextNode("go forward"));
            name.appendChild(lkName);

            Element type = doc.createElement("Type");
            type.appendChild(doc.createTextNode("type"));
            device.appendChild(type);

            Element address = doc.createElement("Command");
            address.appendChild(doc.createTextNode("a"));
            device.appendChild(address);

            Element image = doc.createElement("Image");
            image.appendChild(doc.createTextNode("ImageName"));
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
            StreamResult result = new StreamResult(file);            //new File("C:\\file.xml"));

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    public static Element readFromCapabilityFile(File fXmlFile) {

        Element eElement = null;

        try {

            //File fXmlFile = new File("/Users/mkyong/staff.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

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
