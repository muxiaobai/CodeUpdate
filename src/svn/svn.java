/**
 * Project Name:Code
 * File Name:svn.java
 * Package Name:svn
 * Date:2019年2月21日上午9:08:32
 * Copyright (c) 2019, All Rights Reserved.
 *
*/

package svn;
/**
 * ClassName:svn 
 * Function: TODO 
 * Reason:	 TODO 
 * Date:     2019年2月21日 上午9:08:32 
 * @author   Mu Xiaobai
 * @version  
 * @since    JDK 1.8	 
 */
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * svn获取
 *
 * @author youzongxu
 * @date 2018/9/7
 */
public class svn {
    private String userName = "";
    private String password = "";
    private String urlString = "";
    /**
     * 临时文件
     */
    private String tempDir = System.getProperty("java.io.tmpdir");
    private DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
    private Random random = new Random();

    private SVNRepository repos;
    private ISVNAuthenticationManager authManager;
    public svn(){
    }
    public svn(String u,String p,String url) {
        try {
            userName = u;
            password = p;
            urlString = url;
            init();
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    public void init() throws SVNException{
        authManager = SVNWCUtil.createDefaultAuthenticationManager(new File(tempDir+"/auth"), userName, password.toCharArray());
        options.setDiffCommand("-x -w");
        repos = SVNRepositoryFactory.create(SVNURL
                .parseURIEncoded(urlString));
        repos.setAuthenticationManager(authManager);
    }

    public static void  main(String[] args) throws Exception{  
        Map tMap = new HashMap<String, Object>();
        svn svn = new svn(tMap.get("svn_username").toString(), tMap.get("svn_password").toString(), tMap.get("svn_url").toString()+tMap.get("svn_path").toString());
        try {
            Map map = new HashMap<String,Object>();
            map.put("", "");
            System.out.println(svn.getChangeFileList(svn,map,System.currentTimeMillis()));
        } catch (Exception e) {
            e.printStackTrace();
        }

//        for(SVNLogEntry svnLog: svnLogs){
//            System.out.println("-------------------------------");
//            System.out.println("Revision:"+svnLog.getRevision()+",Author:"+svnLog.getAuthor()+",Date:"+sDf.format(svnLog.getDate())+",message:"+svnLog.getMessage());
//            System.out.println(svnLog.getChangedPaths());
//        }
//        svn.getChangeLog(startDate,endDate);
    }
    
    public List getChangeFileList(svn svn,Map tMap,long time) throws Exception{
        SimpleDateFormat sDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String svn_time  = tMap.get("svn_time").toString();
        Date startDate = sDf.parse(svn_time);
//        Date startDate = new Date(time);
//      Date endDate = sDf.parse("2019-02-20 19:00:00");
        Date endDate = new Date();
        SVNLogEntry[] svnLogs =svn.getLogByTime(startDate,endDate);
        List files = svn.getChangeFileList(svnLogs,tMap);
//        Iterator<String>tmp=files.iterator();
//        while(tmp.hasNext()){
//             System.out.println(tmp.next());
//        }
        return files;
    }
    public List getChangeFileList(SVNLogEntry[] SVNLogEntries,Map tMap) throws SVNException{
        String path = tMap.get("svn_path").toString();
        String java_path = tMap.get("java_path").toString();
        String web_path = tMap.get("web_path").toString();
        String search_dir  = tMap.get("search_dir").toString();
        System.out.println("-------------------------------");
        List ls = new ArrayList();
        for(int i=0;i<SVNLogEntries.length;i++){
//            System.out.println("Revision:"+SVNLogEntries[i].getRevision());
//            System.out.println(SVNLogEntries[i].getChangedPaths());
            Map<String,SVNLogEntryPath> myMap = SVNLogEntries[i].getChangedPaths();
            for(String key:myMap.keySet()){
                String val = key;
                String suffix = val.substring(val.lastIndexOf(".")+1);
                Boolean flag = false;
                flag  = Pattern.matches("java", suffix);
//                System.out.println("key="+key+",suffix:"+suffix+",flag:"+flag);
                //is java file replace all path
                if(flag){
                    val = val.replaceAll(path+java_path, search_dir+"/WEB-INF/classes");
                    val = val.substring(0, val.lastIndexOf(".")+1)+"class";
                }else{
                    val = val.replaceAll(path+web_path, search_dir);

                }
                //De-reprocessing
                if(!ls.contains(val)){
                    ls.add(val);
                }
            }
        }
        
        return ls;
    }
    public String getFilePath(){
//        Pattern.matches(this.search_filter, pFile.getPath()
        return "";
    }
    /**获取一段时间内，所有的commit记录
     * @param st    开始时间
     * @param et    结束时间
     * @return
     * @throws SVNException
     */
    public SVNLogEntry[] getLogByTime(Date st, Date et) throws SVNException{
        long startRevision = repos.getDatedRevision(st);
        long endRevision = repos.getDatedRevision(et);
        System.out.println("startRevision:"+startRevision+",endRevision:"+endRevision);
        @SuppressWarnings("unchecked")
        Collection<SVNLogEntry> logEntries = repos.log(new String[]{""}, null,
                startRevision, endRevision, true, true);
        SVNLogEntry[] svnLogEntries = logEntries.toArray(new SVNLogEntry[0]);
//        SVNLogEntry[] svnLogEntries1 = Arrays.copyOf(svnLogEntries, svnLogEntries.length - 1);
        return svnLogEntries;
    }

    public File getChangeLog(Date st, Date et) throws SVNException {
        long startRevision = repos.getDatedRevision(st);
        long endRevision = repos.getDatedRevision(et);
        return getChangeLog(startRevision,endRevision);
    }
    /**获取版本比较日志，并存入临时文件
     * @param startVersion
     * @param endVersion
     * @return
     */
    public File getChangeLog(long startVersion, long endVersion) {
        SVNDiffClient diffClient = new SVNDiffClient(authManager, options);
        diffClient.setGitDiffFormat(true);
        File tempLogFile;
        OutputStream outputStream = null;
        String svnDiffFile;
        do {
            svnDiffFile = tempDir + "/svn_diff_file_"+startVersion+"_"+endVersion+"_"+random.nextInt(10000)+".txt";
            tempLogFile = new File(svnDiffFile);
        } while (tempLogFile != null && tempLogFile.exists());
        try {
            tempLogFile.createNewFile();
            outputStream = new FileOutputStream(svnDiffFile);
            diffClient.doDiff(SVNURL.parseURIEncoded(urlString),
                    SVNRevision.create(startVersion),
                    SVNURL.parseURIEncoded(urlString),
                    SVNRevision.create(endVersion),
                    SVNDepth.UNKNOWN, true, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(outputStream!=null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tempLogFile;
    }

    public boolean getBoolean(String filePath){
        String[] k = {".java", ".html", ".css", ".js", ".jsp", ".properties",".xml",".json",".sql",".wxml",".wxss"};
        List<String> strings = Arrays.asList(k);
        boolean ba = false;
        c:for (String ls:strings) {
            if(filePath.contains(ls)){
                ba = true;
                break c;
            }
        }
        return ba;
    }

}