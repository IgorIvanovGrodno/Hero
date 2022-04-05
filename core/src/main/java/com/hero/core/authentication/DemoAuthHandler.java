package com.hero.core.authentication;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.auth.core.spi.AuthenticationFeedbackHandler;
import org.apache.sling.auth.core.spi.AuthenticationHandler;
import org.apache.sling.auth.core.spi.AuthenticationInfo;
import org.apache.sling.auth.core.spi.DefaultAuthenticationFeedbackHandler;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.crx.security.token.TokenUtil;
import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * Custom bundle must be whitelisted. Go to configMgr, select "bypass the whitelist" in Apache Sling Login Admin Whitelist
 * Use test login with url such as http://localhost:4503/content/we-retail/us/en.html?j_username=admin&j_password=admin
 * Go to the CRXDE http://localhost:4503/crx/de/index.jsp to see that you did login as the user
 */
@SuppressWarnings("deprecation")
@Component(service = AuthenticationHandler.class, immediate = true, property = { "path=/content/Hero",
        Constants.SERVICE_RANKING +":Integer=90000", Constants.SERVICE_DESCRIPTION +"=Hero Authenticator" })
public class DemoAuthHandler extends DefaultAuthenticationFeedbackHandler implements AuthenticationHandler, AuthenticationFeedbackHandler {
    private static final Logger log = LoggerFactory.getLogger(DemoAuthHandler.class);
    @Reference(target = "(service.pid=com.day.crx.security.token.impl.impl.TokenAuthenticationHandler)")
    private AuthenticationHandler wrappedAuthHandler;
    @Reference
    private SlingRepository repository;
    @Reference
    public ResourceResolverFactory rrFactory;
    /**
     * Demo Authentication Handler gets the username j_username and password j_password from the request parameters to retrieve user credentials.
     * This logs the user into the session. Otherwise return a null authInfo object.
     */
    public AuthenticationInfo extractCredentials(HttpServletRequest request, HttpServletResponse response) {
        String j_username = request.getParameter("j_username");
        String j_password = request.getParameter("j_password");
        if( (j_username!=null && j_password!=null)) {
            SimpleCredentials creds = new SimpleCredentials(j_username,
                    j_password.toCharArray());
            AuthenticationInfo authInfo = null;
            Session session = null;
            try {
                session = this.repository.login(creds);
                this.repository.login(creds);
                if(session != null) {
                    authInfo = createAuthenticationInfo(request, response, creds.getUserID());
                    if(session.isLive()) {
                        session.logout();
                    }
                    return authInfo;
                }
            } catch (LoginException e) {
                log.error(this.getClass().getName() + " extractCredentials(..) Failed to log user in" + e.getMessage(), e);
                e.printStackTrace();
            } catch (RepositoryException e) {
                log.error(this.getClass().getName() + " extractCredentials(..) Failed to log user in" + e.getMessage(), e);
                e.printStackTrace();
            }
        }
        return null;
    }
    private AuthenticationInfo createAuthenticationInfo(HttpServletRequest request, HttpServletResponse response,
                                                        String userId) throws RepositoryException {
        AuthenticationInfo authinfo = TokenUtil.createCredentials(request, response, this.repository, userId, true);
        return authinfo;
    }
    @Override
    public void dropCredentials(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error("Custom override of dropCredentials has not been implemented");
    }
    @Override
    public boolean requestCredentials(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error("Custom override of requestCredentials has not been implemented");
        return false;
    }
}
