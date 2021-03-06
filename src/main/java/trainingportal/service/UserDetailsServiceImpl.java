package trainingportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import trainingportal.dao.RoleDao;
import trainingportal.dao.UserDao;
import trainingportal.model.LoggedInUser;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        trainingportal.model.User user = this.userDao.findUserAccount(email);

        if (user == null) {
            throw new UsernameNotFoundException(email + " was not found in the database");
        }

        if(user.getEnabled() == 0){
            throw new UsernameNotFoundException(email + " is not enabled");
        }

        // [ROLE_USER, ROLE_ADMIN,..]
        List<String> roleNames = this.roleDao.getRoleNames(user.getUserId());

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        if (roleNames != null) {
            for (String role : roleNames) {
                // ROLE_USER, ROLE_ADMIN,..
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantList.add(authority);
            }
        }

        return (UserDetails) new LoggedInUser(user.getUserName(),
                user.getPassword(), grantList, user.getUserId());
    }
}