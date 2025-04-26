package com.challenge.Intuit.configuration;
import com.challenge.Intuit.dto.CustomerDto;
import com.challenge.Intuit.entity.Customer;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        /**
         *  Optional configuration for the mapping strategy.
         *  STANDARD: Flexible matching (e.g. firstName -> first_name)
         *  STRICT: Exact matching (e.g. firstName -> firstName)
         *  LOOSE: Very flexible matching
         */
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        /**
         * Enables the option to skip null values in the source (DTO).
         * Use  setSkipNullEnabled(true) or setSkipNulls(true) depending on the version.
         * This tells ModelMapper to ignore null values during the mapping process.
         */
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        Converter<String, String> emptyStringConverter = ctx ->
                ctx.getSource() == null ? "" : ctx.getSource();
        //Schee for customer mapper
        modelMapper.createTypeMap(CustomerDto.class, Customer.class)
                   .addMappings(mapper -> {
                       mapper.using(emptyStringConverter).map(CustomerDto::getNombre, Customer::setNombre);
                       mapper.using(emptyStringConverter).map(CustomerDto::getApellido, Customer::setApellido);
                       mapper.using(emptyStringConverter).map(CustomerDto::getCuit, Customer::setCuit);
                       mapper.using(emptyStringConverter).map(CustomerDto::getDomicilio, Customer::setDomicilio);
                       mapper.using(emptyStringConverter).map(CustomerDto::getTelefonoCelular, Customer::setTelefonoCelular);
                       mapper.using(emptyStringConverter).map(CustomerDto::getEmail, Customer::setEmail);
                   });
        return modelMapper;
    }
}
