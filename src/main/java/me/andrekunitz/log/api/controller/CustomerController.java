package me.andrekunitz.log.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.andrekunitz.log.domain.model.Customer;
import me.andrekunitz.log.domain.repository.CustomerRepository;
import me.andrekunitz.log.domain.service.CustomerCatalogService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {
	private final CustomerRepository customerRepository;
	private final CustomerCatalogService customerCatalogService;

	@GetMapping()
	public List<Customer> list() {
		return customerRepository.findAll();
	}

	@GetMapping("/{customerId}")
	public ResponseEntity<Customer> fetch(@PathVariable Long customerId) {
		return customerRepository.findById(customerId)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Customer add(@Valid @RequestBody Customer customer) {
		return customerCatalogService.save(customer);
	}

	@PutMapping("/{customerId}")
	public ResponseEntity<Customer> update(@PathVariable Long customerId, @Valid @RequestBody Customer customer) {
		if (!customerRepository.existsById(customerId)) {
			return ResponseEntity.notFound().build();
		}
		customer.setId(customerId);
		customer = customerCatalogService.save(customer);
		return ResponseEntity.ok(customer);
	}

	@DeleteMapping("/{customerId}")
	public ResponseEntity<Void> remove(@PathVariable Long customerId) {
		if (!customerRepository.existsById(customerId)) {
			return ResponseEntity.notFound().build();
		}
		customerCatalogService.remove(customerId);
		return ResponseEntity.noContent().build();
	}
}
