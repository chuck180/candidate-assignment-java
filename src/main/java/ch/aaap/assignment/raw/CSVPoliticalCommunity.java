package ch.aaap.assignment.raw;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CSVPoliticalCommunity {

  private String number;
  private String name;
  private String shortName;
  private String cantonCode;
  private String cantonName;
  private String districtNumber;
  private String districtName;
  private LocalDate lastUpdate;
}
