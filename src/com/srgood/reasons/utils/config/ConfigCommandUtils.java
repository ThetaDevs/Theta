package com.srgood.reasons.utils.config;

import com.srgood.reasons.commands.Command;
import com.srgood.reasons.commands.PermissionLevels;
import com.srgood.reasons.utils.CommandUtils;
import com.srgood.reasons.utils.Permissions.PermissionUtils;
import net.dv8tion.jda.core.entities.Guild;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.srgood.reasons.utils.config.ConfigBasicUtils.*;

class ConfigCommandUtils {
    private static final Map<String, Function<String, Object>> requiredCommandSubElements = new HashMap<String, Function<String, Object>>() {
        private static final long serialVersionUID = -710068261487017415L;

        {
            put("permLevel", name -> CommandUtils.getCommandByName(name).defaultPermissionLevel().getLevel());
            put("isEnabled", name -> true);
        }
    };

    private static Element getCommandElement(Guild guild, String commandName) {
        return getCommandElement(getCommandsElement(guild), commandName);
    }

    private static Element getCommandElement(Guild guild, Command command) {
        return getCommandElement(guild, CommandUtils.getNameFromCommand(command));
    }

    private static Element getCommandElement(Element commandsElement, String commandName) {
        try {
            lockDocument();
            List<Node> commandList = ConfigBasicUtils.nodeListToList(commandsElement.getElementsByTagName("command"));

            for (Node n : commandList) {
                Element elem = (Element) n;
                if (elem.getAttribute("name").equals(commandName)) {
                    return elem;
                }
            }
        } finally {
            releaseDocument();
        }
        return null;
    }

    private static String getCommandProperty(Element commandElement, String property) {
        try {
            lockDocument();
            Element propertyElement = ConfigBasicUtils.getFirstSubElement(commandElement, property);
            return propertyElement != null ? propertyElement.getTextContent() : null;
        } finally {
            releaseDocument();
        }
    }

    private static String getCommandProperty(Guild guild, Command command, String property) {
        return getCommandProperty(getCommandElement(guild, command), property);
    }

    private static String getCommandProperty(Guild guild, String commandName, String property) {
        return getCommandProperty(guild, CommandUtils.getCommandByName(commandName), property);
    }

    private static void setCommandProperty(Element commandElement, String property, String value) {
        Element firstMatchElement = ConfigBasicUtils.getFirstSubElement(commandElement, property);
        if (firstMatchElement == null) {
            try {
                Element newPropElem = lockAndGetDocument().createElement(property);
                newPropElem.setTextContent(value);
                commandElement.appendChild(newPropElem);
            } finally {
                ConfigBasicUtils.releaseDocument();
            }
        } else {
            firstMatchElement.setTextContent(value);
        }
    }

    private static void setCommandProperty(Guild guild, Command command, String property, String value) {
        setCommandProperty(getCommandElement(guild, command), property, value);
    }

    static void setCommandProperty(Guild guild, String commandName, String property, String value) {
        setCommandProperty(guild, CommandUtils.getCommandByName(commandName), property, value);
    }

    private static Element getCommandsElement(Guild guild) {
        return ConfigBasicUtils.getFirstSubElement(ConfigGuildUtils.getGuildNode(guild), "commands");
    }

    private static void initGuildCommands(Guild guild) {
        try {
            Element commandsElement = lockAndGetDocument().createElement("commands");
            ConfigGuildUtils.getGuildNode(guild).appendChild(commandsElement);
            initCommandsElement(commandsElement);
        } finally {
            ConfigBasicUtils.releaseDocument();
        }
    }

    private static void initCommandsElement(Element commandsElement) {
        try {
            for (String command : CommandUtils.getCommandsMap().keySet()) {
                initCommandElement(commandsElement, command);
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }

    private static void initCommandElement(Element commandsElement, String command) {
        command = CommandUtils.getPrimaryCommandAlias(command);

        if (commandElementExists(commandsElement, command)) {
            return;
        }

        try {
            Element commandElement = lockAndGetDocument().createElement("command");

            commandElement.setAttribute("name", command);

            commandsElement.appendChild(commandElement);

            addMissingSubElementsToCommand(commandsElement, command);
        } finally {
            ConfigBasicUtils.releaseDocument();
        }
    }

    public static boolean isCommandEnabled(Guild guild, Command command) {
        return Boolean.parseBoolean(getCommandProperty(guild, command, "isEnabled"));
    }

    public static void setCommandIsEnabled(Guild guild, Command command, boolean enabled) {
        setCommandProperty(guild, command, "isEnabled", "" + enabled);
    }

    private static boolean commandElementExists(Element commandsElement, String cmdName) {
        return getCommandElement(commandsElement, cmdName) != null;
    }

    private static void addMissingSubElementsToCommand(Element commandsElement, String commandName) {
        Element targetCommandElement = getCommandElement(commandsElement, commandName);

        for (Map.Entry<String, Function<String, Object>> entry : requiredCommandSubElements.entrySet()) {
            if (getCommandProperty(targetCommandElement, entry.getKey()) == null) {
                setCommandProperty(targetCommandElement, entry.getKey(), entry.getValue().apply(commandName).toString());
            }
        }
    }

    static void initCommandConfigIfNotExists(com.srgood.reasons.commands.CommandParser.CommandContainer cmd) {
        initCommandConfigIfNotExists(cmd.event.getGuild(), CommandUtils.getCommandByName(cmd.invoke));
    }

    static void initCommandConfigIfNotExists(Guild guild, Command cmd) {
        try {
            lockDocument();
            Element serverElement = ConfigGuildUtils.getGuildNode(guild);
            Element commandsElement;
            String realCommandName = CommandUtils.getNameFromCommand(cmd);
            {
                NodeList commandsNodeList = serverElement.getElementsByTagName("commands");
                if (commandsNodeList.getLength() == 0) {
                    initGuildCommands(guild);
                }
                commandsElement = getCommandsElement(guild);

                NodeList commandNodeList = commandsElement.getElementsByTagName("command");
                if (commandNodeList.getLength() == 0) {
                    initCommandsElement(commandsElement);
                }
            }
            if (commandElementExists(commandsElement, realCommandName)) {
                addMissingSubElementsToCommand(commandsElement, realCommandName);
                return;
            }
            initCommandElement(commandsElement, realCommandName);
        } finally {
            releaseDocument();
        }
    }

    public static PermissionLevels getCommandPermission(Guild guild, Command command) {
        return PermissionUtils.intToEnum(Integer.parseInt(getCommandProperty(guild, command, "permLevel")));
    }

    public static void setCommandPermission(Guild guild, Command command, PermissionLevels perm) {
        setCommandProperty(guild, command, "permLevel", "" + perm.getLevel());
    }
}
