package net.deendayal;

import java.util.*;

public class XMLElement {
   String name;
   Set<String> attributes = new LinkedHashSet<String>();
   Map<String, List<String>> attributeValueMap = new HashMap<String, List<String>>();
   List<XMLElement> nestedXmlElement = new ArrayList<XMLElement>();

   public void setName(String name) {
      this.name = name;
   }

   public void setAttributes(Set<String> attributes) {
      this.attributes = attributes;
   }

   public void setAttributeValueMap(Map<String, List<String>> attributeValueMap) {
      this.attributeValueMap = attributeValueMap;
   }

   public void setNestedXmlElement(List<XMLElement> nestedXmlElement) {
      this.nestedXmlElement = nestedXmlElement;
   }

   public String getName() {
      return name;
   }

   public Set<String> getAttributes() {
      return attributes;
   }

   public Map<String, List<String>> getAttributeValueMap() {
      return attributeValueMap;
   }

   public List<XMLElement> getNestedXmlElement() {
      return nestedXmlElement;
   }
}
