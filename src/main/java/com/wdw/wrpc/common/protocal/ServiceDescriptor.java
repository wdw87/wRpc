package com.wdw.wrpc.common.protocal;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

@Data
@NoArgsConstructor
public class ServiceDescriptor {
    private String clazz;
    private String method;
    private String[] parameterTypes;
    private String returnType;

    public static ServiceDescriptor from(Class<?> clazz, Method method){
        ServiceDescriptor descriptor = new ServiceDescriptor();
        descriptor.setClazz(clazz.getName());
        descriptor.setMethod(method.getName());
        Class<?>[] params = method.getParameterTypes();
        String[] strs = new String[params.length];
        for(int i = 0; i < params.length; i++){
            strs[i] = params[i].getClass().getName();
        }
        descriptor.setParameterTypes(strs);
        descriptor.setReturnType(method.getReturnType().getName());

        return descriptor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceDescriptor that = (ServiceDescriptor) o;
        return Objects.equals(clazz, that.clazz) &&
                Objects.equals(method, that.method) &&
                Arrays.equals(parameterTypes, that.parameterTypes) &&
                Objects.equals(returnType, that.returnType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(clazz, method, returnType);
        result = 31 * result + Arrays.hashCode(parameterTypes);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceDescriptor{" +
                "clazz='" + clazz + '\'' +
                ", method='" + method + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", returnType='" + returnType + '\'' +
                '}';
    }
}
