package au.com.westpac.testing.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * Created by M041451 on 9/03/2017.
 */
public class Log4j2MockAppenderConfigurer {
    public void addAppender(final Appender mockAppender){
        final LoggerContext context = LoggerContext.getContext(false);
        final Configuration config = context.getConfiguration();
        config.addAppender(mockAppender);
        updateLoggers(mockAppender, config);
    }

    private void updateLoggers(final Appender appender, final Configuration config) {
        final Level level = null;
        final Filter filter = null;
        for (final LoggerConfig loggerConfig : config.getLoggers().values()) {
            loggerConfig.addAppender(appender, level, filter);
        }
        config.getRootLogger().addAppender(appender, level, filter);
    }
}
