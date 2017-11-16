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
public class FileRequest extends BaseClass{

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
    

    public byte getFileNameSize() {
        return fileNameSize;
    }

    public void setFileNameSize(byte fileNameSize) {
        this.fileNameSize = fileNameSize;
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
    private byte fileNameSize;
    private String fileName;
    private int fileSize;
    @Override
    public byte[] toBytes() {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        byte[] data= new byte[this.group.length() + this.sender.length() +this.getFileName().length() + 8];
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
        data[index] = (byte) this.getFileName().length();
        index++;
        System.arraycopy(this.getFileName().getBytes(), 0, data, index, this.getFileName().length());
        index+=this.getFileName().length();
        System.arraycopy(BitConverter.intToBytes(this.getFileSize(), ByteOrder.BIG_ENDIAN), 0, data, index, 4);
        index+=4;
        return data;
    }

    @Override
    public void fromBytes(byte[] data) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int index = 0;
        this.setIdentifier(data[index]);
        index++;
        this.setGroupSize(data[index]);
        index++;
        this.setGroup(new java.lang.String(data,index,this.groupSize));
        index+=this.groupSize;
        this.setSenderSize(data[index]);
        index++;
        this.setSender(new java.lang.String(data,index,this.senderSize));
        index+=this.senderSize;
        this.setFileNameSize(data[index]);
        index++;
        this.setFileName(new java.lang.String(data, index, this.getFileNameSize()));
        index+=this.getFileSize();
        this.setFileSize(BitConverter.bytesToInt(data, index, ByteOrder.BIG_ENDIAN));
        index+=4;
    }
    
}
