package au.com.westpac.testing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by M041451 on 23/03/2017.
 */
@Component
public class ComponentNeedingType {
    private final Foo foo;

    @Autowired
    public ComponentNeedingType(Foo foo) {
        this.foo = foo;
    }
}
