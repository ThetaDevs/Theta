package com.srgood.reasons.impl.commands.main;

import com.meowingtwurtle.math.api.IMathGroup;
import com.meowingtwurtle.math.api.IMathHandler;
import com.meowingtwurtle.math.api.MathExpressionParseException;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.permissions.Permission;
import com.srgood.reasons.impl.commands.permissions.PermissionChecker;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Optional;

public class CommandEvalDescriptor extends BaseCommandDescriptor {
    private final static ThreadLocal<NumberFormat> RESULT_FORMATTER = ThreadLocal.withInitial(() -> new DecimalFormat("#0.0###"));

    public CommandEvalDescriptor() {
        super(Executor::new, "Evaluates a math expression and prints result. Supports arithmetic operations, sin, cos, tan, abs, sqrt", "<math expr.>", "eval");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            try {
                String exp = executionData.getRawArguments();

                if (!exp.matches("[()\\d\\w\\s.+\\-*/^]+")) {
                    sendOutput("`MATH:` Expression contains invalid characters");
                    return;
                }

                IMathGroup group = IMathHandler.getMathHandler().parse(exp);
                sendOutput("`MATH:` %s", RESULT_FORMATTER.get().format(group.eval()));
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
                sendOutput("`MATH:` An error occurred during parsing: %s", t == null ? "null" : t.getClass()
                                                                                                 .getCanonicalName() + ": " + t
                        .getMessage());
            }
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return PermissionChecker.checkMemberPermission(executionData.getBotManager()
                                                                        .getConfigManager(), executionData.getSender(), Permission.EVALUATE_MATH);
        }
    }
}
