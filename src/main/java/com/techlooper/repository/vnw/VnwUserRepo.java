package com.techlooper.repository.vnw;

import com.techlooper.entity.vnw.RoleName;
import com.techlooper.entity.vnw.VnwUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by phuonghqh on 6/25/15.
 */
public interface VnwUserRepo extends ReadOnlyRepository<VnwUser, Long> {

  VnwUser findByUsernameIgnoreCaseAndUserPassAndRoleName(String username, String userPass, RoleName roleName);
}
