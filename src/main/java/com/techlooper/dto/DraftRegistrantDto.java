package com.techlooper.dto;

import com.techlooper.model.Language;
import lombok.Data;

/**
 * Created by phuonghqh on 2/2/16.
 */
@Data
public class DraftRegistrantDto {
  private Long registrantId;
  private String registrantEmail;
  private String registrantInternalEmail;
  private Long challengeId;
  private String registrantFirstName;
  private String registrantLastName;
  private Language lang;
}
