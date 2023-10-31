package org.example;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ParserXMLObj {
    SimpleDateFormat sdf =  new java.text.SimpleDateFormat("yyyy-MM-dd");
    public  List<AsAddrObj> parseAsAddrObj(File filepath) throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = filepath;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        // получаем узлы с именем OBJECT
        // теперь XML полностью загружен в память
        // в виде объекта Document
        NodeList nodeList =  doc.getElementsByTagName("OBJECT");


        // создадим из него список объектов OBJECT
        List<AsAddrObj> addrObjList;
        Stream<Node> nodeStream = IntStream.range(0, nodeList.getLength()).mapToObj(nodeList::item);

        addrObjList = nodeStream.map(node -> getAddrObj(node)).collect(Collectors.toList());
        return addrObjList;
    }
    // создаем из узла документа объект AsAddrObj
    private  AsAddrObj getAddrObj(Node node)  {
        AsAddrObj addrObj = new AsAddrObj();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            addrObj.setId(Long.parseLong(getAtrValue("ID", node)));
            addrObj.setObjectId(Long.parseLong(getAtrValue("OBJECTID", node)));
            addrObj.setObjectGUID(getAtrValue("OBJECTGUID",node));
            addrObj.setChangeId(Long.parseLong(getAtrValue("CHANGEID",node)));
            addrObj.setName(getAtrValue("NAME",node));
            addrObj.setTypeName(getAtrValue("TYPENAME",node));
            addrObj.setLevel(Integer.parseInt(getAtrValue("LEVEL",node)));
            addrObj.setOperTypeId(Integer.parseInt(getAtrValue("OPERTYPEID",node)));
            addrObj.setPrevId(Long.parseLong(getAtrValue("PREVID",node)));
            addrObj.setNextId(Long.parseLong(getAtrValue("NEXTID",node)));

            try{
                addrObj.setUpdateDate(sdf.parse( getAtrValue("UPDATEDATE",node) ));
                addrObj.setStartDate(sdf.parse(getAtrValue("STARTDATE",node) ));
                addrObj.setEndDate(sdf.parse(getAtrValue("ENDDATE",node) ));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            addrObj.setIsActual(getAtrValue("ISACTUAL",node).equals("1")?true:false);
            addrObj.setIsActive(getAtrValue("ISACTIVE",node).equals("1")?true:false);
        }

        return addrObj;
    }

    // получаем значение атрибута по указанному имени
    private  String getAtrValue(String atr, Node obj) {
        NamedNodeMap attr = obj.getAttributes();
        if (null != attr) {
            Node p = attr.getNamedItem(atr);
            if (p !=null) {
                return p.getNodeValue();
            }else {
                return "0";
            }
        }
        return null;
    }
}
