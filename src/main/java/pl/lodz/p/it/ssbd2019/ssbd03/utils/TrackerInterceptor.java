package pl.lodz.p.it.ssbd2019.ssbd03.utils;


import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.Objects;
import java.util.logging.Logger;

@Interceptor
public class TrackerInterceptor {

    @Resource
    private SessionContext sessionContext;

    private static final Logger logger = Logger.getLogger(TrackerInterceptor.class.getName());

    @AroundInvoke
    public Object logMethodInvocation(InvocationContext context) throws Exception {

        String className = context.getTarget().getClass().getCanonicalName();
        String methodName = context.getMethod().getName();
        String user = sessionContext.getCallerPrincipal().getName();

        String parameters = retrieveMethodParameters(context);

        logger.info( () ->
                String.format("%s.%s(%s) zostala wywolana przez uzytkownika %s", className, methodName, parameters, user)
        );

        Object result = proceedMethod(context, className, methodName, user, parameters);

        String resultString = context.getMethod().getReturnType().equals(Void.TYPE) ? "void" : Objects.toString(result);
        logger.info( () ->
                String.format("%s.%s(%s) wywolana przez uzytkownika %s zwrocila %s",
                className, methodName, parameters, user, resultString)
        );

        return result;
    }

    private String retrieveMethodParameters(InvocationContext context) {
        if (context.getParameters() != null) {
            return concatenateParameters(context);
        } else {
            return "";
        }
    }

    private String concatenateParameters(InvocationContext context) {
        StringBuilder parameters = new StringBuilder();

        for (Object param : context.getParameters()) {
            if (parameters.length() > 0) {
                parameters.append(", ");
            }
            parameters.append(param.toString());
        }

        return parameters.toString();
    }

    private Object proceedMethod(InvocationContext context,
                                 String className,
                                 String methodName,
                                 String user,
                                 String parameters) throws Exception {
        Object result;

        try {
            result = context.proceed();
        } catch (Exception e) {
            String causes = concatenateCauses(e);

            logger.severe( () ->
                    String.format("%s.%s(%s) wywolana przez uzytkownika %s rzucila następujacy wyjatek %s: %s. Causes: [%s]",
                            className, methodName, parameters, user, e, e.getLocalizedMessage(), causes)
            );

            throw e;
        }

        return result;
    }

    private String concatenateCauses(Exception ex) {
        StringBuilder causes = new StringBuilder();

        Throwable cause = ex.getCause();
        while (cause != null) {
            if (causes.length() > 0) {
                causes.append(", ");
            }
            causes.append(cause);
            cause = cause.getCause();
        }

        return causes.toString();
    }
}