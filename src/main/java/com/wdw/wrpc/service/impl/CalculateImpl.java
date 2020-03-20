package com.wdw.wrpc.service.impl;

import com.wdw.wrpc.service.CalculateInterFace;

public class CalculateImpl implements CalculateInterFace {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int dec(int a, int b) {
        return a - b;
    }
}
