package me.andrekunitz.log.domain.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.andrekunitz.log.domain.exception.DomainException;
import me.andrekunitz.log.domain.model.Customer;
import me.andrekunitz.log.domain.repository.CustomerRepository;

@RequiredArgsConstructor
@Service
public class CustomerCatalogService {
	private final CustomerRepository customerRepository;

	@Transactional
	public Customer save(Customer customer) {
		boolean emailInUse = customerRepository.findByEmail(customer.getEmail())
				.stream()
				.anyMatch(existingCustomer -> !existingCustomer.equals(customer));
		if (emailInUse) {
			throw new DomainException("Informed email is already in use.");
		}
		return customerRepository.save(customer);
	}

	@Transactional
	public void remove(Long customerId) {
		customerRepository.deleteById(customerId);
	}
}
