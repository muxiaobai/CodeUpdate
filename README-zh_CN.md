更新工具使用指南





# 使用本地的更新时间来更新

1.添加配置文件到/config/项目简称.xml；配置查找文件的中间件目录;配置相关需要更新的文件类型(后缀)
```
<base_dir>{root}/update/jy_zq_demo/{ver}_{time}/ROOT</base_dir>
<search_dir>D:/tomcat-8.5.32-8095/webapps/ROOT</search_dir>
<search_filter>*.jsp||*.html||*.htm||*.swf||*.xml||*.css||*.js||*.xsl||*.png||*.jpg||*.gif||*.class</search_filter>
<from_dir>D:/tomcat-8.5.32-8095/webapps/ROOT</from_dir>
<to_dir></to_dir>
 ```
 修改search_dir和from_dir的路径

2.配置修改时间（将此时间之后修改的文件加入到更新列表）；在/ctrl/lasttime.properties文件中添加该时间

3.在run.bat文件中添加对应的操作。（不必要）可用`3*替换`

```
echo * 2:生成更新数据 （jy_zq）                 *

if %in%==2 goto :2

:2
start createUpdate.bat jy_zq_svn
goto :begin

```

3*

cd ..\..\ProgramTool\CodeUpdateV2.0

C:\ProgramTool\CodeUpdateV2.0
java ctrl.FileSearch jy_zq

注意事项：上面三项中配置的名字须保证一致。


# 使用svn的更新时间来更新

1.拷贝svnkit-1.9.3.jar 文件到%JAVA_HOME%\lib\svnkit-1.9.3.jar
2.添加系统环境变量 类似：               
 .;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar;%JAVA_HOME%\lib\svnkit-1.9.3.jar
3.查看svn中要更新的版本，找到更新时间
4。更改config/jy_zq_svn.xml中的svn相关配置和base_dir

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
    <search_filter>*.jsp||*.html||*.htm||*.swf||*.xml||*.css||*.js||*.xsl||*.png||*.jpg||*.gif||*.class</search_filter>
    <from_dir>D:/tomcat-8.5.32-8080/webapps/ROOT</from_dir>
    <to_dir></to_dir>
``` 
修改svn_username,svn_password,svn_url,svn_path,svn_time,java_path,web_path和编译路径search_dir,from_dir

# 注意事项

编译路径不能包含中文，配置文件中使用反斜杠/

run : java ctrl.FileSearch jy_zq
