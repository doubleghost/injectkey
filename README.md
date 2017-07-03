# injectkey

##android事件注入，非root方式注入触摸事件。

这个功能是一个android手柄手游工具的一部分，用于注入触摸事件。这个功能可以用在一些需要自动化操作android手机的地方，比如自动化测试，或者自动化刷榜等。

##PCTool:

一个PC使用的adb命令工具，用于调起我们的触屏映射进程（injectserver）。PCTool用于非root手机，如果手机已root，不需要使这个工具。

##injectserver：

进行触摸事件注入，如果是root手机启动，它就具有root权限，如果是adb启动，则具有system权限。

##injectservertest：

测试程序，通过socket往injectserver发送触模事件指令。

#使用说明：

1.安装injectservertest应用到手机，并启动。

2.如果是已root手机，直接点击开始键值映射，直到提示映射成功。

3.如果是没有root的手机，需要双击PCTool工具里面的Start.bat。直接到提示injectservce success!，这时在应用上点击开始键值映射，会直接提示已成功。

4.点击测试。按home键回桌面，会看到桌面在自动划动。
