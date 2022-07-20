# rpc
这是一个模仿dubbo的简易RPC项目

## 项目模块介绍
### register
这是一个注册中心，用于发现和管理服务
### rpc
这是整个rpc框架的核心代码
### util
这是整个rpc框架所需要的工具类
### server
这是一个用于测试的服务提供者
### client
这是一个用于测试的服务消费者
### api
这是服务消费者和服务提供者的公共接口

## 使用方式
1.在client模块和server模块引入rpc模块和api模块的依赖
2.在service模块的启动类上使用@StratRPC注解按照属性声明基础配置
3.在service模块的接口实现类上使用@RegisterService注解标识需要代理的类
4.在client模块的启动类上使用@EnableRPC注解按照属性声明基础配置
5.在client模块使用@Reference注解表明需要进行远程调用的接口

## 启动方式
1.将代码拉取到本地后，先启动register项目
2.启动server服务
3.启动client服务

## 测试方式
在client模块保留了一个对外接口，可以通过localhost:8000/hello/say?world=123进行测试


