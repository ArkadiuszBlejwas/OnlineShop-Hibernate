package com.capgemini.store.persistence.service;

import com.capgemini.store.persistence.service.exception.ArgumentIsNullException;

public interface Service {

    default void checkWhetherArgumentIsNull(Object object){
        if (object == null){
            throw new ArgumentIsNullException();
        }
    }

    default void checkWhetherArgumentIsNull(Object object1, Object object2){
        if (object1 == null || object2 == null){
            throw new ArgumentIsNullException();
        }
    }
}
