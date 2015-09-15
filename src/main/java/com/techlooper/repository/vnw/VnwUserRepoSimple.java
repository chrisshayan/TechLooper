package com.techlooper.repository.vnw;

import com.techlooper.entity.vnw.RoleName;
import com.techlooper.entity.vnw.VnwUser;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by phuonghqh on 6/25/15.
 */
@Service
@Profile("local")
public class VnwUserRepoSimple implements VnwUserRepo {
  public VnwUser findByUsernameIgnoreCaseAndUserPassAndRoleName(String username, String userPass, RoleName roleName) {
    return userThuHoang();
  }

  public VnwUser findByUsernameIgnoreCase(String username) {
    return userThuHoang();
  }

  public VnwUser findOne(Long aLong) {
    return null;
  }

  public Iterable<VnwUser> findAll() {
    return null;
  }

  private static VnwUser userThuHoang() {
    return VnwUser.VnwUserBuilder.vnwUser()
      .withRoleName(RoleName.EMPLOYER).withUsername("thu.hoang@navigosgroup.com").withEmail("thu.hoang@navigosgroup.com").build();
  }
}
