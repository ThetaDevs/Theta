package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.lang.management.ManagementFactory;

public class CommandStatusDescriptor extends BaseCommandDescriptor {
    public CommandStatusDescriptor() {
        super(Executor::new, "Displays the current status of the bot", null, false, "status");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            Runtime runtime = Runtime.getRuntime();
            String totalMemory = formatMemoryMB(runtime.totalMemory());
            String freeMemory = formatMemoryMB(runtime.freeMemory());
            String usedMemory = formatMemoryMB(runtime.totalMemory() - runtime.freeMemory());
            int threadCount = Thread.getAllStackTraces().size();

            embedBuilder.addField("Shard", executionData.getJDA().getShardInfo().getShardString(), false);
            embedBuilder.addField("JVM Uptime", "" + ManagementFactory.getRuntimeMXBean().getUptime(), false);
            embedBuilder.addField("Used memory", usedMemory, false);
            embedBuilder.addField("Free memory", freeMemory, false);
            embedBuilder.addField("Total memory", totalMemory, false);
            embedBuilder.addField("Total threads", String.valueOf(threadCount), false);
            embedBuilder.setColor(Color.GREEN);

            MessageEmbed embed = embedBuilder.build();
            Message message = new MessageBuilder().setEmbed(embed).build();

            sendOutput(message);
        }

        private static String formatMemoryMB(double number) {
            return String.format("%.0fMB", number / 1024 / 1024);
        }
    }
}
