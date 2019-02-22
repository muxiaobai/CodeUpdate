color 70
echo off
:begin
cls
echo ****************************************************************************
echo * 1:生成更新数据 this       					*
echo * 2:生成更新数据 （jy_zq_svn）			        *

echo * u:更新数据                                            		*
echo * 0:退出                                                		*
echo ****************************************************************************

set /p in=
if %in%==1 goto :1
if %in%==2 goto :2

if %in%==u goto :UPDATE
if %in%==a goto :ALL
if %in%==0 goto :end

:1
start createUpdate.bat this
goto :begin

:2
start createUpdate.bat jy_zq_svn
goto :begin

:ALL
start createUpdate.bat
goto :begin

:UPDATE
start runUpdate.bat
goto :begin
:3
goto :end
:end
exit