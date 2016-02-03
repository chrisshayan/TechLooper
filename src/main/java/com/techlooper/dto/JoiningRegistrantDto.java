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
  public enum Reason {UNMATCH_PASSCODE, INVALID_FBEMAIL, INVALID_INTERNAL_EMAIL, SINGLE_ACCOUNT}

  private Long countRegistrants;
  private boolean succeedJoin = false;
  private Reason reason;
}
