package com.simple.rpc.main;

import com.simple.rpc.annotations.EnableRPC;
import com.simple.rpc.annotations.RegisterService;
import com.simple.rpc.annotations.StratRPC;
import com.simple.rpc.config.ProxyConfig;
import com.simple.rpc.register.ClientServer;
import com.simple.util.entiy.TCPConfig;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @Author: huangjun
 * @Date: 2022/7/12 10:03
 * @Version 1.0
 */
@Component
@Order(1)
public class RPCMain implements CommandLineRunner, ApplicationContextAware {

    private static final Logger logger = Logger.getLogger(RPCMain.class.getName());

    private  static String registerPackage;

    private  static String hostAddress;

    private  static Integer serverPort;

    private  static String registerIp = null;

    private  static Map<String,Class<?>> serviceMap = new HashMap<>();

    private  static Map<String, TCPConfig> tcpMap = new HashMap<>();

    private  static ApplicationContext context;

    private static String getMainClassName() {
        //获取到启动类
        StackTraceElement[] stackTraceElements = new RuntimeException().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if ("main".equals(stackTraceElement.getMethodName())) {
                return stackTraceElement.getClassName();
            }
        }
        return "";
    }

    private  void initTcpServerClass(){
        //只扫描指定的包路径
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(registerPackage))
                .setScanners(new FieldAnnotationsScanner(),new TypeAnnotationsScanner(),new SubTypesScanner(false)));
        Set<Class<?>> serverClass = reflections.getTypesAnnotatedWith(RegisterService.class);
        for (Class<?> aClass : serverClass) {
            RegisterService service = aClass.getAnnotation(RegisterService.class);
            if (service == null) {
                continue;
            }
            Class<?>[] interfaces = aClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                Method[] methods = anInterface.getMethods();
                for (Method method : methods) {
                    String name = anInterface.getCanonicalName()+"."+ method.getName();
                    serviceMap.put(name, aClass);
                    TCPConfig tcpConfig = new TCPConfig(name,hostAddress,serverPort,Boolean.TRUE);
                    tcpMap.put(name,tcpConfig);
                }
            }
        }
    }

    private  void getHost() {
        try{
            InetAddress addr = InetAddress.getLocalHost();
            hostAddress = addr.getHostAddress();
        }catch (UnknownHostException e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Map<String, TCPConfig> getTcpMap() {
        return tcpMap;
    }

    public static void setTcpMap(Map<String, TCPConfig> tcpMap) {
        RPCMain.tcpMap = tcpMap;
    }

    public static Map<String, Class<?>> getServiceMap() {
        return serviceMap;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }


    @Override
    public void run(String... args) throws Exception {
        String mainClassName = getMainClassName();
        Class<?> aClass = null;
        try {
            aClass = ClassLoader.getSystemClassLoader().loadClass(mainClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (aClass == null){
            throw new RuntimeException("can not find main class");
        }

        EnableRPC enabletcp = aClass.getAnnotation(EnableRPC.class);
        if (enabletcp != null){
            String referencePackage = enabletcp.referencePackage();
            if ("".equals(referencePackage.replaceAll("\\s*|\t|\r|\n", ""))){
                throw new RuntimeException("@com.simple.tcp.annotations.EnableTCP.referencePackage can not be empty");
            }
            String clientRegisterIp = enabletcp.clientRegisterIp();
            if ("".equals(clientRegisterIp.replaceAll("\\s*|\t|\r|\n", ""))){
                throw new RuntimeException("@com.simple.tcp.annotations.StratTCP.clientRegisterIp can not be empty");
            }
            registerIp = clientRegisterIp;
            //为service对象注入代理
            ProxyConfig.initReference(context,referencePackage);
        }

        StratRPC annotation = aClass.getAnnotation(StratRPC.class);
        //根据启动类上是否存在注解判断是否启动tcp
        if (annotation != null){
            registerPackage = annotation.registerPackage();
            if ("".equals(registerPackage.replaceAll("\\s*|\t|\r|\n", ""))){
                throw new RuntimeException("@com.simple.tcp.annotations.StratTCP.registerPackage can not be empty");
            }

            String serverRegisterIp = annotation.serverRegisterIp();
            if ("".equals(serverRegisterIp.replaceAll("\\s*|\t|\r|\n", ""))){
                throw new RuntimeException("@com.simple.tcp.annotations.StratTCP.serverRegisterIp can not be empty");
            }

            serverPort = annotation.serverPort();
            if (serverPort == 0){
                throw new RuntimeException("@com.simple.tcp.annotations.StratTCP.serverPort can not be empty");
            }
            if (serverPort == 9999){
                throw new RuntimeException("Regiter center has use this port.Please use other port");
            }

            getHost();

            //初始化服务类
            initTcpServerClass();



            //启动线程监听
            Execute.startTpcServer(hostAddress,serverPort);

            if (registerIp != null && serverRegisterIp.equals(registerIp)){
                throw new RuntimeException("@com.simple.tcp.annotations.StratTCP.serverRegisterIp must equals @com.simple.tcp.annotations.EnableTCP.clientRegisterIp");
            }
            registerIp = serverRegisterIp;
        }

        // 启动netty,发布/订阅服务
        ClientServer.run(registerIp,tcpMap);
    }
}
