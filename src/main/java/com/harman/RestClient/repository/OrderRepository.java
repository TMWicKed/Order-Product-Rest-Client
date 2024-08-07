package com.harman.RestClient.repository;

import com.harman.RestClient.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
