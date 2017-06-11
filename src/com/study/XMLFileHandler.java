package com.study;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class XMLFileHandler {

    public static LocationsDataset loadDatasetFromXML(String path){
        LocationsDataset locationsDataset = new LocationsDataset();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {

                boolean blDatasetName = false;
                boolean blDimension = false;
                boolean blX = false;
                boolean blY = false;
                boolean blTagname = false;
                int index = 0;

                public void startElement(String uri, String localName,String qName,
                                         Attributes attributes) throws SAXException {

                    //System.out.println("Start Element :" + qName);

                    if (qName.equalsIgnoreCase("DATASETNAME")) {
                        blDatasetName = true;
                    }
                    if (qName.equalsIgnoreCase("DIMENSION")) {
                        blDimension = true;
                    }
                    if (qName.equalsIgnoreCase("X")) {
                        blX = true;
                    }
                    if (qName.equalsIgnoreCase("Y")) {
                        blY = true;
                    }
                    if (qName.equalsIgnoreCase("TAGNAME")) {
                        blTagname = true;
                    }
                }

                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {

                    //System.out.println("End Element :" + qName);

                }

                public void characters(char ch[], int start, int length) throws SAXException {
                    if (blDatasetName) {
                        //System.out.println("Dataset Name : " + new String(ch, start, length));
                        locationsDataset.setDatasetName(new String(ch, start, length));
                        blDatasetName = false;
                    }
                    if (blDimension) {
                        //System.out.println("Dimension : " + new String(ch, start, length));
                        locationsDataset.setDatasetCount(Integer.parseInt(new String(ch, start, length)));
                        blDimension = false;
                    }
                    if (blX) {
                        //System.out.println(index+". Location\nX : " + new String(ch, start, length));
                        locationsDataset.addLocation(new Location(index+1,Double.parseDouble(new String(ch, start, length)),0));
                        blX = false;
                    }
                    if (blY) {
                        //System.out.println("Y : " + new String(ch, start, length));
                        locationsDataset.getLocationsData().get(index).setY(Double.parseDouble(new String(ch, start, length)));
                        blY = false;
                    }
                    if (blTagname) {
                        //System.out.println("Tagname : " + new String(ch, start, length));
                        locationsDataset.getLocationsData().get(index).setTagName(new String(ch, start, length));
                        index++;
                        blTagname = false;
                    }
                }
            };

            //saxParser.parse("C:\\Users\\Filipek\\Documents\\IntelliJ\\TSP\\datasets\\Example XML Dataset.xml", handler);
            saxParser.parse(path, handler);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new Frame(),"Erorr! XML load failed!");
        }
        return locationsDataset;
    }

    public static void saveDatsetToXMLFile(String filePath, LocationsDataset locationsDataset) {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElement("Dataset");
            doc.appendChild(mainRootElement);

            // append child elements to root element
            mainRootElement.appendChild(addDatasetElement(doc,"DatasetName",locationsDataset.getDatasetName()));
            mainRootElement.appendChild(addDatasetElement(doc,"Dimension",String.valueOf(locationsDataset.getDatasetCount())));
            for(Location location : locationsDataset.getLocationsData()){
                mainRootElement.appendChild(addLocationNode(doc,location));
            }
//            output DOM XML to console
//            Transformer transformer = TransformerFactory.newInstance().newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            DOMSource source = new DOMSource(doc);
//            StreamResult console = new StreamResult(System.out);
//            transformer.transform(source, console);
//            System.out.println("\nXML DOM Created Successfully..");
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            Result output = new StreamResult(new File("dataset\\"+locationsDataset.getDatasetName()+".xml"));
            Source input = new DOMSource(doc);
            transformer.transform(input, output);
            JOptionPane.showMessageDialog(new Frame(),"Dataset successfully saved to XML file");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveDatsetToXMLFile(String filePath, String datasetName, int dimension, ArrayList<Double[]> locationsList) {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElement("Dataset");
            doc.appendChild(mainRootElement);

            // append child elements to root element
            mainRootElement.appendChild(addDatasetElement(doc,"DatasetName",datasetName));
            mainRootElement.appendChild(addDatasetElement(doc,"Dimension",String.valueOf(dimension)));
            for(Double[] location : locationsList){
                mainRootElement.appendChild(addLocationNode(doc,String.valueOf(location[0]),String.valueOf(location[1]),""));
            }
//            output DOM XML to console
//            Transformer transformer = TransformerFactory.newInstance().newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            DOMSource source = new DOMSource(doc);
//            StreamResult console = new StreamResult(System.out);
//            transformer.transform(source, console);
//            System.out.println("\nXML DOM Created Successfully..");
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            Result output = new StreamResult(new File(filePath+".xml"));
            Source input = new DOMSource(doc);
            transformer.transform(input, output);
            JOptionPane.showMessageDialog(new Frame(),"Dataset successfully saved to XML file");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Node addDatasetElement(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    private static Node addLocationNode(Document doc, String x, String y, String tagname) {
        Element loc = doc.createElement("Location");
        loc.appendChild(addLocationElement(doc, loc, "X", x));
        loc.appendChild(addLocationElement(doc, loc, "Y", y));
        loc.appendChild(addLocationElement(doc, loc, "Tagname", tagname));
        return loc;
    }

    private static Node addLocationNode(Document doc, Location location) {
        Element loc = doc.createElement("Location");
        loc.appendChild(addLocationElement(doc, loc, "X", String.valueOf(location.getX())));
        loc.appendChild(addLocationElement(doc, loc, "Y", String.valueOf(location.getY())));
        loc.appendChild(addLocationElement(doc, loc, "Tagname", location.getTagName()));
        return loc;
    }

    private static Node addLocationElement(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

}