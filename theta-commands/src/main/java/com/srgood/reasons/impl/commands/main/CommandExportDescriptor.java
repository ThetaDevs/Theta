package com.srgood.reasons.impl.commands.main;

import com.google.common.io.Files;
import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.BaseConstants;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.CommandConstants;
import com.srgood.reasons.impl.commands.permissions.GuildPermissionSet;
import com.srgood.reasons.impl.commands.permissions.Permission;
import com.srgood.reasons.impl.commands.permissions.PermissionChecker;
import com.srgood.reasons.impl.commands.utils.GuildDataManager;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Role;
import org.apache.commons.codec.binary.Base32;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class CommandExportDescriptor extends BaseCommandDescriptor {
    public CommandExportDescriptor() {
        super(Executor::new, "Outputs a file that contains commands to reconstruct the bot state of this Guild.", null, "export");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        private static final String newline = "\r\n";
        private static final String DEFAULT_FILE_CONTENTS = "# THETA GUILD BACKUP FILE" + newline + "# GENERATED BY THETA v" + CommandConstants.VERSION_SUPPLIER
                .get() + newline + newline;

        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            StringBuilder output = new StringBuilder(DEFAULT_FILE_CONTENTS);

            generatePrefixOutput(output);
            generatePermissionsOutput(output);
            generateCommandEnabledOutput(output);
            generateBlacklistOutput(output);
            generateCensorOutput(output);
            generateGreetingOutput(output);
            generateGoodbyeOutput(output);

            try {
                File file = File.createTempFile("THETA_BACKUP_GUILD_" + executionData.getGuild().getId(), ".txt");
                String rawOutput = output.toString();
                String fakeEncryptedOutput = encrypt(rawOutput);
                Files.asCharSink(file, BaseConstants.FILE_CHARSET).write(fakeEncryptedOutput);
                executionData.getChannel()
                             .sendFile(file, new MessageBuilder().append("Output file generated.").build())
                             .queue();
            } catch (Exception e) {
                sendError("Internal I/O error, could not generate temp file.");
            }
        }

        private String encrypt(String rawOutput) {
            try {
                byte[] base32 = new Base32().encode(rawOutput.getBytes());
                String base64 = Base64.getEncoder().encodeToString(base32);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(base64);
                return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void generatePrefixOutput(StringBuilder output) {
            output.append("# Set the prefix");
            output.append(newline);
            output.append("PRFIX ")
                  .append(executionData.getBotManager()
                                       .getConfigManager()
                                       .getGuildConfigManager(executionData.getGuild())
                                       .getPrefix());
            output.append(newline);
        }

        private void generatePermissionsOutput(StringBuilder output) {
            output.append(newline);
            output.append("# Setup permissions");
            output.append(newline);
            GuildPermissionSet guildPermissionSet = GuildDataManager.getGuildPermissionSet(executionData.getBotManager()
                                                                                                        .getConfigManager(), executionData
                    .getGuild());
            for (Role role : executionData.getGuild().getRoles()) {
                output.append(newline)
                      .append("# Permissions for role ")
                      .append(role.getId())
                      .append(" AKA \"")
                      .append(role.getName())
                      .append("\"")
                      .append(newline);
                for (Permission permission : Permission.values()) {
                    output.append("PERMS ")
                          .append(role.getId())
                          .append(" ")
                          .append(permission.name())
                          .append(" ")
                          .append(guildPermissionSet.getPermissionStatus(role, permission))
                          .append(newline);
                }
            }
        }

        private void generateCommandEnabledOutput(StringBuilder output) {
            output.append(newline);
            output.append("# Set command enabled status");
            output.append(newline);

            for (CommandDescriptor commandDescriptor : executionData.getBotManager()
                                                                    .getCommandManager()
                                                                    .getRegisteredCommands()) {
                boolean enabled = executionData.getBotManager()
                                               .getConfigManager()
                                               .getGuildConfigManager(executionData.getGuild())
                                               .getCommandConfigManager(commandDescriptor)
                                               .isEnabled();
                output.append(enabled ? "ENBLE" : "DSBLE").append(" ").append(commandDescriptor.getPrimaryName());
                output.append(newline);
            }
        }

        private void generateBlacklistOutput(StringBuilder output) {
            List<String> blacklist = GuildDataManager.getGuildBlacklist(executionData.getBotManager()
                                                                                     .getConfigManager(), executionData.getGuild());
            if (blacklist.isEmpty()) {
                return;
            }

            output.append(newline);
            output.append("# Set blacklist");
            output.append(newline);
            for (String id : blacklist) {
                output.append("BLIST ").append(id);
                output.append(newline);
            }
        }

        private void generateCensorOutput(StringBuilder output) {
            List<String> censorList = GuildDataManager.getGuildCensorList(executionData.getBotManager()
                                                                                       .getConfigManager(), executionData
                    .getGuild());
            if (censorList.isEmpty()) {
                return;
            }

            output.append(newline);
            output.append("# Set censorlist");
            output.append(newline);
            for (String censorItem : censorList) {
                output.append("CLIST ").append(censorItem);
            }
            output.append(newline);
        }

        private void generateGreetingOutput(StringBuilder output) {
            String welcomeMessage = executionData.getBotManager()
                                                 .getConfigManager()
                                                 .getGuildConfigManager(executionData.getGuild())
                                                 .getProperty("moderation/welcome");
            String welcomeChannel = executionData.getBotManager()
                                                 .getConfigManager()
                                                 .getGuildConfigManager(executionData.getGuild())
                                                 .getProperty("moderation/welcomechannel");
            if (welcomeChannel != null) {
                output.append(newline);
                output.append("# Set welcome message and channel");
                output.append(newline);
                output.append("WLCME").append(" ").append(welcomeChannel).append(" ").append(welcomeMessage);
                output.append(newline);
            }
        }

        private void generateGoodbyeOutput(StringBuilder output) {
            String goodbyeMessage = executionData.getBotManager()
                                                 .getConfigManager()
                                                 .getGuildConfigManager(executionData.getGuild())
                                                 .getProperty("moderation/goodbye");
            String goodbyeChannel = executionData.getBotManager()
                                                 .getConfigManager()
                                                 .getGuildConfigManager(executionData.getGuild())
                                                 .getProperty("moderation/goodbyechannel");
            if (goodbyeChannel != null) {
                output.append(newline);
                output.append("# Set goodbye message and channel");
                output.append(newline);
                output.append("GDBYE").append(" ").append(goodbyeChannel).append(" ").append(goodbyeMessage);
                output.append(newline);
            }
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkMemberPermission(executionData.getBotManager()
                                                                        .getConfigManager(), executionData.getSender(), Permission.MANAGE_BACKUPS);
        }

        @Override
        protected Optional<String> checkBotPermissions() {
            if (!executionData.getGuild()
                              .getSelfMember()
                              .hasPermission((Channel) executionData.getChannel(), net.dv8tion.jda.core.Permission.MESSAGE_ATTACH_FILES)) {
                return Optional.of("Cannot attach output file because I don't have permission to do so.");
            }
            return Optional.empty();
        }
    }
}
