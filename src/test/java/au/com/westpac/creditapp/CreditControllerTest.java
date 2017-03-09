package au.com.westpac.creditapp;

import au.com.westpac.creditapp.repositories.ApplicationRepository;
import au.com.westpac.creditapp.resources.Application;
import au.com.westpac.testing.utils.Log4j2MockAppenderConfigurer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


/**
 *
 * Created by M041451 on 27/02/2017.
 */
public class CreditControllerTest {

    private CreditController CUT;
    private ApplicationRepository applicationRepository;
    private UserManager userManager;
    private User user;
    private Identifier<String> id;
    private final Appender mockAppender = mock(Appender.class);
    private static List<Application> applications;

    @Before
    public void setUp() throws Exception {
        id = mock(Identifier.class);
        applicationRepository = mock(ApplicationRepository.class);
        userManager = mock(UserManager.class);
        user = mock(User.class);

        CUT = new CreditController(applicationRepository, userManager);
        applications = new ArrayList<>(2);
        initialiseUserMock(applications);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void can_list_outstanding_applications() throws InvalidUserException {
        //arrange
        List<Application> applications = getApplications();
        when(applicationRepository.findByUserId(userManager.currentUser().getUserId())).thenReturn(applications);
        //act
        List<Application> actual = CUT.listOutstandingApplications();
        //assert
        assertThat(actual.size()).isEqualTo(2);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void cannot_list_applications_without_a_valid_user() throws InvalidUserException {
        List<Application> applications = getApplications();
        when(user.isValid()).thenReturn(false);
        thrown.expect(InvalidUserException.class);
        CUT.listOutstandingApplications();
    }

    @Test
    public void list_applications_logs_success_calls() throws InvalidUserException {
        ArgumentCaptor<LogEvent> logEvent = ArgumentCaptor.forClass(LogEvent.class);
        Log4j2MockAppenderConfigurer.addAppender(mockAppender);
        List<Application> applications = getApplications();
        when(applicationRepository.findByUserId(userManager.currentUser().getUserId())).thenReturn(applications);

        CUT.listOutstandingApplications();

        verify(mockAppender).append(logEvent.capture());
        assertThat(logEvent.getValue().getLevel()).isEqualTo(Level.DEBUG);
        assertThat(logEvent.getValue().getMessage().getFormattedMessage()).startsWith("Successfully");
    }

    @Test
    public void list_applications_logs_errors(){

    }

    @Test
    public void can_list_rejected_applications_in_specific_period(){
        //arrange
        List<Application> applications = getApplications();
        when(applicationRepository.findRejected(userManager.currentUser().getUserId())).thenReturn(applications);
        //act
        List<Application> actual = CUT.listRejectedApplications();
        //assert
        assertThat(actual.size()).isEqualTo(2);
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

    private void configureMockUser() {
        when(user.isValid()).thenReturn(true);
        when(id.getId()).thenReturn("my id");
        doReturn(id).when(user).getUserId();
    }

    private static List<Application> getApplications() {
        applications.add(new Application());
        applications.add(new Application());
        return applications;
    }

    private void initialiseUserMock(List<Application> applications) {
        when(userManager.currentUser()).thenReturn(user);
        configureMockUser();
    }
}