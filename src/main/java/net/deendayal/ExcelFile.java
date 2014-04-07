package net.deendayal;

import java.util.ArrayList;
import java.util.List;

public class ExcelFile {
   String fileName;
   List<XMLElement> xmlElementList = new ArrayList<XMLElement>();

   public void setFileName(String fileName) {
      this.fileName = fileName;
   }

   public void setXmlElementList(List<XMLElement> xmlElementList) {
      this.xmlElementList = xmlElementList;
   }

   public String getFileName() {
      return fileName;
   }

   public List<XMLElement> getXmlElementList() {
      return xmlElementList;
   }
}
