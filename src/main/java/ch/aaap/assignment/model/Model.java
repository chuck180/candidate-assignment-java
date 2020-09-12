package ch.aaap.assignment.model;

import java.util.Set;

import lombok.Value;
import lombok.Builder;

@Value
@Builder
public class Model {

  Set<PoliticalCommunity> politicalCommunities;
  Set<PostalCommunity> postalCommunities;
  Set<Canton> cantons;
  Set<District> districts;
}
