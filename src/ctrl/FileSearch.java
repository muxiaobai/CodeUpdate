/**
 * Project Name:Code
 * File Name:FileSearch.java
 * Package Name:ctrl
 * Date:2019年2月20日下午4:19:35
 * Copyright (c) 2019, All Rights Reserved.
 *
*/

package ctrl;
/**
 * ClassName:FileSearch 
 * Function: TODO 
 * Reason:	 TODO 
 * Date:     2019年2月20日 下午4:19:35 
 * @author   Mu Xiaobai
 * @version  
 * @since    JDK 1.8	 
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import svn.svn;

public class FileSearch
{
  private List infoList = new ArrayList();
  private String search_dir = "";
  private String from_dir = "";
  private String base_dir = "";
  private String to_dir = "";
  private String to_all = "";
  private String search_filter = "";
  private long lasttime;
  private Date thistime;
//  private String rootPath = FileSearch.class.getClassLoader().getResource("/").getPath();
  private String rootPath = getClass().getResource("/").getPath();
//    private String rootPath =Thread.currentThread().getContextClassLoader().getResourceAsStream("test.properties");
  public String getLastModTime()
  {
    Date tdate = new Date(this.lasttime);
    return MUtil.date2String(tdate);
  }
  
  public FileSearch(String iKey)
  {
    PpManager pp = new PpManager();
    Map pMap = pp.getPpInfo();
    String path = this.rootPath;
    Iterator it = pMap.entrySet().iterator();
    while (it.hasNext())
    {
      Map.Entry en = (Map.Entry)it.next();
      String key = (String)en.getKey();
      String value = (String)en.getValue();
      if ((iKey == null) || (iKey.length() <= 0) || (iKey.equals(key)))
      {
        File file = new File(path + File.separator + "config" + 
          File.separator + key + ".xml");
        if ((file.exists()) && (file.isFile()))
        {
          Map tmap = XmlManager.getXmlInfo(file.getAbsolutePath());
          System.out.println("::" + key + " Last mod time is :: " + value);
          if (value.length() > 0)
          {
            String fstr = "yyyy-MM-dd";
            if (value.trim().length() > 10) {
              fstr = "yyyy-MM-dd HH:mm:ss";
            }
            Date fDate = MUtil.string2Date(value, fstr);
            this.lasttime = fDate.getTime();
          }
          else
          {
            Date tDate = MUtil.string2Date("1970-01-01", "yyyy-MM-dd");
            this.lasttime = tDate.getTime();
          }
          this.thistime = new Date();
          Map infoMap = new HashMap();
          
          infoMap.put("time", Long.toString(this.lasttime));
          infoMap.put("value", tmap.get("item"));
          infoMap.put("key", key);
          this.infoList.add(infoMap);
          
          pMap.put(key, MUtil.date2String(this.thistime));
          pp.upatePp(pMap);
        }
        else
        {
          System.out.println("File " + file.getAbsolutePath() + 
            " is not exists!");
        }
      }
    }
  }
  
  public Map findSearchFiles()
  {
    Map rMap = new TreeMap();
    String timeDir = MUtil.date2String(new Date(), "yyyyMMddHHmmss");
    Map baseDirMap = new HashMap();
    Map fromMap = new HashMap();
    for (int i = 0; i < this.infoList.size(); i++)
    {
      Map infoMap = (Map)this.infoList.get(i);
      long time = Long.parseLong((String)infoMap.get("time"));
      List vList = (List)infoMap.get("value");
      String key = (String)infoMap.get("key");
      for (int j = 0; j < vList.size(); j++)
      {
        Map tMap = (Map)vList.get(j);
        this.search_dir = ((String)tMap.get("search_dir"));
        this.from_dir = ((String)tMap.get("from_dir"));
        this.base_dir = ((String)tMap.get("base_dir"));
        this.to_dir = ((String)tMap.get("to_dir"));
        if (this.base_dir.indexOf("{root}") >= 0) {
          this.base_dir = this.base_dir.replaceAll("\\{root\\}", this.rootPath);
        }
        if (this.base_dir.indexOf("{time}") >= 0) {
          this.base_dir = this.base_dir.replaceAll("\\{time\\}", timeDir);
        } else {
          this.base_dir = (this.base_dir + File.separator + timeDir);
        }
        if (this.search_dir.indexOf("{root}") >= 0)
        {
          this.search_dir = this.search_dir.replaceAll("\\{root\\}", this.rootPath);
          tMap.put("search_dir", this.search_dir);
        }
        if (this.from_dir.indexOf("{root}") >= 0)
        {
          this.from_dir = this.from_dir.replaceAll("\\{root\\}", this.rootPath);
          tMap.put("from_dir", this.from_dir);
        }
        baseDirMap.put(key, this.base_dir);
        fromMap.put(key, this.search_dir);
        String tFilter = "";
        if (tMap.get("search_filter") != null) {
          tFilter = (String)tMap.get("search_filter");
        }
        this.search_filter = tFilter.replaceAll("\\*", "\\\\S*");
        this.search_filter = this.search_filter.replaceAll("\\.", "[.]");
        this.to_all = (this.base_dir + File.separator + this.to_dir);
        //search file sList is base_path array
        List sList = null;
        if("svn".equals(tMap.get("type").toString().trim())){
             sList = findSearchFilesSvn(tMap,time);
        }else if("dir".equals(tMap.get("type").toString().trim())){
            sList = findSearchFiles(this.search_dir, this.search_filter, time);
        }else{
            sList = findSearchFiles(this.search_dir, this.search_filter, time);
        }
        System.out.println("search all Files:"+sList);
        tMap.put("LIST", sList);
        tMap.put("TOALL", this.to_all);
        tMap.put("KEY", key);
        rMap.put(this.search_dir, tMap);
      }
    }
    rMap.put("baseDir", baseDirMap);
    rMap.put("fromDir", fromMap);
    
    return rMap;
  }
  /**
   * user xml svn_time 
   * findSearchFilesSvn:().
   * @author Mu Xiaobai
   * @param tMap 
   * @param time no use
   * @return
   * @since JDK 1.8
   */
  private List findSearchFilesSvn(Map tMap,long time){
//      svn svn = new svn();
//      System.out.print(tMap);
//      String base_svn = tMap.get("svn_url").toString();
//      String path = tMap.get("svn_path").toString();
//      String java_path = tMap.get("java_path").toString();
//      String web_path = tMap.get("web_path").toString();
      svn svn = new svn(tMap.get("svn_username").toString(), tMap.get("svn_password").toString(), tMap.get("svn_url").toString()+tMap.get("svn_path").toString());
      try {
          return svn.getChangeFileList(svn,tMap,time);
      } catch (Exception e) {
          e.printStackTrace();
      }
    return null;
  }
  
  private List findSearchFiles(String path, String filter, long time)
  {
    List rList = new ArrayList();
    File pFile = new File(path);
    if (pFile.isDirectory())
    {
      List tList = new ArrayList();
      File[] fList = pFile.listFiles();
      File[] arrayOfFile1;
      int j = (arrayOfFile1 = fList).length;
      for (int i = 0; i < j; i++)
      {
        File file = arrayOfFile1[i];
        if (file.isDirectory())
        {
          tList.addAll(findSearchFiles(file.getPath(), filter, time));
        }
        else if (Pattern.matches(filter, file.getPath()))
        {
          long tlm = file.lastModified();
          if (tlm > time) {
            tList.add(file.getPath());
          }
        }
      }
      rList.addAll(tList);
    }
    else if (Pattern.matches(this.search_filter, pFile.getPath()))
    {
      rList.add(pFile.getPath());
    }
    return rList;
  }
  /**
   * 拷贝文件到目录
   * backFiles:().
   * @author Mu Xiaobai
   * @param searchMap
   * @since JDK 1.8
   */
  public void backFiles(Map searchMap)
  {
    Iterator it = searchMap.entrySet().iterator();
    while (it.hasNext())
    {
      Map.Entry en = (Map.Entry)it.next();
      Map tMap = (Map)en.getValue();
      String fDir = (String)tMap.get("from_dir");
      String tDir = (String)tMap.get("TOALL");
      String sDir = (String)tMap.get("search_dir");
      List roleList = null;
      if (tMap.get("roles") != null) {
        roleList = (List)tMap.get("roles");
      }
      List fList = new ArrayList();
      List tList = new ArrayList();
      
      List searchList = (List)tMap.get("LIST");
      if (searchList != null)
      {
        for (int i = 0; i < searchList.size(); i++)
        {
          String path = (String)searchList.get(i);
          path = path.replaceAll("\\\\", "/");
          path = path.replaceAll(sDir, "");
          path = ("/" + path).replaceAll(sDir, "");
          if (roleList != null) {
            for (int m = 0; m < roleList.size(); m++)
            {
              Map rMap = (Map)roleList.get(m);
              String rf = (String)rMap.get("rf");
              String rt = (String)rMap.get("rt");
              path = path.replaceAll(rf, rt);
            }
          }
          String fpath = fDir + path;
          String tpath = tDir + path;
          fList.add(fpath);
          tList.add(tpath);
        }
        FileCopyManager fm = new FileCopyManager();
        fm.copyFiles(fList, tList, null);
      }
    }
  }
  
  public static void main(String[] args)
  {
    String key = "";
    if ((args != null) && 
      (args.length > 0)) {
      key = args[0];
    }
//    key="jy_zq_dir";
    //load Xml and time
    FileSearch fs = new FileSearch(key);

    //find file ,lastModified() and Pattern.matches(filter, file.getPath())
    Map vMap = fs.findSearchFiles();
    
    //version 
    System.out.println("Create version::");
    Map bMap = (Map)vMap.get("baseDir");
    Map fMap = (Map)vMap.get("fromDir");
    VerCtrl ver = new VerCtrl();
    Map verMap = new HashMap();
    Iterator it = bMap.entrySet().iterator();
    while (it.hasNext())
    {
      Map.Entry en = (Map.Entry)it.next();
      String tkey = (String)en.getKey();
      String path = (String)en.getValue();
      String fPath = (String)fMap.get(tkey);
      // write version.properties 
      String istr = ver.verManager(tkey, path, fPath);
      System.out.println(tkey + "===>> " + istr);
      verMap.put(tkey, istr);
    }
    Iterator vit = vMap.entrySet().iterator();
    while (vit.hasNext())
    {
      Map.Entry en = (Map.Entry)vit.next();
      String tkey = (String)en.getKey();
      if ((!tkey.equalsIgnoreCase("baseDir")) && (!tkey.equalsIgnoreCase("fromDir")))
      {
        Map tMap = (Map)en.getValue();
        String tToall = (String)tMap.get("TOALL");
        String ttkey = (String)tMap.get("KEY");
        if ((tToall.indexOf("{ver}") >= 0) && 
          (verMap.get(ttkey) != null))
        {
          String verstr = verMap.get(ttkey).toString();
          tToall = tToall.replaceAll("\\{ver\\}", verstr);
          tMap.put("TOALL", tToall);
        }
      }
    }
    System.out.println("Create Version OK!");
   
    // Copy file to baseDir
    fs.backFiles(vMap);
  }
}
