/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jb.ipmessenger;

import java.io.File;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author MIS
 */
public class App {
    public static void main(String args[]){
        ApplicationContext contextLauncher = new FileSystemXmlApplicationContext("conf"+File.separator+"messenger.conf.xml");
        contextLauncher.getBean("messenger");
    }
}
