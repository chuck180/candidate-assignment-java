package ch.aaap.assignment.model;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PoliticalCommunity {

  String number;
  String name;
  String shortName;
  LocalDate lastUpdate;
  Canton canton;
  District district;
}
