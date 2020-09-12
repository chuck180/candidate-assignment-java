package ch.aaap.assignment.raw;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CSVPostalCommunity {

  private String zipCode;
  private String zipCodeAddition;
  private String name;
  private String cantonCode;
  private String politicalCommunityNumber;
  private String politicalCommunityShortName;
}
