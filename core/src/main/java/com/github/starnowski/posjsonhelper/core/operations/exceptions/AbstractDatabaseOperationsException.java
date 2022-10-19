package com.github.starnowski.posjsonhelper.core.operations.exceptions;

public abstract class AbstractDatabaseOperationsException extends Exception{

    protected AbstractDatabaseOperationsException(String message){
        super(message);
    }
}
