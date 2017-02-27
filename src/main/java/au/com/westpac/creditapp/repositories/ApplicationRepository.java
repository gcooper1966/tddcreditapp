package au.com.westpac.creditapp.repositories;

import au.com.westpac.creditapp.Identifier;
import au.com.westpac.creditapp.resources.Application;

import java.util.List;

/**
 * Created by M041451 on 27/02/2017.
 */
public interface ApplicationRepository {
    List<Application> findByUserId(Identifier<?> userId);
}
