package com.techlooper.repository.vnw;

import com.techlooper.entity.vnw.RoleName;
import com.techlooper.entity.vnw.VnwUser;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by phuonghqh on 6/25/15.
 */
@Service
@Profile("local")
public class VnwUserRepoLocal implements VnwUserRepo {

  public VnwUser findByUsernameIgnoreCase(String username) {
    return userThuHoang();
  }


  public VnwUser findOne(Long aLong) {
    return userThuHoang();
  }

  public Iterable<VnwUser> findAll() {
    List<VnwUser> ls = Collections.emptyList();
    ls.add(userThuHoang());
    return ls;
  }

  public VnwUser findByUsernameIgnoreCaseAndUserPassAndRoleName(String username, String userPass, RoleName roleName) {
    return userThuHoang();
  }

  private static VnwUser userThuHoang() {
    return VnwUser.VnwUserBuilder.vnwUser()
      .withRoleName(RoleName.EMPLOYER).withUsername("thu.hoang@navigosgroup.com").withEmail("thu.hoang@navigosgroup.com").build();
  }
}
