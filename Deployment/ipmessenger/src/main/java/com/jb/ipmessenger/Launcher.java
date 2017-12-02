/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jb.ipmessenger;

import static com.jb.ipmessenger.ListNet.displayInterfaceInformation;
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
import static java.lang.System.out;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
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
    public static String[] ipPrefix = null;
    
    // private Logger logger = null;
    public Launcher(String args[]) throws SocketException {
        PropertyConfigurator.configure("conf"+File.separator+"log4j.properties");
        log = org.apache.log4j.Logger.getLogger("LAUNCHER");
        ipPrefix = args;
        log.debug("the prefix[0] is "+args[0]);
//        try {
//            
//            ip = InetAddress.getLocalHost().getHostName();
//            log.debug("the ip is " + ip );
//        } catch (UnknownHostException ex) {
//            log.error(ex.toString());
//            System.exit(0);
//        }
        
        try{
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)){
                // displayInterfaceInformation(netint);
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    try{
                        log.debug("the prefix is"+inetAddress.toString().substring(0, args[0].length()+1));
                        if(inetAddress.toString().substring(0, args[0].length()+1).equals("/"+args[0]) ){
                            ip = inetAddress.toString().substring(1);
                        }
                        log.debug("detect inet address :"+inetAddress.toString());
                    }catch(StringIndexOutOfBoundsException sioobe){
                        // if the string index would not sufficient to ipPrefix
                    }
                    
                }
            }
                
        }catch(SocketException e){
            log.error(e);
        }
        log.debug("the ip was : "+ip);
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
