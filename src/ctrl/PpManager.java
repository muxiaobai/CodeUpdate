/**
 * Project Name:Code
 * File Name:PpManager.java
 * Package Name:ctrl
 * Date:2019年2月20日下午4:20:43
 * Copyright (c) 2019, All Rights Reserved.
 *
*/

package ctrl;
/**
 * ClassName:PpManager 
 * Function: TODO 
 * Reason:	 TODO 
 * Date:     2019年2月20日 下午4:20:43 
 * @author   Mu Xiaobai
 * @version  
 * @since    JDK 1.8	 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class PpManager
{
  private Map infoMap = new TreeMap();
  private String configPath = "lasttime.properties";
  private String filePath;

  public PpManager()
  {
    this.filePath = getClass().getResource(this.configPath).getFile();
  }
  
  public PpManager(String cPath)
  {
    this.configPath = cPath;
    this.filePath = getClass().getResource(this.configPath).getFile();
  }
  
  public void upatePp(Map pMap)
  {
    try
    {
      Properties pp = new Properties();
      InputStream is = new FileInputStream(new File(this.filePath));
      pp.load(is);
      Iterator it = pMap.entrySet().iterator();
      while (it.hasNext())
      {
        Map.Entry en = (Map.Entry)it.next();
        pp.setProperty((String)en.getKey(), (String)en.getValue());
      }
      File wf = new File(this.filePath);
      Writer wr = new FileWriter(wf);
      pp.store(wr, "");
      wr.flush();
      wr.close();
      is.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public Map getPpInfo()
  {
    try
    {
      Properties pp = new Properties();
      InputStream is = new FileInputStream(new File(this.filePath));
      pp.load(is);
      
      Iterator it = pp.entrySet().iterator();
      while (it.hasNext())
      {
        Map.Entry en = (Map.Entry)it.next();
        this.infoMap.put(en.getKey(), en.getValue());
      }
      is.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return this.infoMap;
  }
}
