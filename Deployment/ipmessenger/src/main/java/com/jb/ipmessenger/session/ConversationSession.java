/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jb.ipmessenger.session;

import com.jb.ipmessenger.Launcher;
import com.jb.ipmessenger.Messenger;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JButton;
import org.apache.log4j.Logger;

/**
 *
 * @author MIS
 */
public class ConversationSession {
    private String senderip = "";
    private String ip = "";
    private String convo = "";
    private String username = "";
    private String groupSender = "";
    private String sender = "";
    private String group = "";
    private TrayIcon trayIcon = null;

    public TrayIcon getTrayIcon() {
        return trayIcon;
    }

    private Messenger messengerBox;
    private Logger log = null;
    private void initTray(){
       log.info("creating tray for certain ip");
        if (SystemTray.isSupported()) {
             // get the SystemTray instance
             SystemTray tray = SystemTray.getSystemTray();
             // load an image
             Image image = Toolkit.getDefaultToolkit().getImage("png"+File.separator+"mes2.png")
                     .getScaledInstance(18, 18,Image.SCALE_DEFAULT);
             // create a action listener to listen for default action executed on the tray icon
             ActionListener listener = new ActionListener() {
                 public void actionPerformed(ActionEvent e) {
                 messengerBox.setVisible(true);
                 messengerBox.updateChatBox(senderip);
                 }
             };
             PopupMenu popup = new PopupMenu();
             
             trayIcon = new TrayIcon(image, "["+ groupSender + sender + "]", popup);
             trayIcon.addActionListener(listener);
             // ...
             // add the tray image
                try {
                    tray.add(trayIcon);
                } catch (AWTException ex) {
                    log.error(ex.toString());
                }
         } else {
             log.error("this is not support system tray");
         }
    }
    
    public Messenger getMessengerBox() {
        return messengerBox;
    }
    
    public ConversationSession(String sender,String senderGroup,String senderIp){
        log = Logger.getLogger("CONV_SESS");
        convo = "";
        this.senderip = senderIp;
        this.sender = sender;
        this.groupSender = senderGroup;
        String hostName = "";
        
        
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            // Logger.getLogger(ConversationSession.class.getName()).log(Level.SEVERE, null, ex);
            log.error(ex.toString());
        }
        String[] hostnameArray = hostName.split("-");
        this.username = hostnameArray[1];
        this.group = hostnameArray[0];
        try {
            this.ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            log.error(ex.toString());
        }
        messengerBox = new Messenger();
        
        initTray();
        
        messengerBox.modelOnlineTable.addRow(
            new Object[]{true, sender, groupSender, senderip} );
        messengerBox.hideForClient();
    }
    
    public void addConvoFromSender(String chat){
        log.info("["+groupSender+"-"+sender+"]@"+ senderip+ " >> " + chat);
        convo += "\n["+groupSender+"-"+sender+"]@"+ senderip+ " >> " + chat;
    }
    
    public void addConvoFromOwner(String chat){
        log.info("["+group+"-"+username+"]@"+ ip + " >> " + chat);
        convo += "\n["+group+"-"+username+"]@"+ ip + " >> " + chat;
    }
    
    public void addFileFromSender(JButton button){
        log.info("["+groupSender+"-"+sender+"]@"+ senderip+ " >> ");
        messengerBox.addFileButton(button);
    }
    
    public void addFileFromOwner(JButton button){
        log.info("["+group+"-"+username+"]@"+ ip + " >> ");
        messengerBox.addFileButton(button);
    }
    
    public String getConvo(){
        return this.convo;
    }
    
    public void disposeThis(){
        Launcher.tray.remove(this.trayIcon);
    }
}
