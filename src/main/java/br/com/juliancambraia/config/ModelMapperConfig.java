package br.com.juliancambraia.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ModelMapperConfig {
    public <O, D> D parseObject(O origin, Class<D> destination) {
        return modelMapper().map(origin, destination);
    }

    public <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
        List<D> destinationObjects = new ArrayList<>();
        for (O o : origin) {
            destinationObjects.add(modelMapper().map(o, destination));
        }
        return destinationObjects;
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
