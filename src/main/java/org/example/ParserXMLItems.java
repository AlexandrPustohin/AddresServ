package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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

public class ParserXMLItems {
    SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
    public List<AsAdmHierarchy> parseAsAddrObj(File filepath) throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = filepath;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        // получаем узлы с именем OBJECT
        // теперь XML полностью загружен в память
        // в виде объекта Document
        NodeList nodeList =  doc.getElementsByTagName("ITEM");

        // создадим из него список объектов
        List<AsAdmHierarchy> addrObjList;
        Stream<Node> nodeStream = IntStream.range(0, nodeList.getLength()).mapToObj(nodeList::item);
        addrObjList = nodeStream.map(node -> getAddrItem(node)).collect(Collectors.toList());
        return addrObjList;
    }
    // создаем из узла документа объект OBJECT
    private AsAdmHierarchy getAddrItem(Node node)  {
        AsAdmHierarchy addrObj = new AsAdmHierarchy();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            addrObj.setId(Long.parseLong(getAtrValue("ID", node)));
            addrObj.setObjectId(Long.parseLong(getAtrValue("OBJECTID", node)));
            addrObj.setParentObjId(Long.parseLong(getAtrValue("PARENTOBJID",node)));
            addrObj.setChangeId(Long.parseLong(getAtrValue("CHANGEID",node)));
            addrObj.setPrevId(Long.parseLong(getAtrValue("PREVID",node)));
            addrObj.setNextId(Long.parseLong(getAtrValue("NEXTID",node)));

            try{
                addrObj.setUpdateDate(sdf.parse( getAtrValue("UPDATEDATE",node) ));
                addrObj.setStartDate(sdf.parse(getAtrValue("STARTDATE",node) ));
                addrObj.setEndDate(sdf.parse(getAtrValue("ENDDATE",node) ));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            addrObj.setActive(getAtrValue("ISACTIVE",node).equals("1")?true:false);
        }

        return addrObj;
    }

    // получаем значение атирибута по указанному имени
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
