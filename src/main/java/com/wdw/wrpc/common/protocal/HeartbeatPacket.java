package com.wdw.wrpc.common.protocal;

import static com.wdw.wrpc.common.protocal.codec.PacketType.HEARTBEAT;

public class HeartbeatPacket extends Packet {
    @Override
    public byte getType() {
        return HEARTBEAT;
    }
}
