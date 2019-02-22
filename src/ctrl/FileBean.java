/**
 * Project Name:Code
 * File Name:FileBean.java
 * Package Name:ctrl
 * Date:2019年2月20日下午4:17:41
 * Copyright (c) 2019, All Rights Reserved.
 *
*/

package ctrl;
/**
 * ClassName:FileBean 
 * Function: TODO 
 * Reason:	 TODO 
 * Date:     2019年2月20日 下午4:17:41 
 * @author   Mu Xiaobai
 * @version  
 * @since    JDK 1.8	 
 */

import java.util.List;

public class FileBean
{
  private String name;
  private String size;
  private String modtime;
  private String isDir;
  private String path;
  private List children;
  
  public List getChildren()
  {
    return this.children;
  }
  
  public void setChildren(List children)
  {
    this.children = children;
  }
  
  public String getIsDir()
  {
    return this.isDir;
  }
  
  public void setIsDir(String isDir)
  {
    this.isDir = isDir;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getSize()
  {
    return this.size;
  }
  
  public void setSize(String size)
  {
    this.size = size;
  }
  
  public String getModtime()
  {
    return this.modtime;
  }
  
  public void setModtime(String modtime)
  {
    this.modtime = modtime;
  }
  
  public String getPath()
  {
    return this.path;
  }
  
  public void setPath(String path)
  {
    this.path = path;
  }
}

