package com.wdw.wrpc.common.protocal;

import lombok.Data;

@Data
public abstract class Packet {

    private Byte version = 1;

    public abstract byte getType();

}
