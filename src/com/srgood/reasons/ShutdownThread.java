package com.srgood.reasons;

import com.srgood.reasons.config.ConfigPersistenceUtils;

import javax.xml.transform.TransformerException;

public class ShutdownThread extends Thread {

    public void start() {

        try {
            ConfigPersistenceUtils.writeXML();
        } catch (TransformerException e) {
            
            e.printStackTrace();
        }

        ReasonsMain.jda.shutdown();

        System.out.println("Successfully shutdown from termination");
    }
}
