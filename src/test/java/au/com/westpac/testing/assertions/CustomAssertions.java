package au.com.westpac.testing.assertions;

import au.com.westpac.testing.CheatSheet;
import au.com.westpac.testing.helpers.SimpleDependencies;

import java.io.File;

/**
 *
 * Created by Graeme Cooper on 23/02/2017.
 */
public class CustomAssertions {
    public static DomainThingAssert assertThat(CheatSheet.DomainThing actual){
        return new DomainThingAssert(actual);
    }

    public static SimpleDependenciesAssert assertThat(SimpleDependencies actual) {
        return new SimpleDependenciesAssert(actual);
    }

    public static XmlFileAssert assertThat(File actual){
        return new XmlFileAssert(actual);
    }
}
