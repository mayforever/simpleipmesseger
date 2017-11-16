/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jb.ipmessenger.processor;

import com.jb.ipmessenger.session.ConversationSession;
import com.jb.ipmessenger.udp.UdpData;
import com.mayforever.queue.Queue;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author MIS
 */
public class ProcessorManager {
    
    private Queue<UdpData> data = null;
    private Map<String, ConversationSession> conversations;
    private Logger log = null;

    public Queue<UdpData> getData() {
        return data;
    }
    private Map<Integer,Processor> processors = null;
    private int noOfProcessor; 
    
    public ProcessorManager(int processorCount){
        log = Logger.getLogger("PROC_MAN");
        data = new Queue<UdpData>();
        processors = new HashMap<Integer,Processor>();
        noOfProcessor = processorCount;
        
        for(int i = 0; i < noOfProcessor; i++){
            processors.put(i, new Processor(this));
        }
        
        conversations = new HashMap<String, ConversationSession>();
    }

    public Map<String, ConversationSession> getConversations() {
        return conversations;
    }
    
    public void startAllProcessor(){
        for (Map.Entry<Integer, Processor> processor : processors.entrySet()) {
            log.info("the processor no. " + processor.getKey() + " has been launch");
            processor.getValue().startProcessor();
        }
    }
}
