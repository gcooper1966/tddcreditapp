package au.com.westpac.testing;

import au.com.westpac.testing.helpers.Address;
import au.com.westpac.testing.helpers.SimpleDependencies;
import org.junit.Test;

import static au.com.westpac.testing.assertions.CustomAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;

/**
 *
 * Created by M041451 on 27/02/2017.
 */
public class UnitTestingOfSimpleDependencies {

    private SimpleDependencies CUT; //class under test
    private int testAge = 21;
    private String testFirstName = "Fred";
    private String testLastName = "Smith";
    private Address mockAddress = mock(Address.class);

    @Test
    public void a_unit_test_should_be_focused_on_a_single_method(){
        Address wrongApproach = new Address();
        Address unimportant = mockAddress;
        CUT = new SimpleDependencies(testAge, testFirstName, testLastName, wrongApproach);
        CUT = new SimpleDependencies(21, "Fred", "Smith", unimportant);

        assertThat(CUT).isInitialisedCorrectly();
    }

    @Test
    public void unit_testing_simple_getters_and_setters_is_waste(){
        CUT = new SimpleDependencies(testAge, testFirstName, testLastName, mockAddress );

        assertThat(CUT.getAge()).isEqualTo(testAge);
        fail("Wasted effort for no value");
    }
}
