package id.swarawan.asteroid.service.neofeed;

import id.swarawan.asteroid.config.exceptions.BadRequestException;
import id.swarawan.asteroid.model.response.NeoFeedResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class NeoFeedService {

    public NeoFeedResponse getNeoFeed(LocalDate startDate, LocalDate endDate) throws BadRequestException {
        validateRequest(startDate, endDate);
        return null;
    }

    private void validateRequest(LocalDate startDate, LocalDate endDate) throws BadRequestException {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            throw new BadRequestException("Start / end date cannot be null");
        } else if(startDate.isAfter(endDate)) {
            throw new BadRequestException("Start date cannot more than end date");
        }
    }
}
