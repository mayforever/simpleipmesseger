/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jb.ipmessenger.protocol;

/**
 *
 * @author MIS
 */
public class HostResponse extends BaseClass{

    public byte getHostSize() {
        return hostSize;
    }

    public void setHostSize(byte hostSize) {
        this.hostSize = hostSize;
    }

  
    private byte hostSize;
    
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
    
    private String host;
    
    @Override
    public byte[] toBytes() {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        byte[] data = new byte[hostSize];
        int index = 0;
        data[index] = this.getIdentifier();
        index++;
        data[index] = this.getHostSize();
        index++;
        System.arraycopy(this.getHost().getBytes(), 0, data, index, this.getHostSize() -2);
        index+=this.getHostSize() - 2;
        return data;
    }

    @Override
    public void fromBytes(byte[] data) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int index = 0;
        this.setIdentifier(data[index]);
        index++;
        this.setHostSize(data[index]);
        index++;
        this.setHost(new java.lang.String(data,index,this.getHostSize()-2));
        index+=this.getHostSize();
    }
    
}
