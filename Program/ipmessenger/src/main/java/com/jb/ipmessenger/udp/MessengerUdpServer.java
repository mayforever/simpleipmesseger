/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jb.ipmessenger.udp;

import com.jb.ipmessenger.Launcher;
import com.mayforever.network.udp.UDPServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MIS
 */
public class MessengerUdpServer 
        extends com.mayforever.thread.BaseThread
        implements com.mayforever.network.udp.ServerListener{
    private com.mayforever.network.udp.UDPServer udpServer = null;
    private com.mayforever.queue.Queue<UdpData> udpData = null;
    
    public MessengerUdpServer (){
        udpServer = new UDPServer(13501);
        udpServer.addListener(this);
        this.udpData = new com.mayforever.queue.Queue<UdpData>();
    }
    
    public void startMessenger(){
        this.startThread();
    }
    
    @Override
    public void recievePacket(byte[] bytes, InetSocketAddress isa) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        udpData.add(new UdpData(bytes, isa));
        try {
            this.udpServer.sendPacket(bytes, isa);
        } catch (IOException ex) {
            Logger.getLogger(MessengerUdpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void errorDatagram(Exception excptn) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
    }

    @Override
    public void run() {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        while(this.getServiceState()==com.mayforever.thread.state.ServiceState.RUNNING){
            try {
                UdpData data = udpData.get();
                if (data != null){
                    Launcher.procManager.getData().add(data);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MessengerUdpServer.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    
}
