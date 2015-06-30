package com.techlooper.repository.vnw;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * Created by phuonghqh on 6/25/15.
 */

@NoRepositoryBean
public interface ReadOnlyRepository<T, ID extends Serializable> extends Repository<T, ID> {
  T findOne(ID id);
  Iterable<T> findAll();
}
