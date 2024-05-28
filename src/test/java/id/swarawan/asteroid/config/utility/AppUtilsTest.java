package id.swarawan.asteroid.config.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppUtilsTest {

    @Test
    public void toInt_success_returnValue() {
        Assertions.assertEquals(AppUtils.toInt("1", 0), 1);
        Assertions.assertEquals(AppUtils.toInt("1.1", 0), 0);
        Assertions.assertEquals(AppUtils.toInt("abcd", 0), 0);

        Assertions.assertEquals(AppUtils.toDouble("1.1", 0.0), 1.1);
        Assertions.assertEquals(AppUtils.toDouble("1", 0.0), 1.0);
        Assertions.assertEquals(AppUtils.toDouble("abcd", 0.0), 0.0);
    }

}