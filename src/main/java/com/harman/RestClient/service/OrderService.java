package com.harman.RestClient.service;

import com.harman.RestClient.exception.ResourceNotFoundException;
import com.harman.RestClient.model.Order;
import com.harman.RestClient.model.Product;
import com.harman.RestClient.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    RestClient client;

    @Autowired
    OrderRepository repository;

    @Value("http://localhost:8080/products")
    private String productUrl;

    public List<Order> findAll() {
        return repository.findAll();
    }

    public Order findById(Integer id) {
        return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Order not found!!"));
    }

    public Order save(Order order) {
        Product product=getProductById(order.getProductId());
        double totalPrice=product.getPrice()*order.getQuantity();
        order.setTotalPrice(totalPrice);
        if(order.getOrderDate()==null)
            order.setOrderDate(new Date());
        return repository.save(order);
    }

    public Order update(Integer id, Order order) {
        Order order1=findById(id);
//        Product product=getProductById(order.getProductId());
        if (order.getQuantity()!=null)
            order1.setQuantity(order.getQuantity());
//        double totalPrice= product.getPrice()* order1.getQuantity();
//        order1.setTotalPrice(totalPrice);
        if(order.getOrderDate()!=null)
            order1.setOrderDate(order.getOrderDate());

//        return repository.save(order1);
        return save(order1);
    }

    private Product getProductById(Integer productId) {
        try {
            return client.get()
                    .uri(productUrl+"/get/"+productId)
                    .retrieve()
                    .body(Product.class);
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("PRODUCT NOT FOUND!!");
        }
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<Order> getOrderByQuantityAndTotalPrice() {
        List<Order> orders = repository.findAll();
        List<Order> order = new ArrayList<>();
        for (Order order1 : orders)
            if (order1.getQuantity()<4 && order1.getTotalPrice()>1000){
                order.add(order1);
        }
        return order;
    }

    public Map<Integer, List<Order>> groupByQuantity() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(Order::getQuantity));
    }
}
