/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jb.ipmessenger;

import com.jb.ipmessenger.processor.ProcessorManager;
import com.jb.ipmessenger.tcp.TcpServer;
import com.jb.ipmessenger.udp.MessengerUdpServer;
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
import org.apache.log4j.PropertyConfigurator;
/**
 *
 * @author MIS
 */
public class Launcher {
    
    public static ProcessorManager procManager;
    public static MessengerUdpServer messengerUdpServer;
    public static Messenger messenger;
    public static TrayIcon trayIcon = null;
    public static PopupMenu popup;
    public static SystemTray tray;
    public static TcpServer tcpServer = null;
    public static org.apache.log4j.Logger log = null;
    public static String ip;
    
    // private Logger logger = null;
    public Launcher(String args[]) {
        PropertyConfigurator.configure("conf"+File.separator+"log4j.properties");
        log = org.apache.log4j.Logger.getLogger("LAUNCHER");
        try {
            
            ip = InetAddress.getLocalHost().getHostAddress();
            log.debug("the ip is " + ip );
        } catch (UnknownHostException ex) {
            log.error(ex.toString());
            System.exit(0);
        }
        
        log.info("launching processor");
        procManager = new ProcessorManager(2);
        procManager.startAllProcessor();
        
        log.info("launching udp server listen to ip :"+  ip );
        messengerUdpServer = new MessengerUdpServer();
        messengerUdpServer.startMessenger();
        
        log.info("creating main messenger ");
        messenger = new Messenger();
        
        log.info("getting system tray");
        if (SystemTray.isSupported()) {
             tray = SystemTray.getSystemTray();
             // load an image
             Image image = Toolkit.getDefaultToolkit().getImage("png"+File.separator+"mes3.png")
                     .getScaledInstance(18, 18,Image.SCALE_DEFAULT);
             ActionListener listener = new ActionListener() {
                 public void actionPerformed(ActionEvent e) {
                    messenger.modelOnlineTable.setRowCount(0);
                    String[] ips = args;
                    // sample[0] = "192.168.0.";
                    messenger.fillAvailableTable(ips);
                    messenger.setVisible(true);
                 }
             };
             popup = new PopupMenu();
             trayIcon = new TrayIcon(image, "IP Messenger", popup);
             trayIcon.addActionListener(listener);
            try {
                tray.add(trayIcon);                    
            } catch (AWTException ex) {
                log.error("tray Icon not Supported");
                System.exit(0);
            }
        } else {
            log.error("the os not support tray icon");
            System.exit(0);
        }
    }
}
