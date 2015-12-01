package com.techlooper.config.web.sec;

import com.techlooper.entity.vnw.VnwUser;
import com.techlooper.repository.vnw.VnwUserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by phuonghqh on 11/17/15.
 */
public class SecurityUserDetailsServiceImpl implements UserDetailsService {

  @Resource
  private VnwUserRepo vnwUserRepo;

  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    VnwUser vnwUser = vnwUserRepo.findByUsernameIgnoreCase(username);
    if (vnwUser != null) {
      return new User(username, vnwUser.getUserPass(), Arrays.asList(new SimpleGrantedAuthority(vnwUser.getRoleName().name())));
    }
    return null;
  }
}
