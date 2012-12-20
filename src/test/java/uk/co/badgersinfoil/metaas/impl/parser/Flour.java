package uk.co.badgersinfoil.metaas.impl.parser;

import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Flour extends DefaultHandler {

    float amount = 0;

    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) {
        System.out.println(qName);
    }

    public void endElement (String uri, String localName, String qName)
            throws SAXException
    {
        System.out.println(qName);
    }

    public static void main(String[] args) throws Exception {
        Flour f = new Flour();

        XMLReader parser = org.xml.sax.helpers.XMLReaderFactory.createXMLReader();
        
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser p = factory.newSAXParser();

        //p.setProperty();

        parser.setErrorHandler(new ErrorHandler() {
            public void warning(SAXParseException exception) throws SAXException {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void error(SAXParseException exception) throws SAXException {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void fatalError(SAXParseException exception) throws SAXException {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });


        parser.setFeature("http://xml.org/sax/features/validation", false);
        parser.parse("/Users/eliseev/1.xml");


    }
}