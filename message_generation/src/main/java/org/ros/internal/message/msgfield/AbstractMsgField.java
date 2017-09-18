package org.ros.internal.message.msgfield;

import java.lang.reflect.Method;

/**
 * @author pavel.cernocky@artin.cz
 */
public abstract class AbstractMsgField implements MsgField {

    protected final Method setter;

    public AbstractMsgField(Class<?> msgClass, String setterName, Class<?> fieldClass) {
        try {
            setter = msgClass.getMethod(setterName, fieldClass);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
