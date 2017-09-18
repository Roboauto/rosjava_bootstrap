package org.ros.internal.message.msgfield;

import java.lang.reflect.Method;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */
public abstract class AbstractMsgField implements MsgField {

    protected final Method getter;
    protected final Method setter;

    public AbstractMsgField(Class<?> msgClass, String getterName, String setterName, Class<?> fieldClass) {
        try {
            getter = msgClass.getMethod(getterName);
            setter = msgClass.getMethod(setterName, fieldClass);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
