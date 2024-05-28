package id.swarawan.asteroid.service.neofeed;

import id.swarawan.asteroid.config.exceptions.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NeoFeedServiceTest {

    @InjectMocks
    private NeoFeedService neoFeedService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getNeoFeed_missingStartDate_returnException() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                neoFeedService.getNeoFeed(null, LocalDate.now()));

        Assertions.assertEquals("Start / end date cannot be null", exception.getMessage());
    }

    @Test
    public void getNeoFeed_missingEndDate_returnException() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                neoFeedService.getNeoFeed(LocalDate.now(), null));

        Assertions.assertEquals("Start / end date cannot be null", exception.getMessage());
    }

    @Test
    public void getNeoFeed_startDateIsAfterEndDate_returnException() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                neoFeedService.getNeoFeed(LocalDate.now().plusDays(2), LocalDate.now()));

        Assertions.assertEquals("Start date cannot more than end date", exception.getMessage());
    }

}