## 传统httpclient的痛点
>  1．	Apache Http Client 4.+
   这个应该是大家最常用的版本，这个版本存在着一些问题，包括高版本和低版本兼容问题，使用难度极高（很容易出错，导致close_wait或者time_wait激增）。
   2．	Java Connection
   这个是Java SE版本的实现，这个api是低级的http client api，许多功能需要自己实现，比如http code 301跳转，编码解码问题，未池化，不支持异步操作等。
   
## HttpClient
>  该项目基于JAX-RS 2.1标准协议进行拓展，使用Jersey实现，使用Netty9作为Container，支持同步Http请求和异步Http请求，Api采用回调方式拓展性极强.

## 性能测试
>  性能测试用例见：https://github.com/JavaXiaoJun/HttpClient/blob/master/src/test/java/ClientTest.java
   观察本机和服务端的tcp连接数，发现无论运行多少趟，TCP四元组不发生变化，表明链接得到的复用，不会出现大量time_wait和close_wait的情况。
