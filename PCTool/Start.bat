@echo off
::color 0a
title PC工具
echo ★☆ ★☆ ★☆ ★☆ ★☆★☆★☆ ★☆ ★☆ ★☆ ★☆★
echo ★☆ ★☆ ★☆ ★☆ ★☆★☆★☆ ★☆ ★☆ ★☆ ★☆★
echo.★☆                                              ☆★
echo.★☆                                              ☆★
echo.★☆   触屏映射开启中，请稍等......               ☆★
echo ★☆                                              ☆★
echo.★☆                                              ☆★
echo ★☆ ★☆ ★☆ ★☆ ★☆★☆★☆ ★☆ ★☆ ★☆ ★☆★
echo ★☆ ★☆ ★☆ ★☆ ★☆★☆★☆ ★☆ ★☆ ★☆ ★☆★
echo=
echo 开启触屏映射，速度由手机而定。在没看到结尾信息时 
echo 请勿关闭本窗口。
echo=
echo 配置环境
set Path=%cd%\adb;%Path%
echo %cd%\adb
echo=
echo 重启adb服务
adb kill-server
adb start-server
echo=
echo 扫描adb设备
adb devices
echo=
adb shell echo 关闭SELinux;setenforce 0;echo 删除gamepadtool;rm -Rf /data/local/tmp/.gamepadtool;echo 创建gamepadtool;mkdir /data/local/tmp/.gamepadtool;echo 修改权限;chmod 777 /data/local/tmp/.gamepadtool;echo 创建dalvik;mkdir /data/local/tmp/.gamepadtool/dalvik-cache;echo 修改dal;chmod 777 /data/local/tmp/.gamepadtool/dalvik-cache;echo cp jar文件;cp /data/data/com.doubleghost.injecttest/files/InjectServer.jar /data/local/tmp/.gamepadtool;echo dd拷贝jar;dd if=/data/data/com.doubleghost.injecttest/files/InjectServer.jar of=/data/local/tmp/.gamepadtool/InjectServer.jar;echo 修改jar权限;chmod 777 /data/local/tmp/.gamepadtool/InjectServer.jar;echo 修改jar所有者;chown shell /data/local/tmp/.gamepadtool/InjectServer.jar;echo 环境;export CLASSPATH=/data/local/tmp/.gamepadtool/InjectServer.jar;export ANDROID_DATA=/data/local/tmp/.gamepadtool;trap "" HUP;echo 启动服务，可能需要1分钟左右;exec app_process /data/local/tmp/.gamepadtool com.doubleghost.inject.InjectServer &
echo=
echo 执行完毕，请按任意键退出。
echo=
pause
