package org.util.logging;

/**
 * @author ahmad
 */
public abstract class InternalLoggerFactory {

    private static volatile InternalLoggerFactory defaultFactory = newDefaultFactory(InternalLoggerFactory.class.getName());

    private static InternalLoggerFactory newDefaultFactory(String name) {
        InternalLoggerFactory f;
        try {
            f = new Log4JLoggerFactory();
            f.newInstance(name).debug("Using Log4J as the default logging framework");
        } catch (Throwable t) {
            f = new JdkLoggerFactory();
            f.newInstance(name).debug("Using java.util.logging as the default logging framework");
        }
        return f;
    }

    public static InternalLoggerFactory getDefaultFactory() {
        return defaultFactory;
    }

    public static void setDefaultFactory(InternalLoggerFactory defaultFactory) {
        if (defaultFactory == null) {
            throw new NullPointerException("defaultFactory");
        }
        InternalLoggerFactory.defaultFactory = defaultFactory;
    }

    public static InternalLogger getInstance(Class<?> clazz) {
        return getInstance(clazz.getName());
    }

    public static InternalLogger getInstance(String name) {
        return getDefaultFactory().newInstance(name);
    }

    protected abstract InternalLogger newInstance(String name);

}
