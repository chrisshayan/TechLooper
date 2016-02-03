package com.techlooper.mapper;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;

/**
 * Created by phuonghqh on 2/1/16.
 */
@Mapper
@Repository
public interface ChallengeRegistrantMapper {
  ChallengeRegistrantEntity fromDto(ChallengeRegistrantDto dto);
}
