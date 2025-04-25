package com.challenge.Intuit.configuration;
import com.challenge.Intuit.dto.CustomerDto;
import com.challenge.Intuit.entity.Customer;
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
        //Schee for customer mapper
        modelMapper.createTypeMap(CustomerDto.class, Customer.class)
                .addMappings(mapper -> {
                    mapper.when((MappingContext<CustomerDto, Customer> src) -> src.getSource().getNombres() ==
                            null).map(src -> "", Customer::setNombres);
                    mapper.when((MappingContext<CustomerDto, Customer> src) -> src.getSource().getApellidos() ==
                            null).map(src -> "", Customer::setApellidos);
                    mapper.when((MappingContext<CustomerDto, Customer> src) -> src.getSource().getCuit() ==
                            null).map(src -> "", Customer::setCuit);
                    mapper.when((MappingContext<CustomerDto, Customer> src) -> src.getSource().getDomicilio() ==
                            null).map(src -> "", Customer::setDomicilio);
                    mapper.when((MappingContext<CustomerDto, Customer> src) -> src.getSource().getTelefonoCelular() ==
                            null).map(src -> "", Customer::setTelefonoCelular);
                    mapper.when((MappingContext<CustomerDto, Customer> src) -> src.getSource().getEmail() ==
                            null).map(src -> "", Customer::setEmail);
                });
        return modelMapper;
    }
}
