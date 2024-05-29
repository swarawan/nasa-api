package id.swarawan.asteroid.service.neolookup;

import id.swarawan.asteroid.config.exceptions.BadRequestException;
import id.swarawan.asteroid.config.exceptions.NotFoundException;
import id.swarawan.asteroid.config.utility.AppUtils;
import id.swarawan.asteroid.database.entity.AsteroidTable;
import id.swarawan.asteroid.database.entity.CloseApproachTable;
import id.swarawan.asteroid.database.entity.OrbitTable;
import id.swarawan.asteroid.database.service.AsteroidDbService;
import id.swarawan.asteroid.database.service.CloseApproachDbService;
import id.swarawan.asteroid.database.service.OrbitDataDbService;
import id.swarawan.asteroid.model.api.NeoLookupApiResponse;
import id.swarawan.asteroid.model.api.data.CloseApproachApiData;
import id.swarawan.asteroid.model.response.NeoLookupResponse;
import id.swarawan.asteroid.model.response.item.CloseApproachItem;
import id.swarawan.asteroid.model.response.item.DiameterItem;
import id.swarawan.asteroid.model.response.item.OrbitalItem;
import id.swarawan.asteroid.service.nasa.NasaApiService;
import id.swarawan.asteroid.service.neofeed.NeoSentryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NeoLookupService {

    private final NasaApiService nasaApiService;
    private final NeoSentryService neoSentryService;
    private final AsteroidDbService asteroidDbService;
    private final CloseApproachDbService closeApproachDbService;
    private final OrbitDataDbService orbitDataDbService;

    @Autowired
    public NeoLookupService(NasaApiService nasaApiService,
                            NeoSentryService neoSentryService,
                            AsteroidDbService asteroidDbService,
                            CloseApproachDbService closeApproachDbService,
                            OrbitDataDbService orbitDataDbService) {
        this.nasaApiService = nasaApiService;
        this.neoSentryService = neoSentryService;
        this.asteroidDbService = asteroidDbService;
        this.closeApproachDbService = closeApproachDbService;
        this.orbitDataDbService = orbitDataDbService;
    }

    public NeoLookupResponse getNeoLookup(String referenceId) {
        validate(referenceId);

        NeoLookupApiResponse apiResponse = nasaApiService.getNeoLookUp(referenceId)
                .orElseThrow(() -> new NotFoundException("Data not found"));

        AsteroidTable asteroidTable = asteroidDbService.findByReferenceId(referenceId);
        if (Objects.isNull(asteroidTable)) {
            asteroidTable = asteroidDbService.save(apiResponse);
        }

        List<CloseApproachApiData> closeApproachApiData = apiResponse.getClosestApproaches();
        List<CloseApproachTable> closeApproachTables = closeApproachDbService.findByReferenceId(referenceId);
        if (closeApproachTables.size() != closeApproachApiData.size()) {
            closeApproachDbService.save(referenceId, closeApproachApiData);
            closeApproachTables = closeApproachDbService.findByReferenceId(referenceId);
        }

        OrbitTable orbitTable = orbitDataDbService.findOrbitTable(referenceId);
        if (Objects.isNull(orbitTable)) {
            orbitDataDbService.save(referenceId, apiResponse.getOrbitalData());
        }

        return generateResponse(asteroidTable, closeApproachTables, orbitTable);
    }

    public NeoLookupResponse generateResponse(AsteroidTable asteroidTable, List<CloseApproachTable> closeApproachTables) {
        return generateResponse(asteroidTable, closeApproachTables, null);
    }

    public NeoLookupResponse generateResponse(AsteroidTable asteroidTable,
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
            builder.sentryData(neoSentryService.getNeoSentry(asteroidTable.getReferenceId()));
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

    public void validate(String referenceId) {
        if (Objects.isNull(referenceId)) {
            throw new BadRequestException("Reference ID is required");
        }
    }

}
