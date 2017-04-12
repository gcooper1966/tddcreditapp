package au.com.westpac.integration.setup;

import au.com.westpac.creditapp.UserManager;
import au.com.westpac.creditapp.repositories.ApplicationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Graeme on 5/03/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testConfig.xml"})
@WebAppConfiguration
public class SpringSetupTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ApplicationRepository applicationRepository;


    private MockMvc mockMvc;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void context_contains_beans(){
        assertThat(webApplicationContext.getBean(UserManager.class));
        assertThat(webApplicationContext.getBean("mockApplicationRepository")).isNotNull();
    }

    @Test
    public void can_autowire_beans(){
        assertThat(userManager).isNotNull();
        assertThat(applicationRepository).isNotNull();
    }


}
