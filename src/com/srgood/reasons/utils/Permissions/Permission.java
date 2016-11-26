package com.srgood.reasons.utils.Permissions;

import com.srgood.reasons.commands.Command;
import com.srgood.reasons.utils.CommandUtils;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by srgood on 11/22/2016.
 */


public class Permission {

    public Permission() {
        mapCommands();
    }

    /**
     * @deprecated A constant default permission object for each enum should be stored 'somewhere' and should have a getter method or somthing.
     */
    public Permission(Permission permissions) {
        mapCommands();
        //TODO add Enum mapping
    }


    public Permission(Guild guild,String name) {
        //TODO XML
    }


    private boolean isDefined = false;

    Map<Command,Boolean> pCommands = new HashMap<>();

    private void mapCommands() {
        for (Command command: CommandUtils.getCommandsMap().values()) {
            pCommands.put(command,false);
        }
    }


    public boolean isDefined() {
        return isDefined;
    }

    public void setCommand(Command command,boolean toSet) {
        pCommands.replace(command,toSet);
    }

}
