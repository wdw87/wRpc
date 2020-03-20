package com.wdw.wrpc.common.protocal.serialize;


import com.wdw.wrpc.common.protocal.serialize.impl.JSONSerializer;

public interface Serializer {

    Serializer DEFAULT  = new JSONSerializer();

    Byte getSerializeType();

    byte[] serialize(Object object);

    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
