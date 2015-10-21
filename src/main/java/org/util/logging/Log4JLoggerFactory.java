package org.util.logging;

import org.apache.log4j.Logger;

/**
 * @author ahmad
 */
public class Log4JLoggerFactory extends InternalLoggerFactory {

    @Override
    public InternalLogger newInstance(String name) {
        return new Log4JLogger(Logger.getLogger(name));
    }

}