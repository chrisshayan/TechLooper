package com.techlooper.repository.couchbase;

import com.couchbase.client.protocol.views.Query;
import com.techlooper.entity.UserEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

/**
 * Created by NguyenDangKhoa on 12/10/14.
 */
public interface UserRepository extends CouchbaseRepository<UserEntity, String> {

    UserEntity findByEmail(Query query);
}
