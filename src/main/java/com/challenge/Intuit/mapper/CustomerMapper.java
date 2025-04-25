package com.challenge.Intuit.mapper;

import com.challenge.Intuit.dto.CustomerDto;
import com.challenge.Intuit.entity.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
	private final ModelMapper modelMapper; // Inyecta ModelMapper

	@Autowired
	public CustomerMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	/**
	 * Maps a Customer entity to a CustomerDto.
	 * @param customer The source Customer entity.
	 * @return The resulting CustomerDto, or null if the source entity is null.
	 */
	public CustomerDto toDto(Customer customer) {
		if (customer == null) {
			return null;
		}
		return modelMapper.map(customer, CustomerDto.class);
	}

	/**
	 * Maps a CustomerDto to a Customer entity.
	 * Applies default values (empty strings for Strings, default date for LocalDate)
	 * to null fields in the source DTO, according to the configuration in ModelMapperConfig.
	 * @param dto The source CustomerDto.
	 * @return The resulting Customer entity, or null if the source DTO is null.
	 */
	public Customer toEntity(CustomerDto dto) {
		if (dto == null) {
			return null;
		}
		Customer entity = modelMapper.map(dto, Customer.class);
		return entity;
	}

	/**
	 * Updates an existing Customer entity with data from a CustomerDto.
	 * Only copies the values from the DTO that are NOT null to the existing entity.
	 * @param dto The CustomerDto with updated data.
	 * @param entity The existing Customer entity to be updated.
	 */
	public void updateCustomerFromDto(CustomerDto dto, Customer entity) {
		if (dto == null || entity == null) {
			return;
		}
		modelMapper.map(dto, entity);

	}
}
