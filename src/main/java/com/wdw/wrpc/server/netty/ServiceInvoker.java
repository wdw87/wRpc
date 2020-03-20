package com.wdw.wrpc.server.netty;


import com.alibaba.fastjson.JSON;
import com.wdw.wrpc.common.protocal.ServiceRequestPacket;
import com.wdw.wrpc.common.util.ReflectionUtil;

import java.lang.reflect.Method;

public class ServiceInvoker {

    public static final ServiceInvoker INSTANCE = new ServiceInvoker();

    public Object invoke(ServiceInstance serviceInstance, ServiceRequestPacket serviceRequestPacket) {
        Object instance = serviceInstance.getTarget();
        Method method = serviceInstance.getMethod();
        Object[] args = serviceRequestPacket.getArgs();
        return ReflectionUtil.invoke(instance, method, args);
    }

    public Object invoke(Object object, Method method, ServiceRequestPacket serviceRequestPacket) {
        Object[] args = serviceRequestPacket.getArgs();
        Class<?>[] argTypes = serviceRequestPacket.getParameterTypes();
        Object[] realArgs = parseParams(argTypes, args);
        return ReflectionUtil.invoke(object, method, realArgs);
    }

    private Object[] parseParams(Class<?>[] paramTypes, Object[] params) {
        if (params == null || params.length == 0) {
            return params;
        }
        Object[] newParams = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            newParams[i] = JSON.parseObject(JSON.toJSONString(params[i]), paramTypes[i]);
        }
        return newParams;
    }

}
