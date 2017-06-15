package com.srgood.reasons.impl.commands.impl.actual;

import com.google.common.io.Files;
import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.Reference;
import com.srgood.reasons.impl.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.commands.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.permissions.GuildPermissionSet;
import com.srgood.reasons.impl.permissions.Permission;
import com.srgood.reasons.impl.permissions.PermissionChecker;
import com.srgood.reasons.impl.permissions.PermissionStatus;
import com.srgood.reasons.impl.utils.GuildDataManager;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import org.apache.commons.codec.binary.Base32;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.srgood.reasons.impl.Reference.GLOBAL_RANDOM;

public class CommandImportDescriptor extends BaseCommandDescriptor {
    public CommandImportDescriptor() {
        super(Executor::new, "Runs a script of bot commands","<>", "import");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            Message.Attachment attachment = executionData.getMessage().getAttachments().get(0);
            List<String> commands = getCommands(attachment);
            if (commands == null) {
                sendOutput("Internal I/O error.");
                return;
            }

            for (String command : commands) {
                if (command.startsWith("PRFIX")) {
                    handlePrefixCommand(command);
                } else if (command.startsWith("PERMS")) {
                    handlePermsCommand(command);
                } else if (command.startsWith("ENBLE")) {
                    handleEnableCommand(command);
                } else if (command.startsWith("DSBLE")) {
                    handleDisableCommand(command);
                } else if (command.startsWith("BLIST")) {
                    handleBlacklistCommand(command);
                } else if (command.startsWith("CLIST")) {
                    handleCensorListCommand(command);
                } else if (command.startsWith("WLCME")) {
                    handleWelcomeCommand(command);
                } else if (command.startsWith("GDBYE")) {
                    handleGoodbyeCommand(command);
                }
            }
        }

        private void handlePrefixCommand(String command) {
            executionData.getBotManager().getConfigManager().getGuildConfigManager(executionData.getGuild()).setPrefix(command.split(" ")[1]);
        }

        private void handlePermsCommand(String command) {
            String[] parts = command.split(" ");
            Role role = executionData.getGuild().getRoleById(parts[1]);
            if (role == null) {
                return;
            }
            Permission permission;
            PermissionStatus permissionStatus;
            try {
                permission = Permission.valueOf(parts[2]);
                permissionStatus = PermissionStatus.valueOf(parts[3]);
            } catch (IllegalArgumentException e) {
                return;
            }
            GuildPermissionSet guildPermissionSet = GuildDataManager.getGuildPermissionSet(executionData.getBotManager().getConfigManager(), executionData.getGuild());
            guildPermissionSet.setPermissionStatus(role, permission, permissionStatus);
            GuildDataManager.setGuildPermissionSet(executionData.getBotManager().getConfigManager(), executionData.getGuild(), guildPermissionSet);
        }

        private void handleEnableCommand(String command) {
            String[] parts = command.split(" ");
            CommandDescriptor commandDescriptor = executionData.getBotManager().getCommandManager().getCommandByName(parts[1]);
            if (commandDescriptor != null) {
                executionData.getBotManager()
                             .getConfigManager()
                             .getGuildConfigManager(executionData.getGuild())
                             .getCommandConfigManager(commandDescriptor)
                             .setEnabled(true);
            }
        }

        private void handleDisableCommand(String command) {
            String[] parts = command.split(" ");
            CommandDescriptor commandDescriptor = executionData.getBotManager().getCommandManager().getCommandByName(parts[1]);
            if (commandDescriptor != null) {
                executionData.getBotManager()
                             .getConfigManager()
                             .getGuildConfigManager(executionData.getGuild())
                             .getCommandConfigManager(commandDescriptor)
                             .setEnabled(false);
            }
        }

        private void handleBlacklistCommand(String command) {
            String[] parts = command.split(" ");
            List<String> blacklist = GuildDataManager.getGuildBlacklist(executionData.getBotManager().getConfigManager(), executionData.getGuild());
            blacklist.add(parts[1]);
            GuildDataManager.setGuildBlacklist(executionData.getBotManager().getConfigManager(), executionData.getGuild(), blacklist);
        }

        private void handleCensorListCommand(String command) {
            String[] parts = command.split(" ");
            List<String> censorList = GuildDataManager.getGuildCensorList(executionData.getBotManager().getConfigManager(), executionData.getGuild());
            censorList.add(parts[1]);
            GuildDataManager.setGuildCensorList(executionData.getBotManager().getConfigManager(), executionData.getGuild(), censorList);
        }

        private List<String> getCommands(Message.Attachment attachment) {
            String encrypted;
            try {
                File tempFile = new File("" + GLOBAL_RANDOM.nextLong() + ".txt");
                tempFile.deleteOnExit();

                attachment.download(tempFile);
                encrypted = Files.readFirstLine(tempFile, Reference.FILE_CHARSET);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(encrypted));
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                String deserialized = (String) objectInputStream.readObject();
                byte[] base32 = Base64.getDecoder().decode(deserialized);
                String raw = new String(new Base32().decode(base32));

                String[] lines = raw.split("([\r\n]){1,2}");
                return Arrays.stream(lines)
                             .map(String::trim)
                             .filter(line -> !line.isEmpty())
                             .filter(line -> !line.startsWith("#"))
                             .collect(Collectors.toList());
            } catch (Exception e) {
                return null;
            }
        }

        private void handleWelcomeCommand(String command) {
            String[] parts = command.split(" ");
            String channelID = parts[1];
            String message = Arrays.asList(parts).subList(2, parts.length).stream().collect(Collectors.joining(" "));
            executionData.getBotManager().getConfigManager().getGuildConfigManager(executionData.getGuild()).setProperty("moderation/welcome", message);
            executionData.getBotManager().getConfigManager().getGuildConfigManager(executionData.getGuild()).setProperty("moderation/welcomechannel", channelID);
        }

        private void handleGoodbyeCommand(String command) {
            String[] parts = command.split(" ");
            String channelID = parts[1];
            String message = Arrays.asList(parts).subList(2, parts.length).stream().collect(Collectors.joining(" "));
            executionData.getBotManager().getConfigManager().getGuildConfigManager(executionData.getGuild()).setProperty("moderation/goodbye", message);
            executionData.getBotManager().getConfigManager().getGuildConfigManager(executionData.getGuild()).setProperty("moderation/goodbyechannel", channelID);
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkMemberPermission(executionData.getBotManager().getConfigManager(), executionData.getSender(), Permission.MANAGE_BACKUPS);
        }

        @Override
        protected Optional<String> customPreExecuteCheck() {
            if (executionData.getMessage().getAttachments().isEmpty()) {
                return Optional.of("No file to execute.");
            }
            return Optional.empty();
        }
    }
}
