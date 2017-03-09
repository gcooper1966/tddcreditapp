package au.com.westpac.testing.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import static org.mockito.Mockito.when;

/**
 * Created by M041451 on 9/03/2017.
 */
public class Log4j2MockAppenderConfigurer {
    public static void addAppender(final Appender mockAppender){
        final LoggerContext context = LoggerContext.getContext(false);
        final Configuration config = context.getConfiguration();
        when(mockAppender.getName()).thenReturn("mockappender");
        when(mockAppender.isStarted()).thenReturn(true);
        config.addAppender(mockAppender);
        updateLoggers(mockAppender, config);
    }

    private static void updateLoggers(final Appender appender, final Configuration config) {
        final Level level = Level.DEBUG;
        final Filter filter = null;
        for (final LoggerConfig loggerConfig : config.getLoggers().values()) {
            loggerConfig.addAppender(appender, level, filter);
        }
        config.getRootLogger().addAppender(appender, level, filter);
    }
}
