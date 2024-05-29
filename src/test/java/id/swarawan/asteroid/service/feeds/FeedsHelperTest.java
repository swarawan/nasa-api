package id.swarawan.asteroid.service.feeds;

import id.swarawan.asteroid.database.entity.AsteroidTable;
import id.swarawan.asteroid.database.entity.CloseApproachTable;
import id.swarawan.asteroid.database.entity.OrbitTable;
import id.swarawan.asteroid.model.response.SentryResponse;
import id.swarawan.asteroid.model.response.SingleAsteroidResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

class FeedsHelperTest {

    @Mock
    private SentryService sentryService;

    @InjectMocks
    private FeedsHelper feedsHelper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void collectDates_givenStartAndEndDate_returnSetOfDates() {
        LocalDate date1 = LocalDate.of(2019, 1, 1);
        LocalDate date2 = LocalDate.of(2019, 1, 10);

        Set<LocalDate> dates = feedsHelper.collectDates(date1, date2);
        Assertions.assertEquals(dates.size(), 10);
    }

    @Test
    public void collectDates_givenStartIsAfterEnd_returnEmptySet() {
        LocalDate date1 = LocalDate.of(2019, 1, 10);
        LocalDate date2 = LocalDate.of(2019, 1, 1);

        Set<LocalDate> dates = feedsHelper.collectDates(date1, date2);
        Assertions.assertEquals(dates.size(), 0);
    }

    @Test
    public void generateFeedResponse_givenData_returnResponse() {
        SentryResponse sentryResponse = SentryResponse.builder().spkId("001").build();
        AsteroidTable asteroidTable = AsteroidTable.builder().referenceId("001").isSentryObject(true).build();
        OrbitTable orbitTable = OrbitTable.builder().referenceId("001").build();
        List<CloseApproachTable> closeApproachTables = Collections.singletonList(CloseApproachTable.builder()
                .referenceId("001").build());

        Mockito.when(sentryService.getNeoSentry(Mockito.anyString())).thenReturn(sentryResponse);

        SingleAsteroidResponse actualResponse = feedsHelper.generateFeedResponse(asteroidTable, closeApproachTables, orbitTable);

        Assertions.assertEquals(actualResponse.getId(), "001");
        Assertions.assertEquals(actualResponse.getSentryData().getSpkId(), "001");
    }
}