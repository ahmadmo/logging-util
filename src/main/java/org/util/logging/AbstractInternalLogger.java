package org.util.logging;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author ahmad
 */
public abstract class AbstractInternalLogger implements InternalLogger, Serializable {

    private static final long serialVersionUID = 5772701792466690309L;

    private final String name;

    protected AbstractInternalLogger(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean isEnabled(InternalLogLevel level) {
        switch (level) {
            case TRACE:
                return isTraceEnabled();
            case DEBUG:
                return isDebugEnabled();
            case INFO:
                return isInfoEnabled();
            case WARN:
                return isWarnEnabled();
            case ERROR:
                return isErrorEnabled();
            default:
                throw new Error();
        }
    }

    @Override
    public void log(InternalLogLevel level, String msg, Throwable cause) {
        switch (level) {
            case TRACE:
                trace(msg, cause);
                break;
            case DEBUG:
                debug(msg, cause);
                break;
            case INFO:
                info(msg, cause);
                break;
            case WARN:
                warn(msg, cause);
                break;
            case ERROR:
                error(msg, cause);
                break;
            default:
                throw new Error();
        }
    }

    @Override
    public void log(InternalLogLevel level, String msg) {
        switch (level) {
            case TRACE:
                trace(msg);
                break;
            case DEBUG:
                debug(msg);
                break;
            case INFO:
                info(msg);
                break;
            case WARN:
                warn(msg);
                break;
            case ERROR:
                error(msg);
                break;
            default:
                throw new Error();
        }
    }

    @Override
    public void log(InternalLogLevel level, String format, Object arg) {
        switch (level) {
            case TRACE:
                trace(format, arg);
                break;
            case DEBUG:
                debug(format, arg);
                break;
            case INFO:
                info(format, arg);
                break;
            case WARN:
                warn(format, arg);
                break;
            case ERROR:
                error(format, arg);
                break;
            default:
                throw new Error();
        }
    }

    @Override
    public void log(InternalLogLevel level, String format, Object argA, Object argB) {
        switch (level) {
            case TRACE:
                trace(format, argA, argB);
                break;
            case DEBUG:
                debug(format, argA, argB);
                break;
            case INFO:
                info(format, argA, argB);
                break;
            case WARN:
                warn(format, argA, argB);
                break;
            case ERROR:
                error(format, argA, argB);
                break;
            default:
                throw new Error();
        }
    }

    @Override
    public void log(InternalLogLevel level, String format, Object... arguments) {
        switch (level) {
            case TRACE:
                trace(format, arguments);
                break;
            case DEBUG:
                debug(format, arguments);
                break;
            case INFO:
                info(format, arguments);
                break;
            case WARN:
                warn(format, arguments);
                break;
            case ERROR:
                error(format, arguments);
                break;
            default:
                throw new Error();
        }
    }

    protected Object readResolve() throws ObjectStreamException {
        return InternalLoggerFactory.getInstance(name());
    }

    @Override
    public String toString() {
        return simpleClassName(getClass()) + '(' + name() + ')';
    }

    private static String simpleClassName(Class<?> clazz) {
        String className = Objects.requireNonNull(clazz, "clazz").getName();
        final int lastDotIdx = className.lastIndexOf('.');
        if (lastDotIdx > -1) {
            return className.substring(lastDotIdx + 1);
        }
        return className;
    }

}
