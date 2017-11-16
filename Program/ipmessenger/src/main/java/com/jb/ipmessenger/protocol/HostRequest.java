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
public class HostRequest extends BaseClass{

    public byte getIpSize() {
        return ipSize;
    }

    public void setIpSize(byte ipSize) {
        this.ipSize = ipSize;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    private byte ipSize;
    private String ip;
    @Override
    public byte[] toBytes() {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        byte[] data = new byte[ipSize];
        int index = 0;
        data[index] = this.getIdentifier();
        index++;
        data[index] = this.getIpSize();
        index++;
        System.arraycopy(this.getIp().getBytes(), 0, data, index, this.getIpSize() -2);
        index+=this.getIpSize() - 2;
        return data;
    }

    @Override
    public void fromBytes(byte[] data) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int index = 0;
        this.setIdentifier(data[index]);
        index++;
        this.setIpSize(data[index]);
        index++;
        this.setIp(new java.lang.String(data,index,this.getIpSize()-2));
        index+=this.getIpSize();
    }
    
}
