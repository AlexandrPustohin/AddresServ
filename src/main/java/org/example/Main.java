package org.example;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static List<AsAddrObj> addrObjList = new ArrayList<>();
    public static List<AsAdmHierarchy> itemList = new ArrayList<>();
    public static void main(String[] args) throws ParseException, IOException {
        final String filter = "проезд";


        ParserXMLObj parserXMLObj = new ParserXMLObj();
        ParserXMLItems parserXMLItems = new ParserXMLItems();
        SimpleDateFormat sdf =  new java.text.SimpleDateFormat("yyyy-MM-dd");

        Date date = sdf.parse(args[0]);
        List<Long> listObj = getListObjId(args);

        String fileObject = new File(".").getCanonicalPath()+"\\AS_ADDR_OBJ.XML";
        String fileItem = new File(".").getCanonicalPath()+"\\AS_ADM_HIERARCHY.XML";
        try {
            addrObjList = parserXMLObj.parseAsAddrObj(new File(fileObject));
            itemList = parserXMLItems.parseAsAddrObj(new File(fileItem));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        //анализ с выводом
        addrObjList.stream().filter(addr -> listObj.contains(addr.getObjectId())
                        && (date.after(addr.getStartDate()) && date.before(addr.getEndDate()) ))
                .forEach(a-> System.out.println(a.getObjectId()+": "+ a.getTypeName()+" "+ a.getName()));

        System.out.println();
        System.out.println("Задание 2");
        addrObjList.stream().filter(addr -> addr.getIsActive()
                        && addr.getTypeName().equals(filter))
                .forEach(a-> System.out.println(getFullString(a)));

    }
    private static List<Long> getListObjId(String args[]){
        return Arrays.stream(args).skip(1).map(id->Long.parseLong(id)).collect(Collectors.toList());
    }
    private static String getFullString (AsAddrObj addrObj){
        List<Long> parentList = new ArrayList<>();
        AsAdmHierarchy temp = null;
        long parentId = addrObj.getObjectId();
        parentList.add(parentId);
        while (temp==null || temp.getParentObjId() != 0) {
            for (AsAdmHierarchy item : itemList) {
                if (item.getObjectId() == parentId && item.isActive()) {
                    temp = item;
                    parentList.add(item.getParentObjId());
                    parentId = item.getParentObjId();
                    break;
                }
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        Collections.reverse(parentList);
        for (Long objId : parentList) {
            for(AsAddrObj addr: addrObjList){
                if (addr.getObjectId() == objId) {
                    stringBuilder.append(addr.getTypeName())
                            .append(" ")
                            .append(addr.getName());
                           if(parentList.indexOf(objId)!=parentList.size()-1){
                               stringBuilder.append(", ");}
                    break;
                }
            }

        }

        return stringBuilder.toString();
    }
}