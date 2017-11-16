/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jb.ipmessenger.tcp;

import com.jb.ipmessenger.protocol.FileRequest;
import com.mayforever.network.newtcp.ClientListener;
import com.mayforever.network.newtcp.TCPClient;
import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MIS
 */
public class TcpClient implements ClientListener{
    
    private com.mayforever.network.newtcp.TCPClient tcpClient;
    
    public TcpClient(AsynchronousSocketChannel asc){
       tcpClient = new TCPClient(asc);
       tcpClient.addListener(this);
    }
    
    @Override
    public void packetData(byte[] data) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if(data[0] == 5){
            FileRequest fileRequest = new FileRequest();
            fileRequest.fromBytes(data);
            
            File file = new File(fileRequest.getFileName());
            try {
                byte[] bFile = Files.readAllBytes(file.toPath());
                tcpClient.sendPacket(bFile);
            } catch (IOException ex) {
                Logger.getLogger(TcpClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }



    @Override
    public void socketError(Exception excptn) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
    }

}
