package au.com.westpac.testing.assertions;

import au.com.westpac.testing.CheatSheet;
import org.assertj.core.api.AbstractAssert;

/**
 * Created by Graeme Cooper on 23/02/2017.
 */
public class DomainThingAssert extends AbstractAssert<DomainThingAssert, CheatSheet.DomainThing> {

    DomainThingAssert(CheatSheet.DomainThing actual) {
        super(actual, DomainThingAssert.class);
    }

    public static DomainThingAssert assertThat(CheatSheet.DomainThing actual){
        return new DomainThingAssert(actual);
    }

    public DomainThingAssert isOldEnough(int minimumAge){
        isNotNull();

        if(actual.getAge() <  minimumAge){
            failWithMessage(String.format("Expected the Thing to be older than %d but was only %d", minimumAge, actual.getAge()));
        }
        return this;
    }

    public DomainThingAssert hasAName(){
        isNotNull();

        if(actual.getFirstName().isEmpty() || actual.getLastName().isEmpty()){
            failWithMessage("Expected the Thing to have a first and last name but it didn't");
        }

        return this;
    }
}
