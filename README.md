

------------------------------------------------------------------------------------------------------------------------------------

# Update Tool User Guide


## Use local build time to update

1. Add the configuration file to the /config/ project abbreviation .xml; configure the middleware directory to find the file; configure the file type (suffix) that needs to be updated.

```xml
<base_dir>{root}/update/jy_zq_demo/{ver}_{time}/ROOT</base_dir>
<search_dir>D:/tomcat-8.5.32-8095/webapps/ROOT</search_dir>
<search_filter>*.jsp||*.html||*.htm||*.swf||*.xml||*.css||*.js||*.xsl||*.png||*. jpg||*.gif||*.class</search_filter>
<from_dir>D:/tomcat-8.5.32-8095/webapps/ROOT</from_dir>
<to_dir></to_dir>
```

Modify the path of search_dir and from_dir

 2. Configure the modification time (add the modified file after this time to the update list); add the time in the /ctrl/lasttime.properties file.

 3. Add the corresponding operation to the run.bat file. (unnecessary,and only in window) can be replaced with `3*`

```

Echo * 2: Generate update data (jy_zq_dir) *

If %in%==2 goto :2

:2
Start createUpdate.bat jy_zq_svn
Goto :begin

```

3*

cd ..\..\Code

C:\ProgramTool\Code
java ctrl.FileSearch jy_zq_dir

Note: The names configured in the above three items must be consistent.

## Use svn update time to update

1. Copy the svnkit-1.9.3.jar file to %JAVA_HOME%\lib\svnkit-1.9.3.jar
2. Add system environment variables Similar to:
 .;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar;%JAVA_HOME%\lib\svnkit-1.9.3.jar
3. View the version to be updated in svn and find the update time.
4. Change svn related configuration and base_dir in config/jy_zq_svn.xml

```
<type>svn</type>
<svn_username>{username}</svn_username>
<svn_password>{password}</svn_password>
<svn_url>http://host:port/svn/2018</svn_url>
<svn_path>//web-admin</svn_path>
<svn_time>2019-02-21 14:00:00</svn_time>
<java_path>/src</java_path>
<web_path>/web</web_path>

<base_dir>{root}/update/jy_zq/{ver}_{time}/ROOT</base_dir>
<search_dir>D:/tomcat-8.5.32-8080/webapps/ROOT</search_dir>
<search_filter>*.jsp||*.html||*.htm||*.swf||*.xml||*.css||*.js||*.xsl||*.png||*. Jpg||*.gif||*.class</search_filter>
<from_dir>D:/tomcat-8.5.32-8080/webapps/ROOT</from_dir>
<to_dir></to_dir>
```

Modify svn_username, svn_password, svn_url, svn_path, svn_time, java_path, web_path and compile path search_dir, from_dir

use build path method, 3*

run : java ctrl.FileSearch jy_zq_svn

## Precautions

The compilation path cannot contain Chinese, and the backslash is used in the configuration file.

----------------------------------------------------------------------------------------------------------------------------------------

