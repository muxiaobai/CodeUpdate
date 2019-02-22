/**
 * Project Name:Code
 * File Name:FileCopy.java
 * Package Name:ctrl
 * Date:2019年2月20日下午4:18:06
 * Copyright (c) 2019, All Rights Reserved.
 *
*/

package ctrl;
/**
 * ClassName:FileCopy 
 * Function: TODO 
 * Reason:	 TODO 
 * Date:     2019年2月20日 下午4:18:06 
 * @author   Mu Xiaobai
 * @version  
 * @since    JDK 1.8	 
 */

import java.io.File;
import java.util.Date;
import java.util.Map;

public class FileCopy
{
  String[] systems = null;
  String back_dir = "";
  Map pMap = null;
  String path = ClassLoader.getSystemResource("").getPath();
  
  public FileCopy()
  {
    PpManager pp = new PpManager("copy_config.properties");
    this.pMap = pp.getPpInfo();
    
    this.back_dir = ((String)this.pMap.get("back_dir"));
    if ((this.back_dir == null) || (this.back_dir.length() == 0)) {
      this.back_dir = (this.path + File.separator + "back");
    }
    String system = (String)this.pMap.get("system");
    this.systems = system.split(",");
  }
  
  public void copy()
  {
    if ((this.systems != null) && (this.systems.length > 0))
    {
      Date date = new Date();
      String dStr = MUtil.date2String(date, "yyyyMMdd_HH_mm_ss");
      FileCopyManager fm = new FileCopyManager();
      for (int i = 0; i < this.systems.length; i++)
      {
        String system = this.systems[i];
        String back = this.back_dir;
        if (back.indexOf("{system}") >= 0) {
          back = back.replaceAll("\\{system\\}", system);
        } else {
          back = back + File.separator + system;
        }
        if (back.indexOf("{time}") >= 0) {
          back = back.replaceAll("\\{time\\}", dStr);
        } else {
          back = back + File.separator + dStr;
        }
        String srcDir = (String)this.pMap.get(system + "_src_dir");
        if ((srcDir == null) || (srcDir.trim().length() == 0)) {
          srcDir = 
            this.path + File.separator + "update" + File.separator + system;
        }
        File srcFile = new File(srcDir);
        if (srcFile.exists())
        {
          String pFileName = srcFile.getParent();
          File doneFile = new File(pFileName + File.separator + 
            "_done" + File.separator + dStr + File.separator + 
            system);
          String destDir = (String)this.pMap.get(system + "_dest_dir");
          File destFile = new File(destDir);
          if (!destFile.exists()) {
            destFile.mkdirs();
          }
          System.out.println("CopyFile: from:" + srcDir + " to:" + 
            destDir);
          fm.setBackDir(back);
          fm.copyDirectory(srcDir, destDir);
          fm.moveDirectory(srcDir, doneFile.getAbsolutePath(), false);
        }
      }
    }
  }
  
  public static void main(String[] args)
  {
    FileCopy cp = new FileCopy();
    cp.copy();
  }
}
