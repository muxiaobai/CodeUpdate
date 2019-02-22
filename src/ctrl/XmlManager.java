/**
 * Project Name:Code
 * File Name:XmlManager.java
 * Package Name:ctrl
 * Date:2019年2月20日下午4:21:48
 * Copyright (c) 2019, All Rights Reserved.
 *
*/

package ctrl;
/**
 * ClassName:XmlManager 
 * Function: TODO 
 * Reason:	 TODO 
 * Date:     2019年2月20日 下午4:21:48 
 * @author   Mu Xiaobai
 * @version  
 * @since    JDK 1.8	 
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlManager
{
  public static Map getXmlInfo(String path)
  {
    Map rMap = new TreeMap();
    try
    {
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
      Element root = doc.getDocumentElement();
      
      NodeList items = root.getElementsByTagName("item");
      List itemList = new ArrayList();
      for (int i = 0; i < items.getLength(); i++)
      {
        Map itemMap = new TreeMap();
        Element base_dir = getElement(items.item(i), "base_dir");
        Element search_dir = getElement(items.item(i), "search_dir");
        Element search_filter = getElement(items.item(i), "search_filter");
        Element from_dir = getElement(items.item(i), "from_dir");
        Element to_dir = getElement(items.item(i), "to_dir");
        
        Element type = getElement(items.item(i), "type");
        Element svn_username = getElement(items.item(i), "svn_username");
        Element svn_password = getElement(items.item(i), "svn_password");
        Element svn_url = getElement(items.item(i), "svn_url");
        Element svn_path = getElement(items.item(i), "svn_path");
        Element svn_time = getElement(items.item(i), "svn_time");
        Element java_path = getElement(items.item(i), "java_path");
        Element web_path = getElement(items.item(i), "web_path");

        
        itemMap.put(base_dir.getNodeName(), base_dir.getTextContent());
        itemMap.put(search_dir.getNodeName(), search_dir.getTextContent());
        itemMap.put(search_filter.getNodeName(), search_filter.getTextContent());
        itemMap.put(from_dir.getNodeName(), from_dir.getTextContent());
        itemMap.put(to_dir.getNodeName(), to_dir.getTextContent());

        itemMap.put(type.getNodeName(), type.getTextContent());
        itemMap.put(svn_username.getNodeName(), svn_username.getTextContent());
        itemMap.put(svn_password.getNodeName(), svn_password.getTextContent());
        itemMap.put(svn_url.getNodeName(), svn_url.getTextContent());
        itemMap.put(svn_path.getNodeName(), svn_path.getTextContent());
        itemMap.put(svn_time.getNodeName(), svn_time.getTextContent());
        itemMap.put(java_path.getNodeName(), java_path.getTextContent());
        itemMap.put(web_path.getNodeName(), web_path.getTextContent());

        Element roles = getElement(items.item(i), "roles");
        if (roles != null)
        {
          List roleList = new ArrayList();
          NodeList rolesList = roles.getElementsByTagName("role");
          for (int m = 0; m < rolesList.getLength(); m++)
          {
            Element rf = getElement(rolesList.item(m), "rf");
            Element rt = getElement(rolesList.item(m), "rt");
            Map tMap = new TreeMap();
            tMap.put("rf", rf.getTextContent());
            tMap.put("rt", rt.getTextContent());
            roleList.add(tMap);
          }
          itemMap.put(roles.getNodeName(), roleList);
        }
        itemList.add(itemMap);
      }
      rMap.put("item", itemList);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return rMap;
  }
  
  public static Element getElement(Object item, String name)
  {
    return (Element)((Element)item).getElementsByTagName(name).item(0);
  }
  
  public static void main(String[] args)
  {
    
    getXmlInfo("E:\\git\\CourseExercises\\shell\\Code\\src\\ctrl\\config.xml");
  }
}
