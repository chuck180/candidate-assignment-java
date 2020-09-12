package ch.aaap.assignment;

import ch.aaap.assignment.model.*;
import ch.aaap.assignment.raw.CSVPoliticalCommunity;
import ch.aaap.assignment.raw.CSVPostalCommunity;
import ch.aaap.assignment.raw.CSVUtil;
import java.time.LocalDate;
import java.util.*;
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
   * Gets the Amount of {@link PoliticalCommunity PoliticalCommunities} in a given {@link Canton}
   *
   * @param cantonCode of a canton (e.g. ZH)
   * @return amount of {@link PoliticalCommunity PoliticalCommunities} in a given {@link Canton}
   */
  public long getAmountOfPoliticalCommunitiesInCanton(String cantonCode) {
    Optional<Canton> canton =
        model.getCantons().stream().filter(c -> c.getCode().equals(cantonCode)).findFirst();
    if (canton.isEmpty()) {
      throw new IllegalArgumentException("Invalid Canton Code");
    }
    return model.getPoliticalCommunities().stream()
        .filter(pc -> pc.getCanton().equals(canton.get()))
        .count();
  }

  /**
   * Gets the Amount of {@link District}s in a given {@link Canton}
   *
   * @param cantonCode of a canton (e.g. ZH)
   * @return amount of {@link District}s in a given {@link Canton}
   */
  public long getAmountOfDistrictsInCanton(String cantonCode) {
    Optional<Canton> canton =
        model.getCantons().stream().filter(c -> c.getCode().equals(cantonCode)).findFirst();
    if (canton.isEmpty()) {
      throw new IllegalArgumentException("Invalid Canton Code");
    }
    return model.getPoliticalCommunities().stream()
        .filter(pl -> pl.getCanton().equals(canton.get()))
        .map(PoliticalCommunity::getDistrict)
        .distinct()
        .count();
  }

  /**
   * Gets the Amount of {@link PoliticalCommunity PoliticalCommunities} in a {@link District}
   *
   * @param districtNumber of a district (e.g. 101)
   * @return amount of {@link District}s in given canton
   */
  public long getAmountOfPoliticalCommunitiesInDistrict(String districtNumber) {
    Optional<District> district =
        model.getDistricts().stream().filter(d -> d.getNumber().equals(districtNumber)).findFirst();
    if (district.isEmpty()) {
      throw new IllegalArgumentException("Invalid District Number");
    }
    return model.getPoliticalCommunities().stream()
        .filter(pc -> pc.getDistrict().equals(district.get()))
        .count();
  }

  /**
   * Gets the {@link District}s for a {@param zipCode}
   *
   * @param zipCode 4 digit zip code
   * @return {@link District} that belongs to specified {@param zipCode}
   */
  public Set<String> getDistrictsForZipCode(String zipCode) {
    return model.getPostalCommunities().stream()
        .filter(po -> po.getZipCode().equals(zipCode))
        .map(PostalCommunity::getPoliticalCommunities)
        .flatMap(Collection::stream)
        .map(pc -> pc.getDistrict().getName())
        .collect(Collectors.toUnmodifiableSet());
  }

  /**
   * Gets the lastUpdate Of {@link PoliticalCommunity PoliticalCommunities} by a given {@param
   * postalCommunityName}
   *
   * @param postalCommunityName name
   * @return lastUpdate of {@link PoliticalCommunity PoliticalCommunities} by a given {@param
   *     postalCommunityName}
   */
  public LocalDate getLastUpdateOfPoliticalCommunityByPostalCommunityName(
      String postalCommunityName) {
    return model.getPostalCommunities().stream()
        .filter(po -> po.getName().equals(postalCommunityName))
        .map(PostalCommunity::getPoliticalCommunities)
        .flatMap(Collection::stream)
        .map(PoliticalCommunity::getLastUpdate)
        .findFirst()
        .orElse(null);
  }

  /**
   * Gets the Amount of {@link Canton}s
   *
   * @return amount of {@link Canton}s
   */
  public long getAmountOfCantons() {
    return model.getCantons().size();
  }

  /**
   * Gets the Amount of {@link PoliticalCommunity PoliticalCommunities} without {@link
   * PostalCommunity PostalCommunities}
   *
   * @return amount of {@link PoliticalCommunity PoliticalCommunities} without {@link
   *     PostalCommunity PostalCommunities}
   */
  public long getAmountOfPoliticalCommunityWithoutPostalCommunities() {
    return model.getPoliticalCommunities().stream()
        .filter(
            pc ->
                !model.getPostalCommunities().stream()
                    .map(PostalCommunity::getPoliticalCommunities)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toUnmodifiableSet())
                    .contains(pc))
        .count();
  }
}
