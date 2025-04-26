package com.pokemonreview.api.security;

import com.pokemonreview.api.models.Role;
import com.pokemonreview.api.models.UserEntity;
import com.pokemonreview.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService  implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * The loadUserByUsername method is used to load the user details from the database
     * using the username provided by the user during login.
     * It retrieves the user from the database and maps the roles to authorities.
     * If the user is not found, it throws a UsernameNotFoundException.
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    /*
     * The mapRolesToAuthorities method is used to convert the roles of the user into
     * GrantedAuthority objects.
     * This is necessary for Spring Security to understand the user's roles and
     * permissions.
     * It uses Java Streams to map each role to a SimpleGrantedAuthority object.
     * Finally, it collects the authorities into a collection and returns it.
     * This collection is then used by Spring Security to determine the user's
     * permissions and access rights.
     * 
     * This method is not in the original UserDetailsService interface, but it is a
     * custom method created to handle the mapping of roles to authorities.
     * 
     * The mapRolesToAuthorities method is creating an Object of SimpleGrantedAuthority that implemnets the GrantedAuthority interface.
     * and threrefore returning the collection of GrantedAuthority , which is the required type for the UserDetails constructor and this will be
     * used for the UsernamePasswordAuthenticationToken object in the JWTAuthenticationFilter class.
     */

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
