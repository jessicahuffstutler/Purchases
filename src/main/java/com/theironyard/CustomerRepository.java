package com.theironyard;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by jessicahuffstutler on 11/11/15.
 */
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
}
