package com.techlooper.repository.vnw;

import com.techlooper.entity.vnw.RoleName;
import com.techlooper.entity.vnw.VnwUser;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by phuonghqh on 6/25/15.
 */
@Service
@Profile("default")
public class VnwUserRepoSimple implements VnwUserRepo {
  @Override
  public VnwUser findByUsernameIgnoreCaseAndUserPassAndRoleName(String username, String userPass, RoleName roleName) {
    return VnwUser.VnwUserBuilder.vnwUser().withRoleName(RoleName.EMPLOYER).withUsername("sample").build();
  }

  @Override
  public VnwUser findByUsernameIgnoreCase(String username) {
    return VnwUser.VnwUserBuilder.vnwUser().withRoleName(RoleName.EMPLOYER).withUsername("sample").build();
  }

  @Override
  public VnwUser findOne(Long aLong) {
    return null;
  }

  @Override
  public Iterable<VnwUser> findAll() {
    return null;
  }
}
