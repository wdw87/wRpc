package com.wdw.wrpc.client.loadbalance;

import com.wdw.wrpc.client.registry.Invoker;
import com.wdw.wrpc.common.protocal.ServiceRequestPacket;

import java.util.List;

/**
 * 负载均衡
 */
public interface LoadBalance {
    Invoker select(List<Invoker> invokers, ServiceRequestPacket requestPacket);
}
