package com.srgood.reasons.commands;

import com.meowingtwurtle.math.api.IMathGroup;
import com.meowingtwurtle.math.api.IMathHandler;
import com.meowingtwurtle.math.api.MathExpressionParseException;
import com.srgood.reasons.ReasonsMain;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by srgood on 1/2/2017.
 */
public class CommandOreoEval implements Command{
    private final static NumberFormat RESULT_FORMATTER = new DecimalFormat("#0.0###");

    public CommandOreoEval() {

    }

    private String exp;

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        exp = join(args);

        if (!exp.matches("[()\\d\\w\\s.+\\-*/^]+")) {
            event.getChannel().sendMessage("`MATH:` Expression contains invalid characters");
            return false;
        }
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        try {
            IMathGroup group = IMathHandler.getMathHandler().parse(exp);
            event.getChannel().sendMessage("`MATH:` " + (Float.parseFloat(RESULT_FORMATTER.format(group.eval())) * 0.93) ).queue();
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
            event.getChannel().sendMessage("`MATH:` An error occurred during parsing: " + (t == null ? "null" : t.getClass().getCanonicalName() + ": " + t.getMessage())).queue();
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
        return "Evaluates a math expression and prints result. Supports arithmetic operations, sin, cos, tan, abs, sqrt. Use: '" + ReasonsMain.prefix + "eval <exp>'";
    }

    @Override
    public String[] names() {
        return new String[] {"oreoeval","oreomath"};
    }

}

