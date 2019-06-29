package com.capgemini.store.persistence.service.exception;


//@NoArgsConstructor
public class TransactionCantBeExecutedException extends IllegalArgumentException {

    public TransactionCantBeExecutedException(String message){
        super(message);
    }
}
