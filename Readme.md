# wRpc

> 一个简单的RPC框架demo

## 1. 技术选型

1. Spring：框架基于Spring，通过xml文件配置service等相关参数
2. netty：使用异步IO，支持高并发
3. Zookeeper：提供服务注册以及服务发现功能
4. 反向代理：提供了jdk和cglib两种实现，可在consumer配置文件中配置
5. 负载均衡：目前实现了权重随机和一致性hash，待完善

## 2.目录结构

~~~java
.
├── pom.xml
├── src
│   └── main
│       ├── java
│       │   └── com.wdw.wrpc
│       │               ├── client	//客户端相关代码
│       │               │   ├── loadbalance	//负载均衡
│       │               │   │   └── LoadBalance.java
│       │               │   ├── netty	//netty客户端
│       │               │   │   ├── HeartbeatHandler.java	//心跳检测
│       │               │   │   ├── NettyClient.java	//netty客户端
│       │               │   │   └── ServiceResponseHandler.java
│       │               │   ├── proxy	//动态代理
│       │               │   │   ├── impl
│       │               │   │   │   ├── CGLIBProxyFactory.java	//cglib实现
│       │               │   │   │   ├── InvokerInterceptor.java
│       │               │   │   │   ├── InvokerInvocationHandler.java
│       │               │   │   │   ├── JDKProxyFactory.java	//jdk实现
│       │               │   │   │   └── ProxyManager.java
│       │               │   │   └── ProxyFactory.java
│       │               │   └── registry	//注册中心
│       │               │       └── ServiceDiscovery.java
│       │               ├── common	//客户端与服务端公共部分
│       │               │   ├── protocal	//协议相关
│       │               │   │   ├── codec
│       │               │   │   │   ├── Decoder.java
│       │               │   │   │   ├── Encoder.java
│       │               │   │   │   ├── PacketType.java
│       │               │   │   │   └── Spliter.java
│       │               │   │   ├── HeartbeatPacket.java
│       │               │   │   ├── PacketCodec.java
│       │               │   │   ├── Packet.java
│       │               │   │   ├── serialize	//序列化
│       │               │   │   │   ├── impl
│       │               │   │   │   │   └── JSONSerializer.java
│       │               │   │   │   ├── Serializer.java
│       │               │   │   │   └── SerializerType.java
│       │               │   │   ├── ServiceDescriptor.java	//Spring框架版本该类未使用
│       │               │   │   ├── ServiceRequestPacket.java
│       │               │   │   └── ServiceResponsePacket.java
│       │               │   └── util	//工具类
│       │               │       ├── ClassUtils.java
│       │               │       ├── IDUtil.java
│       │               │       ├── IPUtil.java
│       │               │       ├── ReflectionUtil.java
│       │               │       └── SpringUtil.java
│       │               ├── config	//配置相关，目前客户端和服务端的配置都集中在这里
│       │               │   ├── ApplicationConfig.java
│       │               │   ├── ClientConfig.java
│       │               │   ├── NameSpaceHandler.java	//Spring自定义命名空间handler
│       │               │   ├── ReferenceConfig.java
│       │               │   ├── RegistryConfig.java
│       │               │   ├── RpcContext.java
│       │               │   ├── ServerConfig.java
│       │               │   ├── ServiceConfig.java
│       │               │   └── SimpleBeanDefinitionParser.java	//自定义Bean解析
│       │               ├── example	//测试用目录，客户端与服务端代码在此
│       │               │   ├── Client.java
│       │               │   └── Server.java
│       │               ├── server	//服务端代码在此
│       │               │   ├── netty	//netty服务端
│       │               │   │   ├── HeartbeatHandler.java
│       │               │   │   ├── NettyServer.java
│       │               │   │   ├── ServerIdleHandler.java
│       │               │   │   ├── ServiceInstance.java
│       │               │   │   ├── ServiceInvoker.java
│       │               │   │   ├── ServiceManager.java	//Spring框架版本该类没有使用
│       │               │   │   └── ServiceRequestHandler.java
│       │               │   └── registry	//注册中心
│       │               │       └── ServiceRegistry.java
│       │               └── service	//测试用服务文件在此
│       │                   ├── CalculateInterFace.java
│       │                   ├── impl
│       │                   │   ├── CalculateImpl.java
│       │                   │   └── MultiplyImpl.java
│       │                   └── MultiplyInterface.java
│       └── resources
│           ├── consumer.xml	//客户端配置文件
│           ├── log4j.properties
│           ├── META-INF
│           │   ├── spring.handlers
│           │   ├── spring.schemas
│           │   └── wrpc.xsd
│           └── provider.xml	//服务端配置文件
└── wprc-core.iml

~~~

## 3. 参考

* [dubbo参考文档](http://dubbo.apache.org/zh-cn/docs/user/quick-start.html)
* [【剖析 | SOFARPC 框架】系列](https://www.yuque.com/sofarpclab/article/rgrtrg)
* [RPC基本原理以及如何使用netty实现RPC](https://juejin.im/post/5c6d7640f265da2de80f5e9c#heading-1)
* ...