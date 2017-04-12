package au.com.westpac.testing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Created by M041451 on 23/03/2017.
 */
@Component
public class GenericParameterType<T> {
    private static Logger log = LogManager.getLogger();
    public void doSomthing(T something){
        log.debug(something);
    }
}
