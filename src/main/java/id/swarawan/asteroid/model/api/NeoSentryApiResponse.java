package id.swarawan.asteroid.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NeoSentryApiResponse {

    @JsonProperty(value = "spkId")
    private String spkId;

    @JsonProperty(value = "designation")
    private String designation;

    @JsonProperty(value = "sentryId")
    private String sentryId;

    @JsonProperty(value = "fullname")
    private String fullName;

    @JsonProperty(value = "year_range_min")
    private String yearRangeMin;

    @JsonProperty(value = "year_range_max")
    private String yearRangeMax;

    @JsonProperty(value = "potential_impacts")
    private String potentialImpacts;

    @JsonProperty(value = "impact_probability")
    private String impactProbability;

    @JsonProperty(value = "v_infinity")
    private String infinity;

    @JsonProperty(value = "absolute_magnitude")
    private String absoluteMagnitude;

    @JsonProperty(value = "estimated_diameter")
    private String estimatedDiameter;

    @JsonProperty(value = "palermo_scale_ave")
    private String palermoScaleAve;

    @JsonProperty(value = "Palermo_scale_max")
    private String palermoScaleMax;

    @JsonProperty(value = "torino_scale")
    private String torinoScale;

    @JsonProperty(value = "last_obs")
    private LocalDate lastObs;

    @JsonProperty(value = "last_obs_jd")
    private String lastObsJd;

    @JsonProperty(value = "is_active_sentry_object")
    private String isActiveSentryObject;

    @JsonProperty(value = "url_impact_details")
    private String urlImpactDetails;

    @JsonProperty(value = "url_orbital_element_details")
    private String urlOrbitalElementDetails;

    @JsonProperty(value = "average_lunar_distance")
    private Double averageLunarDistance;
}
