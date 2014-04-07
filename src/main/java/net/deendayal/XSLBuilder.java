package net.deendayal;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class XSLBuilder {
   static ExcelFile excelFile = new ExcelFile();

   public static void main(String[] args) {
      try {
         File file = new File("C:\\sabre\\PNR\\XmlToXslTransformer\\src\\main\\java\\net\\deendayal\\" + args[0]);
         DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder();
         Document doc = dBuilder.parse(file);
         System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
         if (doc.hasChildNodes()) {
            excelFile.setFileName(doc.getChildNodes().item(0).getNodeName());
            buildTree(doc.getChildNodes().item(0).getChildNodes(), null);
            System.out.println(excelFile.getXmlElementList().size());
         }

         HSSFWorkbook wb = new HSSFWorkbook();
         createXSLFile(wb, excelFile.getXmlElementList());

         FileOutputStream output = new FileOutputStream(new File("C:\\sabre\\PNR\\XmlToXslTransformer\\src\\main\\java\\net\\deendayal", excelFile.getFileName() + ".xls"));
         wb.write(output);
         output.flush();
         output.close();
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }
   }

   private static void createXSLFile(HSSFWorkbook wb, List<XMLElement> xmlElementList) {
      for (XMLElement xmlElement : xmlElementList) {
         List<String> attributes = null;
         List<HSSFRow> rowList = new ArrayList<HSSFRow>();
         HSSFSheet sheet = wb.createSheet(xmlElement.getName());
         HSSFRow row = sheet.createRow(0);
         for (int i = 0; i < xmlElement.getAttributes().size(); i++) {
            HSSFCell cell = row.createCell(i);
            attributes = new ArrayList<String>(xmlElement.getAttributes());
            cell.setCellValue(attributes.get(i));
         }
         for (int i = 0; i < xmlElement.getAttributes().size(); i++) {
            List<String> values = xmlElement.getAttributeValueMap().get(attributes.get(i));
            for (int j = 0; j < values.size(); j++) {
               HSSFRow dataRow;
               if (rowList.size() <= j) {
                  dataRow = sheet.createRow(j + 1);
                  rowList.add(dataRow);
               } else {
                  dataRow = rowList.get(j);
               }
               HSSFCell cell = dataRow.createCell(i);
               cell.setCellValue(values.get(j));
            }
         }
         if (xmlElement.getNestedXmlElement().size() > 0)
            createXSLFile(wb, xmlElement.getNestedXmlElement());
      }
   }

   private static void buildTree(NodeList nodeList, XMLElement element) {

      for (int count = 0; count < nodeList.getLength(); count++) {
         Node tempNode = nodeList.item(count);
         if (tempNode.getNodeType() == Node.ELEMENT_NODE && tempNode.hasChildNodes() && tempNode.getChildNodes().getLength() > 1) {
            XMLElement xmlElement = getMatchingXmlElementIfExist(excelFile.getXmlElementList(), tempNode.getNodeName());
            if (xmlElement == null) {
               xmlElement = new XMLElement();
               xmlElement.setName(tempNode.getNodeName());
               if (element == null)
                  excelFile.getXmlElementList().add(xmlElement);
               else
                  element.getNestedXmlElement().add(xmlElement);
            }
            if (tempNode.hasAttributes()) {
               NamedNodeMap nodeMap = tempNode.getAttributes();
               for (int i = 0; i < nodeMap.getLength(); i++) {
                  Node node = nodeMap.item(i);
                  xmlElement.getAttributes().add(node.getNodeName());
                  if (xmlElement.getAttributeValueMap().containsKey(node.getNodeName()))
                     xmlElement.getAttributeValueMap().get(node.getNodeName()).add(node.getNodeValue());
                  else {
                     List<String> values = new ArrayList<String>();
                     values.add(node.getNodeValue());
                     xmlElement.getAttributeValueMap().put(node.getNodeName(), values);
                  }
               }
            }
            buildTree(tempNode.getChildNodes(), xmlElement);
         }
         if (tempNode.getNodeType() == Node.ELEMENT_NODE && element != null && tempNode.getChildNodes().getLength() == 1) {
            element.getAttributes().add(tempNode.getNodeName());
            if (tempNode.hasAttributes()) {
               NamedNodeMap nodeMap = tempNode.getAttributes();
               for (int i = 0; i < nodeMap.getLength(); i++) {
                  Node node = nodeMap.item(i);
                  element.getAttributes().add(node.getNodeName());
                  if (element.getAttributeValueMap().containsKey(node.getNodeName()))
                     element.getAttributeValueMap().get(node.getNodeName()).add(node.getNodeValue());
                  else {
                     List<String> values = new ArrayList<String>();
                     values.add(tempNode.getNodeValue());
                     element.getAttributeValueMap().put(node.getNodeName(), values);
                  }
               }
            } else {
               if (element.getAttributeValueMap().containsKey(tempNode.getNodeName()))
                  element.getAttributeValueMap().get(tempNode.getNodeName()).add(tempNode.getTextContent());
               else {
                  List<String> values = new ArrayList<String>();
                  values.add(tempNode.getTextContent());
                  element.getAttributeValueMap().put(tempNode.getNodeName(), values);
               }
            }
         }
      }
   }

   private static XMLElement getMatchingXmlElementIfExist(List<XMLElement> xmlElementList, String nodeName) {
      for (XMLElement xmlElement : xmlElementList) {
         if (nodeName.equalsIgnoreCase(xmlElement.getName()))
            return xmlElement;
      }
      return null;
   }
}
