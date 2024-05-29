package id.swarawan.asteroid.service.feeds;

import id.swarawan.asteroid.database.entity.AsteroidTable;
import id.swarawan.asteroid.database.entity.CloseApproachTable;
import id.swarawan.asteroid.database.entity.OrbitTable;
import id.swarawan.asteroid.model.response.NeoLookupResponse;
import id.swarawan.asteroid.model.response.item.CloseApproachItem;
import id.swarawan.asteroid.model.response.item.DiameterItem;
import id.swarawan.asteroid.model.response.item.OrbitalItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class FeedsHelper {

    private final SentryService sentryService;

    @Autowired
    public FeedsHelper(SentryService sentryService) {
        this.sentryService = sentryService;
    }

    /**
     * Generate feed response by given data from database
     * <p>
     *
     * @param asteroidTable       data from specific Asteroid Table data
     * @param closeApproachTables collection of Close Approach Table data
     * @return a single feed data
     */
    public NeoLookupResponse generateFeedResponse(AsteroidTable asteroidTable, List<CloseApproachTable> closeApproachTables) {
        return generateFeedResponse(asteroidTable, closeApproachTables, null);
    }

    /**
     * Generate feed response by given data from database
     * <p>
     *
     * @param asteroidTable       data from specific Asteroid Table data
     * @param closeApproachTables collection of Close Approach Table data
     * @param orbitTable          data from Orbit Table for specific Asteroid
     * @return a single feed data
     */
    public NeoLookupResponse generateFeedResponse(AsteroidTable asteroidTable,
                                                  List<CloseApproachTable> closeApproachTables,
                                                  OrbitTable orbitTable) {
        NeoLookupResponse.NeoLookupResponseBuilder builder = NeoLookupResponse.builder()
                .id(asteroidTable.getReferenceId())
                .name(asteroidTable.getName())
                .jplUrl(asteroidTable.getNasaJplUrl())
                .absoluteMagnitude(asteroidTable.getAbsoluteMagnitude())
                .isHazardAsteroid(asteroidTable.getIsHazardPotential())
                .isSentryObject(asteroidTable.getIsSentryObject());

        if (asteroidTable.getIsSentryObject()) {
            builder.sentryData(sentryService.getNeoSentry(asteroidTable.getReferenceId()));
        }

        builder.estimatedDiameterKm(DiameterItem.builder()
                .diameterMin(asteroidTable.getDiameterMilesMin())
                .diameterMax(asteroidTable.getDiameterKmMax())
                .build());

        builder.estimatedDiameterMiles(DiameterItem.builder()
                .diameterMin(asteroidTable.getDiameterMilesMin())
                .diameterMax(asteroidTable.getDiameterMilesMax())
                .build());

        builder.estimatedDiameterFeet(DiameterItem.builder()
                .diameterMin(asteroidTable.getDiameterFeetMin())
                .diameterMax(asteroidTable.getDiameterFeetMax())
                .build());

        List<CloseApproachItem> closeApproachItems = closeApproachTables.stream().map(closeApproach -> CloseApproachItem.builder()
                .approachDate(closeApproach.getApproachDate())
                .approachDateFull(closeApproach.getApproachDateFull())
                .orbitBody(closeApproach.getOrbitingBody())
                .velocityKps(closeApproach.getVelocityKps())
                .velocityKph(closeApproach.getVelocityKph())
                .velocityMph(closeApproach.getVelocityMph())
                .distanceAstronomical(closeApproach.getDistanceAstronomical())
                .distanceLunar(closeApproach.getDistanceLunar())
                .distanceKilometers(closeApproach.getDistanceKilometers())
                .distanceMiles(closeApproach.getDistanceMiles())
                .build()).toList();
        builder.closeApproaches(closeApproachItems);

        if (!Objects.isNull(orbitTable)) {
            builder.orbitData(OrbitalItem.builder()
                    .orbitId(orbitTable.getOrbitId())
                    .orbitDeterminationDate(orbitTable.getOrbitDeterminationDate())
                    .firstObservationDate(orbitTable.getFirstObservationDate())
                    .lastObservationDate(orbitTable.getLastObservationDate())
                    .dataArcInDays(orbitTable.getDataArcInDays())
                    .observationsUsed(orbitTable.getObservationsUsed())
                    .orbitUncertainty(orbitTable.getOrbitUncertainty())
                    .minimum_orbitIntersection(orbitTable.getMinimum_orbitIntersection())
                    .jupiterTisserandInvariant(orbitTable.getJupiterTisserandInvariant())
                    .epochOsculation(orbitTable.getEpochOsculation())
                    .eccentricity(orbitTable.getEccentricity())
                    .semiMajorAxis(orbitTable.getSemiMajorAxis())
                    .inclination(orbitTable.getInclination())
                    .ascendingNodeLongitude(orbitTable.getAscendingNodeLongitude())
                    .orbitalPeriod(orbitTable.getOrbitalPeriod())
                    .perihelionDistance(orbitTable.getPerihelionDistance())
                    .perihelionArgument(orbitTable.getPerihelionArgument())
                    .perihelionTime(orbitTable.getPerihelionTime())
                    .aphelionDistance(orbitTable.getAphelionDistance())
                    .meanAnomaly(orbitTable.getMeanAnomaly())
                    .meanMotion(orbitTable.getMeanMotion())
                    .equinox(orbitTable.getEquinox())
                    .orbitClassType(orbitTable.getOrbitClassType())
                    .orbitClassDescription(orbitTable.getOrbitClassDescription())
                    .orbitClassRange(orbitTable.getOrbitClassRange())
                    .build());
        }

        return builder.build();
    }

    /**
     * Collect all dates based on start-date and end-date
     * <p>
     * This function will collect all dates in a range of date, avoid duplication by using Set instead of List
     *
     * @param startDate start of a range date
     * @param endDate   end of a range date
     * @return a sets of dates
     */
    public Set<LocalDate> collectDates(LocalDate startDate, LocalDate endDate) {
        Set<LocalDate> result = new HashSet<>();
        LocalDate tempDate = startDate;
        while (tempDate.isBefore(endDate) || tempDate.equals(endDate)) {
            result.add(tempDate);
            tempDate = tempDate.plusDays(1);
        }
        return result;
    }
}
