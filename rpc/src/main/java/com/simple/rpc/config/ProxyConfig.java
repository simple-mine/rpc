package com.simple.rpc.config;

import com.simple.rpc.annotations.Reference;
import com.simple.rpc.factory.AbstractInterfaceFactory;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @Author: huangjun
 * @Date: 2022/7/11 18:41
 * @Version 1.0
 */
@Component
public class ProxyConfig{

    public static void initReference(ApplicationContext applicationContext,String referencePackage){
        //反射工具包，指明扫描路径
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(referencePackage))
                .setScanners(new FieldAnnotationsScanner()));

        Set<Field> fields = reflections.getFieldsAnnotatedWith(Reference.class);
        for (Field field : fields) {
            //获取需要远程调用的属性字段
            Reference reference = field.getAnnotation(Reference.class);
            if (reference != null) {
                try {
                    //获取代理对象
                    Object webService = AbstractInterfaceFactory.getInstance().getWebService(field.getType());
                    //允许字段赋值
                    field.setAccessible(true);
                    Object bean;
                    if (applicationContext == null){
                        //非容器化启动时
                        bean = field.getDeclaringClass().newInstance();
                    }else {
                        //获取容器里该字段的类
                        bean = applicationContext.getBean(field.getDeclaringClass());
                    }
                    //对字段进行赋值
                    field.set(bean, webService);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
