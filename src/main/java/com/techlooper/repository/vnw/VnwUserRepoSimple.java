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

    public VnwUser findByUsernameIgnoreCaseAndUserPassAndRoleName(String username, String userPass, RoleName roleName) {
        return userThuHoang();
    }

    private static VnwUser userThuHoang() {
        return VnwUser.VnwUserBuilder.vnwUser()
                .withRoleName(RoleName.EMPLOYER).withUsername("thu.hoang@navigosgroup.com").withEmail("thu.hoang@navigosgroup.com").build();
    }
}
