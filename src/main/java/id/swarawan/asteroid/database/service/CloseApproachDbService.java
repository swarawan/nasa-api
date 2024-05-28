package id.swarawan.asteroid.database.service;

import id.swarawan.asteroid.database.entity.CloseApproachTable;
import id.swarawan.asteroid.database.repository.CloseApproachRepository;
import id.swarawan.asteroid.model.response.item.CloseApproachItem;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CloseApproachDbService {

    private CloseApproachRepository closeApproachRepository;

    @Autowired
    public CloseApproachDbService(CloseApproachRepository closeApproachRepository) {
        this.closeApproachRepository = closeApproachRepository;
    }

    @Transactional
    public void save(List<CloseApproachItem> closeApproaches) {
        List<CloseApproachTable> closeApproachData = closeApproaches.stream().map(data ->
                CloseApproachTable.builder()
                        .approachDate(data.getApproachDate())
                        .approachDateFull(data.getApproachDateFull())
                        .velocityKps(data.getVelocityKps())
                        .velocityKph(data.getVelocityKph())
                        .velocityMph(data.getVelocityMph())
                        .distanceAstronomical(data.getDistanceAstronomical())
                        .distanceLunar(data.getDistanceLunar())
                        .distanceKilometers(data.getDistanceKilometers())
                        .distanceMiles(data.getDistanceMiles())
                        .orbitingBody(data.getOrbitBody())
                        .build()).toList();
        closeApproachRepository.saveAll(closeApproachData);
    }
}
