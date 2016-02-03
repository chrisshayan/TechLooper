package com.techlooper.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Created by phuonghqh on 1/29/16.
 */
@Data
@Builder
public class JoiningRegistrantDto {
  public enum Reason {UNMATCH_PASSCODE, INVALID_FBEMAIL, INVALID_INTERNAL_EMAIL, SINGLE_ACCOUNT}

  private Long countRegistrants;
  private boolean succeedJoin = false;
  private Reason reason;
}
