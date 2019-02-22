/**
 * Project Name:Code
 * File Name:VerCtrl.java
 * Package Name:ctrl
 * Date:2019年2月20日下午4:21:24
 * Copyright (c) 2019, All Rights Reserved.
 *
*/

package ctrl;
/**
 * ClassName:VerCtrl 
 * Function: TODO 
 * Reason:	 TODO 
 * Date:     2019年2月20日 下午4:21:24 
 * @author   Mu Xiaobai
 * @version  
 * @since    JDK 1.8	 
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VerCtrl
{
  public String verManager(String key, String rootPath, String fromPath)
  {
    String path = getClass().getResource("/").getPath() + "/version";
    File pfile = new File(path);
    if (!pfile.exists()) {
      pfile.mkdirs();
    }
    Map verInfo = updateVer(key);
    String mainver = (String)verInfo.get(key + "_main");
    String branchver = (String)verInfo.get(key + "_branch");
    String date = (String)verInfo.get(key + "_date");
    String inStr = "Ver::V" + mainver + "_" + branchver + "\t\tDate::[" + date + "]\tKey::[" + key + "]\r\n";
    verWrite(pfile + "/" + key + ".info", inStr, true);
    
    String xStr = "<?xml version=\"1.0\"?><version><id>" + key + "</id><ver>" + mainver + "</ver><branch>" + branchver + "</branch><date>" + date + "</date></version>";
    String rStr = "V" + mainver + "_" + branchver;
    if (rootPath.indexOf("{ver}") >= 0) {
      rootPath = rootPath.replaceAll("\\{ver\\}", rStr);
    }
    File rfile = new File(rootPath);
    if (!rfile.exists()) {
      rfile.mkdirs();
    }
    verWrite(rootPath + "/version.xml", xStr, false);
    verWrite(fromPath + "/version.xml", xStr, false);
    return rStr;
  }
  
  public Map updateVer(String key)
  {
    Map rMap = new HashMap();
    PpManager pp = new PpManager("version.properties");
    Map info = pp.getPpInfo();
    
    String mainVer = (String)info.get(key + "_main");
    String branchVer = (String)info.get(key + "_branch");
    String date = MUtil.date2String(new Date());
    if (mainVer == null)
    {
      mainVer = "1.0";
      branchVer = "0";
    }
    else
    {
      int i = Integer.parseInt(branchVer);
      branchVer = Integer.toString(i + 1);
    }
    rMap.put(key + "_main", mainVer);
    rMap.put(key + "_branch", branchVer);
    rMap.put(key + "_date", date);
    pp.upatePp(rMap);
    System.out.println("Last version ::" + mainVer + "_" + branchVer + "\t\tdate::" + date);
    
    return rMap;
  }
  
  public void verWrite(String filePath, String ver, boolean append)
  {
    File file = new File(filePath);
    try
    {
      OutputStream os = null;
      os = new FileOutputStream(filePath, append);
      OutputStreamWriter sw = new OutputStreamWriter(os);
      sw.write(ver);
      sw.flush();
      sw.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
