/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jb.ipmessenger;

import com.jb.ipmessenger.protocol.FileRequest;
import com.jb.ipmessenger.protocol.HostRequest;
import com.jb.ipmessenger.protocol.HostResponse;
import com.jb.ipmessenger.protocol.MessageRequest;
import com.jb.ipmessenger.session.ConversationSession;
import com.jb.ipmessenger.tcp.TcpServer;
import com.mayforever.network.udp.UDPClientSWR;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 *
 * @author MIS
 */
public class Messenger extends javax.swing.JFrame 
        implements ActionListener{
    // private final String[] COL_META = {" ","Username","Group","IP"};
    private final int[] COL_SIZE = {10,20,20,20};
    public DefaultTableModel modelOnlineTable ;
    private ArrayList<String> pathList;
    private Logger log = null;
    /**
     * Creates new form Messenger
     */
    public Messenger() {
        log = Logger.getLogger("MESSENGER");
        log.info("load gui to nimbus design");
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Messenger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Messenger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Messenger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Messenger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        initComponents();
        
        
        // jTextPane1.insertIcon ( new ImageIcon ( "C:\\Users\\MIS\\Desktop\\63.png" ) );
        
        jtMessageBox.setDropTarget(
            new DropTarget(){
                @Override
                public synchronized void drop(DropTargetDropEvent evt) {
                    
                    try {
                        pathList = new ArrayList<String>();
                        jtMessageBox.setText("");
                        evt.acceptDrop(DnDConstants.ACTION_COPY);
                        List<File> droppedFiles = (List<File>)
                        evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        
                        int sizeOfListFile = droppedFiles.size();
                        String[] filePath = new String[sizeOfListFile];
                        
                        for (int i = 0;i<sizeOfListFile;i++) {
                            // System.out.println("drop :" + file.getPath());
                            filePath[i]= droppedFiles.get(i).getPath();
                            
                            String path = filePath[i];
                            JButton attach = new JButton(path);
                            pathList.add(path);
                            attach.addActionListener(
                                new ActionListener(){
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    
                                }
                            });
                            attach.setVisible(true);
                            jtMessageBox.insertComponent(attach);
                            
                            // jTextPane1.insertComponent(new ImageIcon ( "\"" +file.toString() +"\"" );
                        }
                    } catch (UnsupportedFlavorException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        this.setLocation(width-440, height-440);
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        pathList = new ArrayList<String>();
        modelOnlineTable = (DefaultTableModel)jtOnline.getModel();
        modelOnlineTable.setRowCount(0);
        jbRefresh.addActionListener(this);
        jbSend.addActionListener(this);
        jbClose.addActionListener(this);
        jbClose.setVisible(false);
        String[] sample = new String[1];
        sample[0] = "192.168.0.";
        fillAvailableTable(sample);
    }

     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtOnline = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jlOnlineCount = new javax.swing.JLabel();
        jbRefresh = new javax.swing.JButton();
        jbSend = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtMessageBox = new javax.swing.JTextPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtChatBox = new javax.swing.JTextPane();
        jcbPriority = new javax.swing.JComboBox<>();
        jbClose = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jtFileBox = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jtOnline.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " ", "Username", "Group", "IP"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jtOnline);
        if (jtOnline.getColumnModel().getColumnCount() > 0) {
            jtOnline.getColumnModel().getColumn(0).setPreferredWidth(10);
        }

        jLabel1.setText("Total Online");

        jlOnlineCount.setText("jLabel2");

        jbRefresh.setText("Refresh");

        jbSend.setText("Send");

        jtMessageBox.setDragEnabled(true);
        jScrollPane3.setViewportView(jtMessageBox);

        jtChatBox.setEditable(false);
        jScrollPane4.setViewportView(jtChatBox);

        jcbPriority.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Normal", "Auto Prompt" }));
        jcbPriority.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbPriorityActionPerformed(evt);
            }
        });

        jbClose.setText("Close Conversation");

        jtFileBox.setEditable(false);
        jScrollPane6.setViewportView(jtFileBox);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbClose)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                        .addComponent(jcbPriority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jbSend))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbRefresh)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jlOnlineCount))))))
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlOnlineCount)
                        .addGap(13, 13, 13)
                        .addComponent(jbRefresh))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbSend)
                    .addComponent(jcbPriority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbClose))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jcbPriorityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbPriorityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbPriorityActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Messenger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Messenger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Messenger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Messenger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Messenger().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbRefresh;
    private javax.swing.JButton jbSend;
    private javax.swing.JComboBox<String> jcbPriority;
    private javax.swing.JLabel jlOnlineCount;
    private javax.swing.JTextPane jtChatBox;
    private javax.swing.JTextPane jtFileBox;
    private javax.swing.JTextPane jtMessageBox;
    private javax.swing.JTable jtOnline;
    // End of variables declaration//GEN-END:variables
    public void fillAvailableTable(String[] ipPrefix){
//        for(int i = 0; i < this.modelOnlineTable.getRowCount();i++){
//            this.modelOnlineTable.removeRow(i);
//        }
        this.modelOnlineTable.setRowCount(0);
        
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                
                for(String ip:ipPrefix){
                    for(int i = 1; i < 255;i++){
                         
                        HostRequest hostRequest = new HostRequest();
                        hostRequest.setIdentifier((byte)0);
                        hostRequest.setIp(ip + i);
                        hostRequest.setIpSize((byte)(hostRequest.getIp().length() + 2));
                        InetSocketAddress isa = new InetSocketAddress(hostRequest.getIp(), 13501);

                        UDPClientSWR udpClient = new UDPClientSWR();
                        byte[] response = udpClient.sendPacket(hostRequest.toBytes(), isa, 10, 1);
                      

                        if(response != null){
                            HostResponse hostResponse = new HostResponse();
                            hostResponse.fromBytes(response);
                            log.debug("Host Request Response : " + hostResponse.getHost());
                        }
                    }
                }
            }
            
        });
        thread.start();
    }
    
    public void updateChatBox(String ip){
        jtChatBox.setText( Launcher.procManager.getConversations().get(ip).getConvo());
        jtChatBox.moveCaretPosition(jtChatBox.getText().length());
    }
    
    public void addFileButton(JButton jbutton){
        jtFileBox.insertComponent(jbutton);
        jtChatBox.moveCaretPosition(jtChatBox.getText().length());
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(jbRefresh)){
            String[] sample = new String[1];
            sample[0] = "192.168.0.";
            fillAvailableTable(sample);
        }else if(e.getSource().equals(jbSend)){
            
            for(int i = 0; i < this.modelOnlineTable.getRowCount();i++){
                if((boolean)this.jtOnline.getValueAt(i, 0)){
                    String ip = (String) this.jtOnline.getValueAt(i , 3);
                    String group = (String) this.jtOnline.getValueAt(i , 2);
                    String reciever = (String) this.jtOnline.getValueAt(i , 1);
                    if(!Launcher.procManager.getConversations().containsKey(ip)){
                        Launcher.procManager.getConversations().put(ip, new ConversationSession(reciever, group, ip));
                    }
                    Launcher.procManager.getConversations().get(ip).addConvoFromOwner(jtMessageBox.getText());

                    String hostName = "";
                    String myIp = "";
                    try {
                        hostName = InetAddress.getLocalHost().getHostName();
                        
                        myIp = InetAddress.getLocalHost().getHostAddress();
                    } catch (UnknownHostException ex) {
                        log.error(ex.toString());
                    }
                    String[] hostnameArray = hostName.split("-");
                    log.debug("HostName Array : "+Arrays.toString(hostnameArray));
                    String ownerGroup = hostnameArray[0];
                    String owner = hostnameArray[1];
                    

                    InetSocketAddress isa = new InetSocketAddress(ip, 13501);
                    UDPClientSWR udpClient = new UDPClientSWR();
                    
                    // System.out.println(Arrays.toString(response));
                    
                    // updateChatBox(ip);
                    
                    this.setVisible(false);
                    
                    if(pathList.size()!=0){

                        String path = pathList.get(i);
                        FileRequest fileRequest = new FileRequest();

                        fileRequest.setIdentifier((byte)3);
                        fileRequest.setSender(owner);
                        fileRequest.setGroup(ownerGroup);
                        
                        fileRequest.setFileName(path);
                        fileRequest.setFileSize((int)new File(path).length());
                        
                        UDPClientSWR udpClient1 = new UDPClientSWR();
                        byte[] response = udpClient1.sendPacket(fileRequest.toBytes(), isa, 100, 5);
                        
                        Launcher.tcpServer = new TcpServer(path,myIp);
                        if(response != null){
                            HostResponse hostResponse = new HostResponse();
                            hostResponse.fromBytes(response);
                            log.debug("Host Request Response : " + hostResponse.getHost());
                        }
                    }else{
                        MessageRequest messageRequest = new MessageRequest();
                        messageRequest.setIdentifier((byte)2);
                        messageRequest.setGroup(ownerGroup);
                        messageRequest.setSender(owner);
                        messageRequest.setMessages(jtMessageBox.getText());
                        messageRequest.setPriority((byte)jcbPriority.getSelectedIndex());
                        byte[] response = udpClient.sendPacket(messageRequest.toBytes(), isa, 100, 5);
                        if(response != null){
                            HostResponse hostResponse = new HostResponse();
                            hostResponse.fromBytes(response);
                            log.debug("message response : " + hostResponse.getHost());
                        }
                    }
                    jtMessageBox.setText("");
                } 
            }
        }else if(e.getSource().equals(jbClose)){
            ConversationSession conv = Launcher.procManager.getConversations().remove((String) this.jtOnline.getValueAt(0 , 3));
            conv.disposeThis();
            this.setVisible(false);
            this.dispose();
        }
    }
    public void setOnlineCount(){
        jlOnlineCount.setText(this.modelOnlineTable.getRowCount() +"");
    }
    
    public void hideForClient(){
        jLabel1.setVisible(false);
        jlOnlineCount.setVisible(false);
        jbRefresh.setVisible(false);
        jbClose.setVisible(true);
    }
    
    public void addAttachment(FileRequest fileRequest){
        JButton file = new JButton(fileRequest.getFileName());
        file.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }
    
    
    public void clearFileBox(){
        jtFileBox.setText("");
    }
}
