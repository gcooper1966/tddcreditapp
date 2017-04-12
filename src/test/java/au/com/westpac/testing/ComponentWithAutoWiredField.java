package au.com.westpac.testing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by M041451 on 23/03/2017.
 */
@Component
public class ComponentWithAutoWiredField {

    @Autowired
    private Foo foo;

}
