package com.github.starnowski.posjsonhelper.core;

import static java.lang.System.getProperty;

/**
 * TODO Test
 */
public class SystemPropertyReader {

    public String read(String property){
        return getProperty(property);
    }
}
