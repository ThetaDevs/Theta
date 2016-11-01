package com.srgood.reasons.threading;

import com.srgood.reasons.BotMain;
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

        BotMain.jda.shutdown();

        System.out.println("Successfully shutdown from termination");
    }
}
