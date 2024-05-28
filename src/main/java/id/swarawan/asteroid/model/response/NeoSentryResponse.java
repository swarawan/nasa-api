package id.swarawan.asteroid.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NeoSentryResponse {

    @JsonProperty(value = "spkId")
    private String spkId;

    @JsonProperty(value = "designation")
    private String designation;

    @JsonProperty(value = "sentryId")
    private String sentryId;

    @JsonProperty(value = "fullname")
    private String fullName;

    @JsonProperty(value = "year_range_min")
    private Integer yearRangeMin;

    @JsonProperty(value = "year_range_max")
    private Integer yearRangeMax;

    @JsonProperty(value = "potential_impacts")
    private Integer potentialImpacts;

    @JsonProperty(value = "impact_probability")
    private String impactProbability;

    @JsonProperty(value = "v_infinity")
    private Double infinity;

    @JsonProperty(value = "absolute_magnitude")
    private Double absoluteMagnitude;

    @JsonProperty(value = "estimated_diameter")
    private Double estimatedDiameter;

    @JsonProperty(value = "palermo_scale_ave")
    private Double palermoScaleAve;

    @JsonProperty(value = "Palermo_scale_max")
    private Double palermoScaleMax;

    @JsonProperty(value = "torino_scale")
    private Integer torinoScale;

    @JsonProperty(value = "last_obs")
    private LocalDate lastObs;

    @JsonProperty(value = "last_obs_jd")
    private Double lastObsJd;

    @JsonProperty(value = "is_active_sentry_object")
    private Boolean isActiveSentryObject;

    @JsonProperty(value = "url_impact_details")
    private String urlImpactDetails;

    @JsonProperty(value = "url_orbital_element_details")
    private String urlOrbitalElementDetails;
}
