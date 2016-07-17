package com.srgood.dbot.hooks;



import java.util.LinkedList;
import java.util.List;

import com.srgood.dbot.hooks.events.PlayerEvent;

public class PlayerEventManager
{
    private final List<PlayerEventListener> listeners = new LinkedList<>();

    public PlayerEventManager() {}

    public void register(PlayerEventListener listener)
    {
        if (listeners.contains(listener))
            throw new IllegalArgumentException("Attempted to register a listener that is already registered");
        listeners.add(listener);
    }

    public void unregister(PlayerEventListener listener)
    {
        listeners.remove(listener);
    }

    public void handle(PlayerEvent event)
    {
        List<PlayerEventListener> listenerCopy = new LinkedList<>(listeners);
        for (PlayerEventListener listener : listenerCopy)
        {
            try
            {
                listener.onEvent(event);
            }
            catch (Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
    }
}