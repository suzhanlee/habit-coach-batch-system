package com.example.demo.domain.service;

public interface Validator<T> {

    boolean isValid(T t);
}
