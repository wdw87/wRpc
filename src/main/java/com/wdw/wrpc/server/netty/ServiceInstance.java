package com.wdw.wrpc.server.netty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInstance {
    private Object target;
    private Method method;
}
