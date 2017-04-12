package au.com.westpac.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by M041451 on 23/03/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testAutowireConfig.xml"})
@WebAppConfiguration
public class SpringAutowiredInterceptorIT {

    @Autowired
    private ComponentNeedingType simpleComponent;

    @Autowired
    private ComponentNeedingGenericType genericTypeComponent;

    @Autowired
    private ComponentWithAutoWiredField simpleFieldDependentComponent;


    @Test
    public void autowired_dependencies_are_mocked(){
        assertThat(simpleComponent).isNotNull();
        assertThat(genericTypeComponent).isNotNull();
        assertThat(simpleFieldDependentComponent).isNotNull();
    }
}
