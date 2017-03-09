package au.com.westpac.creditapp;

import au.com.westpac.creditapp.repositories.ApplicationRepository;
import au.com.westpac.creditapp.resources.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by M041451 on 27/02/2017.
 */
@Controller
public class CreditController {

    private static Logger log = LogManager.getLogger();
    private ApplicationRepository applicationRepository;
    private UserManager userManager;

    @Autowired
    public CreditController(ApplicationRepository applicationRepository, UserManager userManager) {
        this.applicationRepository = applicationRepository;
        this.userManager = userManager;
    }

    public List<Application> listOutstandingApplications() throws InvalidUserException {
        User user = userManager.currentUser();
        if(!user.isValid()){
            throw new InvalidUserException();
        }
        List<Application> appplications = applicationRepository.findByUserId(userManager.currentUser().getUserId());
        if(appplications != null){
            log.debug("Successfully found outstanding applications for user {}", user.getUserId());
        }
        return applicationRepository.findByUserId(userManager.currentUser().getUserId());
    }

    public List<Application> listRejectedApplications() {
        return applicationRepository.findRejected(userManager.currentUser().getUserId());
    }
}
