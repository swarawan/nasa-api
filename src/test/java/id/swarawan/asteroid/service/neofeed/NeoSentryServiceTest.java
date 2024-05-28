package id.swarawan.asteroid.service.neofeed;

import id.swarawan.asteroid.config.exceptions.BadRequestException;
import id.swarawan.asteroid.config.exceptions.NotFoundException;
import id.swarawan.asteroid.model.api.NeoSentryApiResponse;
import id.swarawan.asteroid.model.response.NeoSentryResponse;
import id.swarawan.asteroid.service.nasa.NasaApiService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NeoSentryServiceTest {

    @Mock
    private NasaApiService nasaApiService;

    @InjectMocks
    private NeoSentryService neoSentryService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getNeoSentry_nullReferenceId_throwException() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                neoSentryService.getNeoSentry(null));
        Assertions.assertEquals(exception.getMessage(), "Reference ID is required");
    }

    @Test
    public void getNeoSentry_emptyReferenceId_throwException() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                neoSentryService.getNeoSentry(""));
        Assertions.assertEquals(exception.getMessage(), "Reference ID is required");
    }

    @Test
    public void getNeoSentry_apiReturnNull_throwException() {
        Mockito.when(nasaApiService.getNeoSentry(Mockito.anyString())).thenReturn(null);
        NotFoundException actual = Assertions.assertThrows(NotFoundException.class, () ->
                neoSentryService.getNeoSentry("abcd1234"));

        Assertions.assertEquals(actual.getMessage(), "Data not found");
    }

    @Test
    public void getNeoSentry_success_returnData() {
        NeoSentryApiResponse apiResponse = NeoSentryApiResponse.builder()
                .spkId("001")
                .fullName("OBS")
                .build();

        Mockito.when(nasaApiService.getNeoSentry(Mockito.anyString())).thenReturn(apiResponse);

        NeoSentryResponse actual = neoSentryService.getNeoSentry("abcd1234");

        Assertions.assertEquals(actual.getSpkId(), apiResponse.getSpkId());
        Assertions.assertEquals(actual.getFullName(), apiResponse.getFullName());
    }
}