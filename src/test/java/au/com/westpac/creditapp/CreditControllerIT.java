package au.com.westpac.creditapp;

import au.com.westpac.creditapp.repositories.ApplicationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Created by Graeme on 5/03/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:testConfig.xml"})
public class CreditControllerIT {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ApplicationRepository mockApplicationRepository;

    @Autowired
    private UserManager mockUserManager;

    private MockMvc mockMvc;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }


    @Test
    public void can_list_outstanding_applications() throws Exception {
        mockMvc.perform(get("/tdd").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
