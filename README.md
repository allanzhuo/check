java实现服务器的自动巡检，故障通知。
操作简单就几个文件，避免了成熟软件的冗陈安装步骤。

target是编译后文件，懒得删了，代码在src里

起因:

之前一直是手动的巡检服务器，然后贴图，最近服务器数量大增，有点忙不过来了。因为一直用的java，对shell脚本不是特别了解，所以这次用java写了个小项目，实现对多服务器，多任务的巡检，巡检结果有故障的会通过邮件通知。

Cnblogs 地址：http://www.cnblogs.com/allanzhang/p/6801467.html

GitHub 地址 : https://github.com/allanzhuo/check

OSChina 地址 : http://git.oschina.net/allanzhang/save-xj

使用技术:
spring+jsch+quartz

更多介绍：http://www.cnblogs.com/allanzhang/p/6801467.html
