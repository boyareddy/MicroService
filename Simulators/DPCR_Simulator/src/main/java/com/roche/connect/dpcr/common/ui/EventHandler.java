package com.roche.connect.dpcr.common.ui;

public interface EventHandler<T> {
    void  handle(T event);
}
