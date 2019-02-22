/**
 * Project Name:Code
 * File Name:MUtil.java
 * Package Name:ctrl
 * Date:2019年2月20日下午4:20:23
 * Copyright (c) 2019, All Rights Reserved.
 *
*/

package ctrl;
/**
 * ClassName:MUtil 
 * Function: TODO 
 * Reason:	 TODO 
 * Date:     2019年2月20日 下午4:20:23 
 * @author   Mu Xiaobai
 * @version  
 * @since    JDK 1.8	 
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MUtil
{
  private static String formatCode = "yyyy-MM-dd HH:mm:ss";
  
  public static String date2String(Date date)
  {
    return date2String(date, formatCode);
  }
  
  public static String date2String(Date date, String fStr)
  {
    String rStr = "";
    SimpleDateFormat sf = new SimpleDateFormat(fStr);
    rStr = sf.format(date);
    return rStr;
  }
  
  public static Date string2Date(String d)
  {
    return string2Date(d, formatCode);
  }
  
  public static Date string2Date(String d, String f)
  {
    Date rDate = null;
    SimpleDateFormat sf = new SimpleDateFormat(f);
    try
    {
      rDate = sf.parse(d);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return rDate;
  }
}


