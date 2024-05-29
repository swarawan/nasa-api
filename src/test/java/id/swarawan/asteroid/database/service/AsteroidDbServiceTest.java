package id.swarawan.asteroid.database.service;

import id.swarawan.asteroid.database.repository.AsteroidDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AsteroidDbServiceTest {

    @Mock
    private AsteroidDataRepository asteroidDataRepository;

    @Mock
    private CloseApproachDbService closeApproachDbService;

    @Mock
    private SentryDbService sentryDbService;

    @Mock
    private OrbitDataDbService orbitDataDbService;

    @InjectMocks
    private AsteroidDbService asteroidDbService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void isExistByDate_dataExist_returnTrue() {
        Mockito.when(asteroidDataRepository.existsByDate(Mockito.anyString())).thenReturn(1L);

        boolean exist = asteroidDbService.isExistByDate(LocalDate.now());
        Assertions.assertTrue(exist);
    }

    @Test
    public void isExistByDate_dataNotExist_returnFalse() {
        Mockito.when(asteroidDataRepository.existsByDate(Mockito.anyString())).thenReturn(0L);

        boolean exist = asteroidDbService.isExistByDate(LocalDate.now());
        Assertions.assertFalse(exist);
    }

}