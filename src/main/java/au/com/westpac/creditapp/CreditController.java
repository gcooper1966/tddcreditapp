package au.com.westpac.creditapp;

import au.com.westpac.creditapp.repositories.ApplicationRepository;
import au.com.westpac.creditapp.resources.Application;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by M041451 on 27/02/2017.
 */
@Controller
public class CreditController {
    ApplicationRepository applicationRepository;

    public CreditController(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<Application> listOutstandingApplications() {
        return applicationRepository.findByUserId();
    }
}
