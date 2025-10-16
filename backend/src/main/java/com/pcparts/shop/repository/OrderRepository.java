package com.pcparts.shop.repository;

import com.pcparts.shop.entity.Order;
import com.pcparts.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}


