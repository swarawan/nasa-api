package id.swarawan.asteroid.database.service;

import id.swarawan.asteroid.config.utility.AppUtils;
import id.swarawan.asteroid.database.entity.OrbitTable;
import id.swarawan.asteroid.database.repository.OrbitDataRepository;
import id.swarawan.asteroid.model.api.data.OrbitalApiData;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrbitDataDbService {

    private final OrbitDataRepository orbitDataRepository;

    @Autowired
    public OrbitDataDbService(OrbitDataRepository orbitDataRepository) {
        this.orbitDataRepository = orbitDataRepository;
    }

    @Transactional
    public OrbitTable save(String referenceId, OrbitalApiData data) {
        OrbitTable orbitTable = OrbitTable.builder()
                .referenceId(referenceId)
                .orbitId(data.getOrbitId())
                .orbitDeterminationDate(data.getOrbitDeterminationDate())
                .firstObservationDate(data.getFirstObservationDate())
                .lastObservationDate(data.getLastObservationDate())
                .dataArcInDays(data.getDataArcInDays())
                .observationsUsed(data.getObservationsUsed())
                .orbitUncertainty(AppUtils.toDouble(data.getOrbitUncertainty(), 0.0))
                .minimum_orbitIntersection(AppUtils.toDouble(data.getMinimum_orbitIntersection(), 0.0))
                .jupiterTisserandInvariant(AppUtils.toDouble(data.getJupiterTisserandInvariant(), 0.0))
                .epochOsculation(AppUtils.toDouble(data.getEpochOsculation(), 0.0))
                .eccentricity(AppUtils.toDouble(data.getEccentricity(), 0.0))
                .semiMajorAxis(AppUtils.toDouble(data.getSemiMajorAxis(), 0.0))
                .inclination(AppUtils.toDouble(data.getInclination(), 0.0))
                .ascendingNodeLongitude(AppUtils.toDouble(data.getAscendingNodeLongitude(), 0.0))
                .orbitalPeriod(AppUtils.toDouble(data.getOrbitalPeriod(), 0.0))
                .perihelionDistance(AppUtils.toDouble(data.getPerihelionDistance(), 0.0))
                .perihelionArgument(AppUtils.toDouble(data.getPerihelionArgument(), 0.0))
                .perihelionTime(AppUtils.toDouble(data.getPerihelionTime(), 0.0))
                .aphelionDistance(AppUtils.toDouble(data.getAphelionDistance(), 0.0))
                .meanAnomaly(AppUtils.toDouble(data.getMeanAnomaly(), 0.0))
                .meanMotion(AppUtils.toDouble(data.getMeanMotion(), 0.0))
                .equinox(data.getEquinox())
                .orbitClassType(data.getOrbitClass().getOrbitClassType())
                .orbitClassDescription(data.getOrbitClass().getOrbitClassDescription())
                .orbitClassRange(data.getOrbitClass().getOrbitClassRange())
                .build();
        orbitDataRepository.save(orbitTable);

        return orbitTable;
    }

    public OrbitTable findOrbitTable(String referenceId) {
        return orbitDataRepository.findByReference(referenceId);
    }
}
