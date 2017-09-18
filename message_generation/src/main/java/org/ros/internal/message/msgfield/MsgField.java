package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 */
public interface MsgField {

    void setValue(Object object, ByteBuf buffer);

}
