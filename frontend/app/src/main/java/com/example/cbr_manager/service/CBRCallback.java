package com.example.cbr_manager.service;

public interface CBRCallback<T> {
    void onResponse(T response);

    void onFailure(Throwable t);
}