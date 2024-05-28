package id.swarawan.asteroid.service.neofeed;

import id.swarawan.asteroid.config.exceptions.BadRequestException;
import id.swarawan.asteroid.config.utility.AppUtils;
import id.swarawan.asteroid.model.api.NeoFeedApiResponse;
import id.swarawan.asteroid.model.api.data.AsteroidObjectData;
import id.swarawan.asteroid.model.api.data.item.EstimatedDiameterItem;
import id.swarawan.asteroid.model.response.NeoSentryResponse;
import id.swarawan.asteroid.model.response.item.CloseApproachItem;
import id.swarawan.asteroid.model.response.item.DiameterItem;
import id.swarawan.asteroid.model.response.item.NeoFeedItem;
import id.swarawan.asteroid.model.response.NeoFeedResponse;
import id.swarawan.asteroid.service.nasa.NasaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NeoFeedService {

    private final NasaApiService nasaApiService;
    private final NeoSentryService neoSentryService;

    @Autowired
    public NeoFeedService(NasaApiService nasaApiService, NeoSentryService neoSentryService) {
        this.nasaApiService = nasaApiService;
        this.neoSentryService = neoSentryService;
    }

    public List<NeoFeedResponse> getNeoFeed(LocalDate startDate, LocalDate endDate) throws BadRequestException {
        List<NeoFeedResponse> result = new ArrayList<>();

        validateRequest(startDate, endDate);

        NeoFeedApiResponse neoFeedApiResponse = nasaApiService.getNeoFeedApi(startDate, endDate);
        neoFeedApiResponse.getNearEarthObjects().forEach((date, data) -> {
            List<NeoFeedItem> feedItems = collectFeeds(data);
            NeoFeedResponse item = NeoFeedResponse.builder()
                    .date(date)
                    .asteroids(feedItems)
                    .build();
            result.add(item);
        });

        return result;
    }

    private List<NeoFeedItem> collectFeeds(List<AsteroidObjectData> asteroids) {
        return asteroids.stream().map(data -> {

            NeoFeedItem.NeoFeedItemBuilder builder = NeoFeedItem.builder()
                    .id(data.getReferenceId())
                    .name(data.getName())
                    .jplUrl(data.getNasaJplUrl())
                    .absoluteMagnitude(data.getAbsoluteMagnitude())
                    .isHazardAsteroid(data.getIsHazardousAsteroid())
                    .isSentryObject(data.getIsSentryObject());

            if (!Objects.isNull(data.getSentryData())) {
                builder.sentryData(neoSentryService.getNeoSentry(data.getReferenceId()));
            }

            if (!Objects.isNull(data.getEstimatedDiameter())) {
                EstimatedDiameterItem estimatedDiameterKm = data.getEstimatedDiameter().getKilometers();
                builder.estimatedDiameterKm(DiameterItem.builder()
                        .diameterMin(estimatedDiameterKm.getMin())
                        .diameterMax(estimatedDiameterKm.getMax())
                        .build());

                EstimatedDiameterItem estimatedDiameterMiles = data.getEstimatedDiameter().getMiles();
                builder.estimatedDiameterMiles(DiameterItem.builder()
                        .diameterMin(estimatedDiameterMiles.getMin())
                        .diameterMax(estimatedDiameterMiles.getMax())
                        .build());

                EstimatedDiameterItem estimatedDiameterFeet = data.getEstimatedDiameter().getKilometers();
                builder.estimatedDiameterFeet(DiameterItem.builder()
                        .diameterMin(estimatedDiameterFeet.getMin())
                        .diameterMax(estimatedDiameterFeet.getMax())
                        .build());
            }

            if (!Objects.isNull(data.getClosestApproaches())) {
                List<CloseApproachItem> closeApproachItems = data.getClosestApproaches()
                        .stream().map(closeApproach -> CloseApproachItem.builder()
                                .approachDate(closeApproach.getApproachDateFull())
                                .orbitBody(closeApproach.getOrbitBody())
                                .velocityKps(AppUtils.toDouble(closeApproach.getRelativeVelocity().getKilometerPerSecond(), 0.0))
                                .velocityKph(AppUtils.toDouble(closeApproach.getRelativeVelocity().getKilometerPerHour(), 0.0))
                                .velocityMph(AppUtils.toDouble(closeApproach.getRelativeVelocity().getMilesPerSecond(), 0.0))
                                .distanceAstronomical(AppUtils.toDouble(closeApproach.getMissDistance().getAstronomical(), 0.0))
                                .distanceLunar(AppUtils.toDouble(closeApproach.getMissDistance().getLunar(), 0.0))
                                .distanceKilometers(AppUtils.toDouble(closeApproach.getMissDistance().getKilometers(), 0.0))
                                .distanceMiles(AppUtils.toDouble(closeApproach.getMissDistance().getMiles(), 0.0))
                                .build())
                        .toList();
                builder.closeApproaches(closeApproachItems);
            }

            return builder.build();
        }).toList();
    }

    private void validateRequest(LocalDate startDate, LocalDate endDate) throws BadRequestException {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            throw new BadRequestException("Start / end date is required");
        } else if (startDate.isAfter(endDate)) {
            throw new BadRequestException("Start date cannot more than end date");
        }
    }
}