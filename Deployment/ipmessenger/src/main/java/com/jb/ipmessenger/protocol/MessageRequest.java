/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jb.ipmessenger.protocol;

import com.mayforever.tools.BitConverter;
import java.nio.ByteOrder;


/**
 *
 * @author MIS
 */
public class MessageRequest extends BaseClass{

    public short getSize() {
        return size;
    }

    public void setSize(short size) {
        this.size = size;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public byte getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(byte groupSize) {
        this.groupSize = groupSize;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public byte getSenderSize() {
        return senderSize;
    }

    public void setSenderSize(byte senderSize) {
        this.senderSize = senderSize;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    
    private byte groupSize;
    private String group = "";
    private byte senderSize;
    private String sender= "";
    private short size;
    private String messages= "";
    private byte priority;

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }
    
    @Override
    public byte[] toBytes() {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        byte[] data = new byte[this.group.length() + this.sender.length() + this.messages.length() + 6];
        int index = 0;
        data[index] = this.getIdentifier();
        index++;
        data[index] = (byte)this.group.length();
        index++;
        System.arraycopy(this.group.getBytes(), 0, data, index, this.group.length() );
        index+=this.group.length();
        data[index] = (byte)this.sender.length();
        index++;
        System.arraycopy(this.sender.getBytes(), 0, data, index, this.sender.length() );
        index+=this.sender.length();
        System.arraycopy(BitConverter.shortToBytes((short)this.messages.getBytes().length, ByteOrder.BIG_ENDIAN), 0, data, index, 2);
        index+=2;
        System.arraycopy(this.messages.getBytes(), 0, data, index, this.messages.getBytes().length);
        index+=this.messages.getBytes().length;
        data[index] =  this.getPriority();
        index++;
        return data;
    }

    @Override
    public void fromBytes(byte[] data) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int index = 0;
        this.setIdentifier((byte)data[index]);
        index++;
        this.setGroupSize(data[index]);
        index++;
        this.setGroup(new java.lang.String(data,index,this.groupSize));
        index+=this.groupSize;
        this.setSenderSize(data[index]);
        index++;
        this.setSender(new java.lang.String(data,index,this.senderSize));
        index+=this.senderSize;
        this.setSize(BitConverter.bytesToShort(data, index, ByteOrder.BIG_ENDIAN));
        index+=2;
        this.setMessages(new java.lang.String(data, index, this.getSize()).trim());
        index+=this.getSize();
        this.setPriority(data[index]);
        index++;
    }
}
