package ch.aaap.assignment;

import ch.aaap.assignment.model.*;
import ch.aaap.assignment.raw.CSVPoliticalCommunity;
import ch.aaap.assignment.raw.CSVPostalCommunity;
import ch.aaap.assignment.raw.CSVUtil;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Application {

  private Model model = null;

  public Application() {
    initModel();
  }

  public static void main(String[] args) {
    new Application();
  }

  /** Reads the CSVs and initializes a in memory model */
  private void initModel() {
    Set<CSVPoliticalCommunity> politicalCommunities = CSVUtil.getPoliticalCommunities();
    Set<CSVPostalCommunity> postalCommunities = CSVUtil.getPostalCommunities();

    Set<District> districts = new HashSet<>();
    Set<Canton> cantons = new HashSet<>();
    Set<PoliticalCommunity> politicalCommunitiesSet = new HashSet<>();
    Set<PostalCommunity> postalCommunitiesSet = new HashSet<>();

    for (CSVPoliticalCommunity politicalCommunity : politicalCommunities) {
      cantons.add(
          Canton.builder()
              .name(politicalCommunity.getCantonName())
              .code(politicalCommunity.getCantonCode())
              .build());
      districts.add(
          District.builder()
              .name(politicalCommunity.getDistrictName())
              .number(politicalCommunity.getDistrictNumber())
              .build());
    }

    politicalCommunities.forEach(
        pc -> {
          politicalCommunitiesSet.add(
              PoliticalCommunity.builder()
                  .name(pc.getName())
                  .number(pc.getNumber())
                  .shortName(pc.getShortName())
                  .lastUpdate(pc.getLastUpdate())
                  .canton(
                      cantons.stream()
                          .filter(c -> c.getCode().equals(pc.getCantonCode()))
                          .findFirst()
                          .orElse(null))
                  .district(
                      districts.stream()
                          .filter(d -> d.getNumber().equals(pc.getDistrictNumber()))
                          .findFirst()
                          .orElse(null))
                  .build());
        });

    postalCommunities.stream()
        .collect(
            Collectors.groupingBy(
                po ->
                    new AbstractMap.SimpleImmutableEntry<>(
                        po.getZipCode(), po.getZipCodeAddition())))
        .forEach(
            (zipEntry, pcList) ->
                postalCommunitiesSet.add(
                    PostalCommunity.builder()
                        .name(pcList.get(0).getName())
                        .zipCode(pcList.get(0).getZipCode())
                        .zipCodeAddition(pcList.get(0).getZipCodeAddition())
                        .politicalCommunities(
                            politicalCommunitiesSet.stream()
                                .filter(
                                    pc ->
                                        pcList.stream()
                                            .map(CSVPostalCommunity::getPoliticalCommunityNumber)
                                            .collect(Collectors.toList())
                                            .contains(pc.getNumber()))
                                .collect(Collectors.toList()))
                        .build()));

    model =
        Model.builder()
            .cantons(cantons)
            .districts(districts)
            .politicalCommunities(politicalCommunitiesSet)
            .postalCommunities(postalCommunitiesSet)
            .build();
  }

  /**
   * Gets the {@link Model} and returns it
   *
   * @return {@link Model}
   */
  public Model getModel() {
    return model;
  }

  /**
   * @param cantonCode of a canton (e.g. ZH)
   * @return amount of political communities in given canton
   */
  public long getAmountOfPoliticalCommunitiesInCanton(String cantonCode) {
    // TODO implementation
    throw new RuntimeException("Not yet implemented");
  }

  /**
   * @param cantonCode of a canton (e.g. ZH)
   * @return amount of districts in given canton
   */
  public long getAmountOfDistrictsInCanton(String cantonCode) {
    // TODO implementation
    throw new RuntimeException("Not yet implemented");
  }

  /**
   * @param districtNumber of a district (e.g. 101)
   * @return amount of districts in given canton
   */
  public long getAmountOfPoliticalCommunitiesInDistrict(String districtNumber) {
    // TODO implementation
    throw new RuntimeException("Not yet implemented");
  }

  /**
   * @param zipCode 4 digit zip code
   * @return district that belongs to specified zip code
   */
  public Set<String> getDistrictsForZipCode(String zipCode) {
    // TODO implementation
    throw new RuntimeException("Not yet implemented");
  }

  /**
   * @param postalCommunityName name
   * @return lastUpdate of the political community by a given postal community name
   */
  public LocalDate getLastUpdateOfPoliticalCommunityByPostalCommunityName(
      String postalCommunityName) {
    // TODO implementation
    throw new RuntimeException("Not yet implemented");
  }

  /**
   * https://de.wikipedia.org/wiki/Kanton_(Schweiz)
   *
   * @return amount of canton
   */
  public long getAmountOfCantons() {
    // TODO implementation
    return model.getCantons().size();
  }

  /**
   * https://de.wikipedia.org/wiki/Kommunanz
   *
   * @return amount of political communities without postal communities
   */
  public long getAmountOfPoliticalCommunityWithoutPostalCommunities() {
    // TODO implementation
    throw new RuntimeException("Not yet implemented");
  }
}
