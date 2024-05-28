package id.swarawan.asteroid.service.neofeed;

import id.swarawan.asteroid.config.exceptions.BadRequestException;
import id.swarawan.asteroid.model.api.NeoFeedApiResponse;
import id.swarawan.asteroid.model.api.data.AsteroidObjectData;
import id.swarawan.asteroid.model.response.NeoFeedItem;
import id.swarawan.asteroid.model.response.NeoFeedResponse;
import id.swarawan.asteroid.service.nasa.NasaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class NeoFeedService {

    private NasaApiService nasaApiService;

    @Autowired
    public NeoFeedService(NasaApiService nasaApiService) {
        this.nasaApiService = nasaApiService;
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
        return asteroids.stream().map(data -> NeoFeedItem.builder()
                .id(data.getReferenceId())
                .name(data.getName())
                .build()).toList();
    }

    private void validateRequest(LocalDate startDate, LocalDate endDate) throws BadRequestException {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            throw new BadRequestException("Start / end date cannot be null");
        } else if (startDate.isAfter(endDate)) {
            throw new BadRequestException("Start date cannot more than end date");
        }
    }
}
