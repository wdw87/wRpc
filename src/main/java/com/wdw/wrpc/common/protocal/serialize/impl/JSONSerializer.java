package com.wdw.wrpc.common.protocal.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.wdw.wrpc.common.protocal.serialize.Serializer;
import com.wdw.wrpc.common.protocal.serialize.SerializerType;

public class JSONSerializer implements Serializer {
    @Override
    public Byte getSerializeType() {
        return SerializerType.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
