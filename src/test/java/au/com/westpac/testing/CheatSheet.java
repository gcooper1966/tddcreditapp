package au.com.westpac.testing;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static au.com.westpac.testing.assertions.CustomAssertions.assertThat;

/**
 * Created by Graeme Cooper on 23/02/2017.
 */
public class CheatSheet {

    @Test
    public void simple_assertions_should_be_readable(){
        String actual = "Hello Unit Test World";
        assertThat(actual).startsWith("Hello");
        assertThat(actual.length()).isGreaterThan(21);
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
}
