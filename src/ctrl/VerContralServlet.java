/**
 * Project Name:Code
 * File Name:VerContralServlet.java
 * Package Name:ctrl
 * Date:2019年2月20日下午4:21:05
 * Copyright (c) 2019, All Rights Reserved.
 *
*/

package ctrl;
/**
 * ClassName:VerContralServlet 
 * Function: TODO 
 * Reason:	 TODO 
 * Date:     2019年2月20日 下午4:21:05 
 * @author   Mu Xiaobai
 * @version  
 * @since    JDK 1.8	 
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VerContralServlet
  extends HttpServlet
{
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    doPost(req, resp);
  }
  
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    List pathList = new ArrayList();
    FileSearch fs = new FileSearch();
    String[] paths = req.getParameterValues("path");
    for (int i = 0; i < paths.length; i++)
    {
      String path = paths[i];
      
      pathList.add(path);
    }
    pathList.size();
    
    RequestDispatcher rd = req.getRequestDispatcher("/verInfo.jsp");
    rd.forward(req, resp);
  }
}

