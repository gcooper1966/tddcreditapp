package au.com.westpac.creditapp;

import au.com.westpac.creditapp.repositories.ApplicationRepository;
import au.com.westpac.creditapp.resources.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 *
 * Created by M041451 on 27/02/2017.
 */
public class CreditControllerTest {

    private CreditController CUT;
    private ApplicationRepository applicationRepository;
    private UserManager userManager;
    private User user;

    @Before
    public void setUp() throws Exception {
        applicationRepository = mock(ApplicationRepository.class);
        userManager = mock(UserManager.class);
        user = mock(User.class);

        CUT = new CreditController(applicationRepository, userManager);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void can_list_outstanding_applications(){
        //arrange
        List<Application> applications = new ArrayList<>(2);
        applications.add(new Application());
        applications.add(new Application());
        when(userManager.currentUser()).thenReturn(user);
        when(applicationRepository.findByUserId(userManager.currentUser().getUserId())).thenReturn(applications);
        List<Application> actual = CUT.listOutstandingApplications();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    public void can_list_rejected_applications_in_specific_period(){

    }

    @Test
    public void can_list_approved_applications_in_specific_period(){

    }

    @Test
    public void can_search_for_applications(){

    }

    @Test
    public void can_submit_new_applications(){

    }

    @Test
    public void can_update_unprocessed_applications(){

    }

    @Test
    public void cannot_update_processed_application(){

    }

    @Test
    public void invalid_applications_provide_error_hints(){

    }


}