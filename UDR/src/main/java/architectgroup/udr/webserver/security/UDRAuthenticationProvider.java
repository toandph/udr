package architectgroup.udr.webserver.security;

import architectgroup.fact.access.UserAccess;
import architectgroup.fact.access.util.FactAccessFactory;
import architectgroup.fact.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/2/13
 * Time: 4:42 PM
 */
public class UDRAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    FactAccessFactory factAccessFactory;

    public boolean supports(Class<? extends Object> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public Authentication authenticate(Authentication authentication) {
        UserAccess userAccess = new UserAccess(factAccessFactory);
        UserDto user = userAccess.userAuthenticate(authentication.getName(), authentication.getCredentials().toString());
        if (user != null) {
            GrantedAuthority role = new SimpleGrantedAuthority(user.getRoleInString());
            List<GrantedAuthority> listRole = new ArrayList<GrantedAuthority>();
            listRole.add(role);
            return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials(), listRole);
        }
        else {
            return null;
        }
    }
}