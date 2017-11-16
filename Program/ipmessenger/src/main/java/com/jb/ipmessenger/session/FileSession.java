/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jb.ipmessenger.session;

import com.jb.ipmessenger.tcp.FileRecieverCLient;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;

/**
 *
 * @author MIS
 */


public class FileSession implements ActionListener{
    
    private String fileName = "";
    private JButton jbDownload = null;
    private String ip = "";
    private ConversationSession convSession = null;
    // private int port;
    
    public FileSession(String fileName, String ip){
        
        this.fileName = fileName;
        // this.port = port;
        this.ip = ip;
        jbDownload = new JButton(this.fileName);
        jbDownload.addActionListener(this);
    }
    
    public void addFileFromSender(ConversationSession convSession){
        convSession.addFileFromSender(jbDownload);
        this.convSession = convSession;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser(); 
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Choose Folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(false);
        //    
        if (chooser.showOpenDialog(jbDownload) == JFileChooser.APPROVE_OPTION) { 
            try{
                System.out.println(this.fileName.substring(fileName.length() - 4, fileName.length() - 3));
                if(!this.fileName.substring(fileName.length() - 4, fileName.length() - 3).equals(".") ){
                    this.fileName += ".zip";
                }
            }catch(Exception ex){
                this.fileName += ".zip";
            }
            
            FileRecieverCLient fileClient = new FileRecieverCLient(this.ip, 
                    chooser.getSelectedFile().getPath(), this.fileName);
            
            System.out.println(chooser.getSelectedFile().getPath() + " output is " + this.fileName);
            this.convSession.getMessengerBox().clearFileBox();               
            jbDownload.setVisible(false);
        }else {
            System.out.println("No Selection ");
        }
        
    }
}
