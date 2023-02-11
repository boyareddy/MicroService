package com.roche.swam.labsimulator.common.ui;

public interface EventHandler<T> {
    void  handle(T event);
}
