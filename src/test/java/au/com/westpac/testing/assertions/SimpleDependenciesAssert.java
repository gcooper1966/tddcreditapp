package au.com.westpac.testing.assertions;

import au.com.westpac.testing.helpers.SimpleDependencies;
import org.assertj.core.api.AbstractAssert;

/**
 *
 * Created by M041451 on 27/02/2017.
 */
public class SimpleDependenciesAssert extends AbstractAssert<SimpleDependenciesAssert, SimpleDependencies> {
    public SimpleDependenciesAssert(SimpleDependencies actual) {
        super(actual, SimpleDependenciesAssert.class);
    }

    public static SimpleDependenciesAssert assertThat(SimpleDependencies actual) {
        return new SimpleDependenciesAssert(actual);}

    public SimpleDependenciesAssert isInitialisedCorrectly(){
        isNotNull();
        if((invalidAge(actual.getAge()) || invalidName(actual.getFirstName())
                || invalidName(actual.getLastName()) || isNull(actual.getAddress()))){
            failWithMessage("SimpleDependencies was not initialised correctly");
        }
        return this;
    }

    private boolean invalidName(String name) {
        return (name == null)? true : name.isEmpty();
    }

    private boolean invalidAge(int age){
        return age > 0;
    }

    private boolean isNull(Object o){
        return o == null;
    }
}
