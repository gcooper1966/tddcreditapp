package au.com.westpac.testing;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static au.com.westpac.testing.assertions.CustomAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.CoreMatchers.containsString;

/**
 *
 * Created by Graeme Cooper on 23/02/2017.
 */
public class CheatSheet {

    @Test
    public void simple_assertions_should_be_readable(){
        String actual = "Hello Unit Test World";
        assertThat(actual).startsWith("Hello");
        assertThat(actual.length()).isGreaterThan(20);
    }

    @Test
    public void create_custom_assertions_to_make_tests_explicit(){
        DomainThing actual = new DomainThing(20, "fred", "Bloggs");
        assertThat(actual).isOldEnough(18);
        assertThat(actual).hasAName();
    }

    public  class DomainThing{
        private int age;
        private String firstName;
        private String lastName;

        DomainThing(int age, String firstName, String lastName) {
            this.age = age;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public int getAge() {
            return age;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void use_rules_for_setup_tasks_outside_the_specific_test(){
        //like creating a file that needs to be deleted when the test completes
        final File actual;
        try{
            actual = folder.newFile();
            assertThat(actual).exists();
        }catch(IOException ex){
            fail("Unable to create temporary file, check the permissions for the identity running the tests");
        }

        //like setting up an exception in a way that is more readable and explicit than using the ExpectedException annotation
        thrown.expect(IndexOutOfBoundsException.class);
        thrown.expectMessage(containsString("2"));
        thrown.reportMissingExceptionWithMessage("Expected and IndexOutOfBoundsException when attempting to access an element of an empty array but it either wasn't thrown or the message was unexpected");

        int[] actualInts = new int[]{5,6};
        int dumbThingToDo = actualInts[actualInts.length];

        fail("Expected to access an array element above the last index but found there was an index at the position");

        //notice that we never deleted the file but if you look you'll find it is gone.
    }

    @Rule
    public TestName name = new TestName();
    @Rule
    public TestRule classTestsTimeout = Timeout.builder().withTimeout(200, TimeUnit.MILLISECONDS).build();

    @Test
    public void use_rules_for_accessing_the_testing_context_to_stop_indefinite_tests(){
        StringBuffer log = new StringBuffer("running test");
        for(;;){
            log.append("inside loop");
        }
    }

    @Test
    public void use_rules_for_accessing_the_testing_context_to_provide_readability(){
        fail("We failed inside of " + name.getMethodName());
    }

}
