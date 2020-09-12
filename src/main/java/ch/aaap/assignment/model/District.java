package ch.aaap.assignment.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class District {

  String number;
  String name;
}
