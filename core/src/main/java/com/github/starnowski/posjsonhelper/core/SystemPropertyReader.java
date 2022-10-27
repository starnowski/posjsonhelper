package com.github.starnowski.posjsonhelper.core;

import static java.lang.System.getProperty;

public class SystemPropertyReader {

    public String read(String property) {
        return getProperty(property);
    }
}
