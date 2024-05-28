package id.swarawan.asteroid.config.utility;

public class AppUtils {

    public static Integer toInt(String s, Integer defaultValue) {
        try {
            return s == null ? defaultValue : Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Double toDouble(String s, Double defaultValue) {
        try {
            return s == null ? defaultValue : Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
