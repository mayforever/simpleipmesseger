/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jb.ipmessenger.tcp;

import com.mayforever.network.newtcp.ServerListener;
import java.nio.channels.AsynchronousSocketChannel;
/**
 *
 * @author MIS
 */
public class TcpServer implements ServerListener{
    
    private com.mayforever.network.newtcp.TCPServer tcpServer = null;
    private String path = null;
    
    public TcpServer(String path,String ip){
        tcpServer = new com.mayforever.network.newtcp.TCPServer(13502, ip);
        tcpServer.addListener(this);
        this.path = path;
    }
    
    @Override
    public void acceptSocket(AsynchronousSocketChannel asc) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        // new TcpClient(asc);
        FilesSender sess = new FilesSender(asc);
        System.out.println("this is the path " + path);
        sess.sendFilePath(path);
        
        this.tcpServer.stopListenning();
    }

    @Override
    public void socketError(Exception excptn) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
    }
}
