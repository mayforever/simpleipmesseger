/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jb.ipmessenger.processor;

import com.jb.ipmessenger.Launcher;
import com.jb.ipmessenger.protocol.FileRequest;
import com.jb.ipmessenger.protocol.HostRequest;
import com.jb.ipmessenger.protocol.HostResponse;
import com.jb.ipmessenger.protocol.MessageRequest;
import com.jb.ipmessenger.session.ConversationSession;
import com.jb.ipmessenger.session.FileSession;
import com.jb.ipmessenger.udp.UdpData;
import com.mayforever.network.udp.UDPClientSWR;
import com.mayforever.thread.state.ServiceState;
import java.awt.TrayIcon;
import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 *
 * @author MIS
 */
public class Processor extends com.mayforever.thread.BaseThread{
    
    private Logger log = null;
    
    private ProcessorManager processorManager = null;
    Processor(ProcessorManager processorManager){
        log = Logger.getLogger("PROCESSOR");
        this.processorManager = processorManager;
    }
    
    
    public void startProcessor(){
        this.startThread();
    }
    
    @Override
    public void run() {
        while(this.getServiceState() == ServiceState.RUNNING){
            try {
                UdpData udpdata = processorManager.getData().get();
                if (udpdata != null){
                    
                    byte[] data = udpdata.getData();
                    log.info("udp server recieved data : " + Arrays.toString(data));
                    
                    switch (data[0]) {
                        case 0:
                            {
                                log.info("host request reciever");
                                HostRequest hostRequest = new HostRequest();
                                hostRequest.fromBytes(data);
                                String hostName = "";
                                try {
                                    hostName = InetAddress.getLocalHost().getHostName();
                                } catch (UnknownHostException ex) {
                                    log.error(ex.toString());
                                }       
                                HostResponse hostResponse = new HostResponse();
                                hostResponse.setIdentifier((byte)1);
                                hostResponse.setHost(hostName);
                                hostResponse.setHostSize((byte)(hostName.length() + 2));
                                InetSocketAddress isa = new InetSocketAddress(udpdata.getInetSocketAddress().getAddress().getHostAddress(), 13501);
                                UDPClientSWR udpClient = new UDPClientSWR();
                                byte[] response = udpClient.sendPacket(hostResponse.toBytes(), isa, 100, 1);
                                if(response != null){
                                    log.info("the response has send with bytes of " + Arrays.toString(response));
                                }       break;
                            }
                        case 1:
                            {
                                log.info("host response recieve");
                                HostResponse hostResponse = new HostResponse();
                                hostResponse.fromBytes(data);
                                String[] hostName = hostResponse.getHost().split("-");
                                log.info("recording to table" + Arrays.toString(hostName));                                
                                String ip = udpdata.getInetSocketAddress().getAddress().getHostAddress();
                                Launcher.messenger.modelOnlineTable.addRow(
                                        new Object[]{false, hostName[1], hostName[0],
                                            ip});
                                Launcher.messenger.setOnlineCount();
                                break;
                            }
                        case 2:
                            {
                                log.info("message recieved");
                                MessageRequest messageRequest = new MessageRequest();
                                messageRequest.fromBytes(data);
                                
                                String ip = udpdata.getInetSocketAddress().getAddress().getHostAddress();
                                if(!this.processorManager.getConversations().containsKey(ip)){
                                    this.processorManager.getConversations().put(ip, 
                                        new ConversationSession(messageRequest.getSender(), messageRequest.getGroup(), ip));
                                }
                                
                                ConversationSession convSession = this.processorManager.getConversations().get(ip);
                                convSession.addConvoFromSender(messageRequest.getMessages());
                                
                                // convSession.getMessengerBox().updateChatBox(ip);
                                if (messageRequest.getPriority() == 0){
                                    convSession.getTrayIcon().displayMessage("Incomming Message ",
                                            messageRequest.getSender() + " : " +
                                                    messageRequest.getMessages(),
                                            TrayIcon.MessageType.INFO);
                                }else{
                                    // convSession.getMessengerBox().modelOnlineTable.setRowCount(0);
                                    convSession.getMessengerBox().setVisible(true);
                                    convSession.getMessengerBox().updateChatBox(ip);
                                }    
                            }
                            break;
                        case 3:
                            {
                                log.info("file request recieved");
                                FileRequest fileRequest = new FileRequest();
                                fileRequest.fromBytes(data);
                                
                                String ip = udpdata.getInetSocketAddress().getAddress().getHostAddress();
                                if(!this.processorManager.getConversations().containsKey(ip)){
                                    this.processorManager.getConversations().put(ip, 
                                        new ConversationSession(fileRequest.getSender(), fileRequest.getGroup(), ip));
                                }
                                
                                FileSession fileSession = new FileSession(new File(fileRequest.getFileName()).getName(), ip);
                                log.info("the file : " + ip);
                                ConversationSession convSession = this.processorManager.getConversations().get(ip);
                                fileSession.addFileFromSender(convSession);
                                
                                convSession.getTrayIcon().displayMessage("Incomming Message ",
                                        fileRequest.getSender() + " : " +
                                                fileRequest.getFileName(),
                                        TrayIcon.MessageType.INFO);
                            }
                            break;
                        default:
                            log.warn("wrong data recieved");
                            break;
                    }
                }
            } catch (InterruptedException ex) {
                log.error(ex.toString());
            }
        }
    }
}
