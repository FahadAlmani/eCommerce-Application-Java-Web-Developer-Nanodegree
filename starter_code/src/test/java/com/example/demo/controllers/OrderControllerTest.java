package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import javax.persistence.criteria.Order;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepo = mock(OrderRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);

        Item item = new Item();
        item.setId(0L);
        item.setName("testItem");
        BigDecimal price = BigDecimal.valueOf(9.99);
        item.setPrice(price);
        item.setDescription("testDescription");

        User user = new User();
        Cart cart = new Cart();
        user.setId(0L);
        user.setUsername("testUsername");
        user.setCart(cart);

        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(List.of(item));

        UserOrder userOrder = new UserOrder();
        userOrder.setId(0L);
        userOrder.setUser(user);
        userOrder.setItems(List.of(item));

        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepo.save(userOrder)).thenReturn(userOrder);
        when(orderRepo.findByUser(user)).thenReturn(List.of(userOrder));
    }

    @Test
    public void submitHappyPath(){
        ResponseEntity<UserOrder> response = orderController.submit("testUsername");
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals("testUsername", order.getUser().getUsername());
        assertEquals("testItem", order.getItems().get(0).getName());
    }
    @Test
    public void submitInvalidUser(){
        ResponseEntity<UserOrder> response = orderController.submit("testUsername1");
        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());
    }
    @Test
    public void getOrdersForUser(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUsername");
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        List<UserOrder> orders = response.getBody();
        assertNotNull(orders);
        assertEquals("testUsername", orders.get(0).getUser().getUsername());
        assertEquals("testItem", orders.get(0).getItems().get(0).getName());
    }
    @Test
    public void getOrdersForInvalidUser(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUsername1");
        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());
    }
}
