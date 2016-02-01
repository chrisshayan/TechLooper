package com.techlooper.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by phuonghqh on 1/29/16.
 */
@Getter
@Setter
@Builder
public class JoiningRegistrantDto {

  private Long countRegistrants;

  private boolean succeedJoin = false;
}
