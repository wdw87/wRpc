package com.wdw.wrpc.common.protocal;

import com.wdw.wrpc.common.protocal.codec.PacketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestPacket extends Packet {

    private String id;

    private String className;

    private String methodName;

    private Class<?>[] parameterTypes;

    //private ServiceDescriptor serviceDescriptor;

    private Object[] args;

    private Class<?> returnType;


    @Override
    public byte getType() {
        return PacketType.SERVICE_REQUEST;
    }

    @Override
    public String toString() {
        return "ServiceRequestPacket{" +
                "id='" + id + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
