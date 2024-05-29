package id.swarawan.asteroid.config.converter;

import id.swarawan.asteroid.enums.Modes;
import org.springframework.core.convert.converter.Converter;

public class ModesEnumConverter implements Converter<String, Modes> {
    @Override
    public Modes convert(String source) {
        return Modes.valueOf(source.toUpperCase());
    }
}
