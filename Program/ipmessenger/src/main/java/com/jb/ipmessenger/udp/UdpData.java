/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jb.ipmessenger.udp;

import java.net.InetSocketAddress;

/**
 *
 * @author MIS
 */
public class UdpData {
    private InetSocketAddress inetSocketAddress = null;
    private byte[] data = null;
    
    public UdpData(byte[] data, InetSocketAddress isa){
        this.inetSocketAddress = isa;
        this.data = data;
    }
    
    public byte[] getData(){
        return this.data;
    }
    
    public InetSocketAddress getInetSocketAddress(){
        return this.inetSocketAddress;
    }
}
