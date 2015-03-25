package com.techlooper.repository.couchbase;

import com.techlooper.entity.UserEntity;
import com.techlooper.entity.UserRegistration;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by phuonghqh on 3/25/15.
 */
@Repository
public interface UserRegistrationRepository extends CouchbaseRepository<UserRegistration, String> {
}
