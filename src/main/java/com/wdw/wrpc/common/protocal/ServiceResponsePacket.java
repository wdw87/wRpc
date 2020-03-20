package com.wdw.wrpc.common.protocal;


import com.wdw.wrpc.common.protocal.codec.PacketType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceResponsePacket extends Packet {
    /**
     *  请求id
     */
    private String requestId;
    /**
     * 服务返回编码，0-成功，非0失败
     */
    private int code = 0;
    /**
     * 具体错误信息
     */
    private String message;
    /**
     * 返的数据
     */
    private Object data;

    @Override
    public byte getType() {
        return PacketType.SERVICE_RESPONSE;
    }
}
