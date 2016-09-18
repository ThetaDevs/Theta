package com.srgood.dbot.commands;

import com.meowingtwurtle.math.api.IMathGroup;
import com.meowingtwurtle.math.api.IMathHandler;
import com.meowingtwurtle.math.api.MathExpressionParseException;
import com.srgood.dbot.utils.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CommandEval implements Command {

    private final static NumberFormat RESULT_FORMATTER = new DecimalFormat("#0.0###");

    public CommandEval() {

    }

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        try {
            String exp = join(args);

            if (!exp.matches("[()\\d\\w\\s.+\\-*/^]+")) {
                event.getChannel().sendMessage("`MATH:` Expression contains invalid characters");
                return;
            }

            IMathGroup group = IMathHandler.getMathHandler().parse(exp);
            event.getChannel().sendMessage("`MATH:` " + RESULT_FORMATTER.format(group.eval()));
        } catch (Exception e) {
            e.printStackTrace();
            Throwable t = e;
            while (t != null && t instanceof MathExpressionParseException) {
                if (t.getCause() != null) {
                    t = t.getCause();
                } else {
                    break;
                }
            }
            event.getChannel().sendMessage("`MATH:` An error occurred during parsing: " + (t == null ? "null" : t.getClass().getCanonicalName() + ": " + t.getMessage()));
        }
    }

    private String join(Object[] arr) {
        String ret = "";
        for (Object o : arr) {
            ret += o.toString();
        }
        return ret;
    }

    @Override
    public String help() {
        return "no way man";
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {
    }

    @Override
    public com.srgood.dbot.PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return ConfigUtils.getCommandPermissionXML(guild, this);
    }

    @Override
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        return com.srgood.dbot.PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"eval"};
    }

}
