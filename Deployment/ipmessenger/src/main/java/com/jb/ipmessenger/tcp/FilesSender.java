/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jb.ipmessenger.tcp;

import com.mayforever.network.newtcp.ClientListener;
import com.mayforever.network.newtcp.TCPClient;
import com.mayforever.tools.BitConverter;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author MIS
 */
public class FilesSender implements ClientListener{
    
    private Logger log = null;
    
    private com.mayforever.network.newtcp.TCPClient tcpClient;
    
    // private int fileCountIndex = 0;
    // private int sizePerSend = 100000;
    public FilesSender(AsynchronousSocketChannel asc){
       log = Logger.getLogger("FILE_SENDER");
       tcpClient = new TCPClient(asc);
       tcpClient.addListener(this);
    }
    
    @Override
    public void packetData(byte[] data) {
        
    }

    public boolean sendFilePath(String files){
        log.info("Initializing File Sender");
        File file = new File(files);
        log.info("checking if file is directory");
        if (file.isDirectory()){
            
            log.info("zip the directory");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream("dirCompressed.zip");
            } catch (FileNotFoundException ex) {
                log.error(ex.toString());
            }
            
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            
            try {
                zipFile(file, file.getName(), zipOut);
                zipOut.close();
                fos.close();
            } catch (IOException ex) {
                log.error(ex.toString());
            }
            log.info("file has been zip");
            File fileCompressed = new File("dirCompressed.zip");
            
            log.info("sending zip file ");
            this.sendFile(fileCompressed);
            
            do{
                try {
                    java.lang.Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.toString();
                }
        }while(!fileCompressed.delete());
        log.info("The compressed file has been deleted");
        }else{
            log.info("sending file ");
            this.sendFile(file);
        }
        
        return false;
    }
    
    @Override
    public void socketError(Exception excptn) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
    }

    
    public void sendFile(File file){
        FileInputStream fis = null;
        BufferedInputStream bis = null;
  
        try{
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            
            int fileLength = (int)file.length();
            log.info("total size sent :" + fileLength);
            byte[] contents = new byte[fileLength + 4];
            byte[] fileSize = BitConverter.intToBytes(fileLength, ByteOrder.BIG_ENDIAN);
            System.arraycopy(fileSize, 0, contents, 0, 4);
            
            bis.read(contents, 4, fileLength);
            this.tcpClient.sendPacket(contents);
            fis.close();
        }catch(OutOfMemoryError e){
            log.error(e);
            log.error("Memory Leak try adjust memory allocation");
        }catch (FileNotFoundException ex) {
            log.error(ex.toString());
        } catch (IOException ex) {
            log.error(ex.toString());
        }
 
    }
    
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
}
