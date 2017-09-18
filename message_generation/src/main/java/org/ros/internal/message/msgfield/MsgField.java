package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */
public interface MsgField {

    void setBufferValueToObject(Object object, ByteBuf buffer);

    void writeObjectValueToBuffer(Object object, ByteBuf buffer);

}
