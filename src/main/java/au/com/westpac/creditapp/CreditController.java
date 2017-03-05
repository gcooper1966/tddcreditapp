package au.com.westpac.creditapp;

import au.com.westpac.creditapp.repositories.ApplicationRepository;
import au.com.westpac.creditapp.resources.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by M041451 on 27/02/2017.
 */
@Controller
public class CreditController {
    private ApplicationRepository applicationRepository;
    private UserManager userManager;

    @Autowired
    public CreditController(ApplicationRepository applicationRepository, UserManager userManager) {
        this.applicationRepository = applicationRepository;
        this.userManager = userManager;
    }

    public List<Application> listOutstandingApplications() {
        return applicationRepository.findByUserId(userManager.currentUser().getUserId());
    }

    public List<Application> listRejectedApplications() {
        return applicationRepository.findRejected(userManager.currentUser().getUserId());
    }
}
