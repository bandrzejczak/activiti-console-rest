package pl.edu.pw.ii.ActivitiConsole.api;

import org.activiti.engine.IdentityService;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.User;
import org.restlet.security.Verifier;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthenticationVerifier implements Verifier {
    public final static int AUTHENTICATION_OBSOLETE = RESULT_VALID;

    private Request request;
    @Autowired
    private Router router;
    @Autowired
    private IdentityService identityService;

    @Override
    public int verify(Request request, Response response) {
        this.request = request;
        if(!isSecuredResource())
            return AUTHENTICATION_OBSOLETE;
        if(request.getChallengeResponse() == null)
            return RESULT_MISSING;
        if(checkCredentials()){
            String userLogin = getUserLogin();
            request.getClientInfo().setUser(new User(userLogin));
            identityService.setAuthenticatedUserId(userLogin);
            return RESULT_VALID;
        }
        return RESULT_INVALID;
    }

    private boolean isSecuredResource() {
        return router.isPathSecured(RequestUtils.getRelativePath(request));
    }

    private boolean checkCredentials() {
        String userLogin = getUserLogin();
        return userLogin != null && identityService.checkPassword(userLogin, getUserPassword());
    }

    private String getUserLogin() {
        return request.getChallengeResponse().getIdentifier();
    }

    private String getUserPassword() {
        return new String(request.getChallengeResponse().getSecret());
    }
}
