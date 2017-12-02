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
public class RecievedResponse extends BaseClass{
    private byte response;

    public byte getResponse() {
        return response;
    }

    public void setResponse(byte response) {
        this.response = response;
    }
    
    @Override
    public byte[] toBytes() {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        byte[] data = new byte[2];
        int index = 0;
        data[index] = this.getIdentifier();
        index++;
        data[index] = this.getResponse();
        index++;
        return null;
    }

    @Override
    public void fromBytes(byte[] data) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int index = 0;
        this.setIdentifier(data[index]);
        index++;
        this.setResponse(data[index]);
        index++;
    }
    
}
