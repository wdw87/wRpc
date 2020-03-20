package com.wdw.wrpc.service.impl;

import com.wdw.wrpc.service.MultiplyInterface;

public class MultiplyImpl implements MultiplyInterface {
    @Override
    public Double multiply(Double a, Double b) {
        return a * b;
    }
}
