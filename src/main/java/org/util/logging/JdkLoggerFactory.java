package org.util.logging;

import java.util.logging.Logger;

/**
 * @author ahmad
 */
public class JdkLoggerFactory extends InternalLoggerFactory {

    @Override
    public InternalLogger newInstance(String name) {
        return new JdkLogger(Logger.getLogger(name));
    }

}
