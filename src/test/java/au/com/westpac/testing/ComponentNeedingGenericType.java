package au.com.westpac.testing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by M041451 on 23/03/2017.
 */
@Component
public class ComponentNeedingGenericType {

    private final GenericParameterType<Foo> aType;

    @Autowired
    public ComponentNeedingGenericType(GenericParameterType<Foo> aType) {
        this.aType = aType;
    }
}
