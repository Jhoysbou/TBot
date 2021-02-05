package com.jhoysbou.TBot.utils.validation;

public interface Validator<T> {
    void validate(T t) throws ValidationException;
}
