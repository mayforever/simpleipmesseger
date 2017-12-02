
package com.jb.ipmessenger.tcp;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jb.ipmessenger.RecievedFile;
import com.mayforever.network.newtcp.ClientListener;
import com.mayforever.network.newtcp.TCPClient;
import com.mayforever.tools.BitConverter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JFrame;
import org.apache.log4j.Logger;

/**
 *
 * @author MIS
 */
public class FileRecieverCLient implements ClientListener{
    
    private com.mayforever.network.newtcp.TCPClient tcpClient;
    private int size = 0;
    private byte[] fileData = null;
    private int byteIndex = 0;
    private String outputFolder = null;
    private String outputName = null;
    private RecievedFile recievedFile = null;
    private Logger log = null;
    
//    public FileRecieverCLient(AsynchronousSocketChannel asc){
//       log = Logger.getLogger("FILE_RECV");
//       tcpClient = new TCPClient(asc);
//       tcpClient.addListener(this);
//       
//    }
    
    public FileRecieverCLient(String ip, String outputFolder, String outputName){
        log = Logger.getLogger("FILE_RECV");
        this.recievedFile = new RecievedFile();
        this.recievedFile.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.recievedFile.changeLoadingDetails("recieving files ...");
        this.recievedFile.setValue(0);
        this.recievedFile.setVisible(true);
        this.recievedFile.setLocation(550, 300);
    	this.tcpClient = new com.mayforever.network.newtcp.TCPClient(ip,13502);
        this.tcpClient.addListener(this);
        this.outputFolder = outputFolder;
        this.outputName = outputName;
        
     }
    
    public void packetData(byte[] data) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    	try{
            if (size == 0){
            
            size = BitConverter.bytesToInt(data , 0, ByteOrder.BIG_ENDIAN);
            fileData = new byte[size];
            System.arraycopy(data, 4, fileData, 0, data.length -4);
            byteIndex += data.length - 4;
            System.out.println(byteIndex + "/" + size);
            int percent = (int)(((double)((double)byteIndex /(double) size)) * 100);
            log.debug("this is the process percent : "+percent);
            this.recievedFile.setValue(percent);
    	}else{
            System.arraycopy(data, 0, fileData, byteIndex, data.length);
            byteIndex+=data.length;
            int percent = (int)(((double)((double)byteIndex /(double) size)) * 100);
            this.recievedFile.setValue(percent);
    	}
        if (byteIndex == size){
             
            FileOutputStream fos = null;
            try {
                System.out.println("creating file :" + outputFolder + File.separator + outputName); 
                fos = new FileOutputStream(outputFolder + File.separator + outputName);
            } catch (FileNotFoundException ex) {
                log.error(ex.toString());
            }
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            try {
                bos.write(fileData);
                bos.flush();
                fos.close();
            } catch (IOException ex) {
                log.error(ex.toString());
            }
            System.out.println(outputName.substring(outputName.length() - 3, outputName.length()));
            if (outputName.substring(outputName.length() - 3, outputName.length()).equals("zip")){
                System.out.println("Successfully Recieves");
                System.out.println(outputFolder + File.separator + outputName);
                File fileCompressed = new File(outputFolder + File.separator + outputName);
                this.unZipFile(fileCompressed,outputFolder);
                do{
                    try {
                        java.lang.Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while(!fileCompressed.delete());

                }
            }
        }catch (OutOfMemoryError e){
            log.error("memory leak. try to add memory allocation");
        }catch(Exception e){
            log.error(e);
            e.printStackTrace();
        }
    	
    }
    
    @Override
    public void socketError(Exception excptn) {
        
    }


    
    
    public void unZipFile(File file,String outputDir){
        this.recievedFile.changeLoadingDetails("unzipping files ...");
        this.recievedFile.setValue(0);
        this.recievedFile.setVisible(true);
    	byte[] buffer = new byte[1024];
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ZipEntry zipEntry;
        try {
            zipEntry = zis.getNextEntry();
            while(zipEntry != null){
                log.debug("the size of zip is " + zipEntry.getSize() + "---");
                String fileName = zipEntry.getName();
                File newFile = new File(outputDir + File.separator + fileName);
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
        zis.closeEntry();
        zis.close();
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
    }
}
