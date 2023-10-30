package org.example;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws  IOException {
        List<AsAddrObj> addrObjList;
        ParserXML parserXML = new ParserXML();
        String fileName = new File(".").getCanonicalPath()+"\\AS_ADDR_OBJ.XML";
        try {
            addrObjList = parserXML.parseAsAddrObj(new File(fileName));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        //TODO анализ

    }
}