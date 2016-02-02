package com.techlooper.mapper;

import com.techlooper.dto.DraftRegistrantDto;
import com.techlooper.entity.DraftRegistrantEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by phuonghqh on 2/2/16.
 */
@Mapper
@Repository
public interface DraftRegistrantMapper {
  DraftRegistrantDto fromEntity(DraftRegistrantEntity entity);
}
