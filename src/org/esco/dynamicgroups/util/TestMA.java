package org.esco.dynamicgroups.util;

import org.apache.log4j.Logger;

public class TestMA {
    public static void main(final String args[]) {
        Logger l = Logger.getLogger(TestMA.class);
        for (int i = 0; i < 1000; i ++) {
            l.info("i=" + i);
        }
    }
    
}
