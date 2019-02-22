/**
 * Project Name:Code
 * File Name:FileCopyManager.java
 * Package Name:ctrl
 * Date:2019年2月20日下午4:18:32
 * Copyright (c) 2019, All Rights Reserved.
 *
*/

package ctrl;
/**
 * ClassName:FileCopyManager 
 * Function: TODO 
 * Reason:	 TODO 
 * Date:     2019年2月20日 下午4:18:32 
 * @author   Mu Xiaobai
 * @version  
 * @since    JDK 1.8	 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class FileCopyManager
{
  private boolean isBack = false;
  private String backDir;
  
  public void setBackDir(String bDir)
  {
    this.isBack = false;
    if (bDir != null)
    {
      File file = new File(bDir);
      if (!file.exists()) {
        file.mkdirs();
      }
      if (file.isDirectory())
      {
        this.isBack = true;
        this.backDir = file.getAbsolutePath();
        MPrint.println("[Open Back MODULE]:: back dir: " + this.backDir);
      }
      else
      {
        MPrint.println("[Back MODULE fault]:: back dir: " + this.backDir);
      }
    }
  }
  
  public void copyFiles(List from_list, List to_list, List back_list)
  {
    for (int i = 0; i < from_list.size(); i++)
    {
      File from = new File((String)from_list.get(i));
      File to = new File((String)to_list.get(i));
      File back = null;
      if ((back_list != null) && (back_list.size() == to_list.size())) {
        back = new File((String)from_list.get(i));
      }
      copyFile(from, to, back);
    }
  }
  
  public void moveDirectory(String fPath, String tPath)
  {
    moveDirectory(fPath, tPath, true);
  }
  
  public void moveDirectory(String fPath, String tPath, boolean moveRoot)
  {
    boolean tBack = this.isBack;
    this.isBack = false;
    copyDirectory(fPath, tPath);
    if (moveRoot)
    {
      delete(new File(fPath));
    }
    else
    {
      File root = new File(fPath);
      File[] files = root.listFiles();
      File[] arrayOfFile1;
      int j = (arrayOfFile1 = files).length;
      for (int i = 0; i < j; i++)
      {
        File f = arrayOfFile1[i];
        delete(f);
      }
    }
    this.isBack = tBack;
  }
  
  public void copyDirectory(String form_path, String to_path)
  {
    String tStr = "Copy";
    MPrint.println("=================[Begin " + tStr + " Dir] From:[ " + form_path + " ] To:[ " + to_path + " ]==================");
    File bakFile = null;
    if (this.isBack) {
      bakFile = new File(this.backDir);
    }
    copyDir(new File(form_path), new File(to_path), bakFile);
    
    MPrint.println("=================[End " + tStr + " Dir]==================");
  }
  
  private void copyDir(File from_dir, File to_dir, File bak_dir)
  {
    if (from_dir.isDirectory())
    {
      File[] files = from_dir.listFiles();
      File[] arrayOfFile1;
      int j = (arrayOfFile1 = files).length;
      for (int i = 0; i < j; i++)
      {
        File tFile = arrayOfFile1[i];
        String name = tFile.getName();
        String toFile = to_dir.getPath() + File.separator + name;
        String fromFile = from_dir.getPath() + File.separator + name;
        File bFile = null;
        if (bak_dir != null)
        {
          String bakFile = bak_dir.getPath() + File.separator + name;
          bFile = new File(bakFile);
        }
        if (tFile.isDirectory()) {
          copyDir(new File(fromFile), new File(toFile), bFile);
        } else {
          copyFile(new File(fromFile), new File(toFile), bFile);
        }
      }
    }
  }
  
  public void copyFile(String from_path, String to_path)
  {
    copyFile(new File(from_path), new File(to_path));
  }
  
  public void copyFile(File from_file, File to_file)
  {
    copyFile(from_file, to_file, null);
  }
  
  public void copyFile(File from_file, File to_file, File bak_file)
  {
    if (from_file.exists())
    {
      if (!to_file.getParentFile().exists()) {
        to_file.getParentFile().mkdirs();
      }
      if (to_file.exists()) {
        if (this.isBack)
        {
          MPrint.print("\t[Bak]::");
          copyFile(to_file, bak_file);
          to_file.delete();
        }
      }
      try
      {
        InputStream is = new FileInputStream(from_file);
        OutputStream os = new FileOutputStream(to_file);
        
        byte[] b = new byte[is.available()];
        while (is.read(b) > 0) {
          os.write(b);
        }
        os.flush();
        os.close();
        is.close();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      MPrint.println("[Copy file]:: " + from_file.getPath() + "==>" + to_file.getPath());
    }
  }
  
  public void delete(File file)
  {
    if (file.isDirectory())
    {
      File[] files = file.listFiles();
      File[] arrayOfFile1;
      int j = (arrayOfFile1 = files).length;
      for (int i = 0; i < j; i++)
      {
        File f = arrayOfFile1[i];
        delete(f);
      }
    }
    MPrint.println("Delete File[" + file.getName() + "]" + file.delete());
  }
  
  public static void main(String[] args)
  {
    FileCopyManager fm = new FileCopyManager();
    fm.delete(new File("E:/workspace/first/VerCtrl2.0/WebRoot/WEB-INF/classes/update/test"));
  }
}
