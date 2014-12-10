package com.techlooper.repository.couchbase;

import com.techlooper.model.UserEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

/**
 * Created by NguyenDangKhoa on 12/10/14.
 */
public interface UserRepository extends CouchbaseRepository<UserEntity, String> {

}
