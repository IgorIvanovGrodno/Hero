//package com.hero.core.authentication;
//
//import com.day.crx.security.token.TokenUtil;
//import org.apache.commons.lang.StringUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.apache.jackrabbit.api.JackrabbitSession;
//import org.apache.jackrabbit.api.security.user.Authorizable;
//import org.apache.jackrabbit.api.security.user.Group;
//import org.apache.jackrabbit.api.security.user.User;
//import org.apache.jackrabbit.api.security.user.UserManager;
//import org.apache.sling.api.resource.ResourceResolver;
//import org.apache.sling.api.resource.ResourceResolverFactory;
//import org.apache.sling.auth.core.spi.AuthenticationHandler;
//import org.apache.sling.auth.core.spi.AuthenticationInfo;
//import org.apache.sling.jcr.api.SlingRepository;
//import org.osgi.framework.Constants;
//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.component.annotations.Reference;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.jcr.RepositoryException;
//import javax.jcr.Session;
//import javax.jcr.SimpleCredentials;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component(service = CustomAuthenticator.class, immediate = true, property = { "path=/content/Hero",
//        Constants.SERVICE_RANKING +":Integer=60000", Constants.SERVICE_DESCRIPTION +"=Hero Authenticator" })
//public class CustomAuthenticator implements AuthenticationHandler {
//
//    private static final String REQUEST_METHOD = "GET";
//    static final String TOKEN_PARAMETER = "token";
//    private final Logger log = LoggerFactory.getLogger(CustomAuthenticator.class);
//
//    @Reference
//    private ResourceResolverFactory resourceResolverFactory;
//
//    @Reference
//    private SlingRepository repository;
//
//    public AuthenticationInfo extractCredentials(HttpServletRequest request, HttpServletResponse response) {
//        if (REQUEST_METHOD.equals(request.getMethod()) && (request.getParameter(TOKEN_PARAMETER) != null)) {
//            Map<String, Object> param = new HashMap<>();
//            param.put(ResourceResolverFactory.USER, "my-system-user");
//            ResourceResolver resolver = null;
//            try {
//                String userId = obtainUserId(request.getParameter(TOKEN_PARAMETER));
//                if(userId != null) {
//                    resolver = resourceResolverFactory.getServiceResourceResolver(param);
//                    UserManager userManager = ((JackrabbitSession) resolver.adaptTo(Session.class)).getUserManager();
//                    Authorizable user = userManager.getAuthorizable(userId);
//                    if(user == null) {
//                        createNewUser(userManager, userId);
//                        resolver.commit();
//                    }
//
//                    Session session = this.repository.login(new SimpleCredentials(userId, userId.toCharArray()));
//
//                    if (session != null) {
//                        return createAuthenticationInfo(request, response, session.getUserID());
//                    }
//                }
//            } catch (Exception e) {
//                log.error("Exception in extractCredentials while processing the request {}", e);
//            }finally {
//                if(resolver != null && resolver.isLive())
//                    resolver.close();
//            }
//        }
//        return null;
//    }
//
//    private AuthenticationInfo createAuthenticationInfo(HttpServletRequest request, HttpServletResponse response,
//                                                        String userId) throws RepositoryException {
//        return TokenUtil.createCredentials(request, response, this.repository, userId, true);
//    }
//
//    public void dropCredentials(HttpServletRequest arg0, HttpServletResponse arg1) {}
//
//    public boolean requestCredentials(HttpServletRequest request, HttpServletResponse arg1) {return true;}
//
//    public String obtainUserId(String token) {
//        HttpPost httpPost = new HttpPost("http://localhost:8080/api/user/getUserId");
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        try (CloseableHttpResponse response = httpClient.execute(httpPost)){
//            List<NameValuePair> urlParameters = new ArrayList<>();
//            urlParameters.add(new BasicNameValuePair(TOKEN_PARAMETER, token));
//            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
//            HttpEntity entity = response.getEntity();
//            if (response.getStatusLine().getStatusCode() != 200) {
//                log.error("Unable to obtain user id from web service! Status: " + response.getStatusLine().getStatusCode());
//                return null;
//            }
//
//            String userId = StringUtils.EMPTY;
//            String output;
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//            while ((output = bufferedReader.readLine()) != null) {
//                userId = userId + output;
//            }
//            EntityUtils.consume(entity);
//            return userId;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    private boolean createNewUser(UserManager userManager, String userId) {
//
//        try {
//            Group group = (Group) userManager.getAuthorizable("dam-users");
//            Authorizable user = userManager.getAuthorizable(userId);
//            if (user == null) {
//                user = userManager.createUser(userId,userId);
//                group.addMember(user);
//            } else if (!group.isMember(user)) {
//                group.addMember(user);
//                if(((User) user).isDisabled())
//                    ((User) user).disable(null);
//            }
//        }catch (Exception e){
//            log.error("Error while creating new user! ", e);
//            return false;
//        }
//        return true;
//    }
//
//    protected void bindRepository(SlingRepository paramSlingRepository) {
//        this.repository = paramSlingRepository;
//    }
//
//    protected void unbindRepository(SlingRepository paramSlingRepository) {
//        if (this.repository == paramSlingRepository) {
//            this.repository = null;
//        }
//    }
//}
