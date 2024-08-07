package com.harman.RestClient.controller;

import com.harman.RestClient.model.Order;
import com.harman.RestClient.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService service;

    @GetMapping("/all")
    public List<Order> getAll(){
        return service.findAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Order> getById(@PathVariable Integer id){
        Order order=service.findById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody Order order){
        return ResponseEntity.ok(service.save(order));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody Order order){
        return ResponseEntity.ok(service.update(id, order));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/quantity/totalPrice")
    public List<Order> getOrderByQuantityAndTotalPrice(){
        return service.getOrderByQuantityAndTotalPrice();
    }

    @GetMapping("/groupByQuantity")
    public Map<Integer, List<Order>> getOrdersGroupedByQuantity() {
        return service.groupByQuantity();
    }
}
