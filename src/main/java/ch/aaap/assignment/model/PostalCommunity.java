package ch.aaap.assignment.model;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PostalCommunity {

  String zipCode;
  String zipCodeAddition;
  String name;
  List<PoliticalCommunity> politicalCommunities;
}
