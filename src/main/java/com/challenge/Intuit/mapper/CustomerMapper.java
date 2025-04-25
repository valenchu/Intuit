package com.challenge.Intuit.mapper;

import com.challenge.Intuit.dto.CustomerDto;
import com.challenge.Intuit.entity.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
	 * Maps a list of CustomerDto to a list of Customer entities.
	 * Handles null or empty input list and null elements within the list.
	 * @param dtoList The source list of CustomerDto. Can be null.
	 * @return A new list of Customer entities. Returns an empty list if the input list is null or empty.
	 */
	public List<Customer> toEntityList(List<CustomerDto> dtoList) {
		if (dtoList == null || dtoList.isEmpty()) {
			return Collections.emptyList();
		}

		return dtoList.stream()
				.filter(dto -> dto != null)
				.map(this::toEntity)
				.filter(entity -> entity != null)
				.collect(Collectors.toList());
	}

	/**
	 * Maps a list of Customer entities to a list of CustomerDto.
	 * Handles null or empty input list and null elements within the list.
	 * @param entityList The source list of Customer entities. Can be null.
	 * @return A new list of CustomerDto. Returns an empty list if the input list is null or empty.
	 */
	public List<CustomerDto> toDtoList(List<Customer> entityList) {
		if (entityList == null || entityList.isEmpty()) {
			return Collections.emptyList();
		}

		return entityList.stream()
				.filter(entity -> entity != null)
				.map(this::toDto)
				.filter(dto -> dto != null)
				.collect(Collectors.toList());
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
