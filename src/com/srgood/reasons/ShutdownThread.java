package com.srgood.reasons;

import com.srgood.reasons.utils.config.ConfigPersistenceUtils;

import javax.xml.transform.TransformerException;

public class ShutdownThread extends Thread {

    public void start() {

        try {
            ConfigPersistenceUtils.writeXML();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ReasonsMain.jda.shutdown();

        System.out.println("Successfully shutdown from termination");
    }
}
