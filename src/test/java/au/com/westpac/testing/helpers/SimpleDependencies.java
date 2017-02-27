package au.com.westpac.testing.helpers;

/**
 *
 * Created by M041451 on 27/02/2017.
 */
public class SimpleDependencies {

    private int age;
    private String firstName;
    private String lastName;
    private Address address;

    public SimpleDependencies(int age, String firstName, String lastName, Address address) {
        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
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

    public Address getAddress() {
        return address;
    }
}
