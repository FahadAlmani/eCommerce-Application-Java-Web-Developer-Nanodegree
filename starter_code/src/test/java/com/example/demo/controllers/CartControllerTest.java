package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);

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
        cart.setItems(List.of(item));
        cart.setItems(new ArrayList<>());
        cart.setUser(user);

        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));
        when(cartRepo.save(cart)).thenReturn(cart);
    }

    @Test
    public void addToCartHappyPath() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("testUsername");
        modifyCartRequest.setQuantity(10);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertNotNull(cart);
        Long cartId = 0L;
        assertEquals(cartId,cart.getId());
        assertEquals("testUsername",cart.getUser().getUsername());
        assertEquals("testItem",cart.getItems().get(0).getName());
    }
    @Test
    public void addToCartInvalidUser() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("testUsername1");
        modifyCartRequest.setQuantity(10);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
    @Test
    public void addToCartInvalidItem() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("testUsername");
        modifyCartRequest.setQuantity(10);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
    @Test
    public void removeFromCartHappyPath() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("testUsername");
        modifyCartRequest.setQuantity(10);
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertNotNull(cart);
        Long cartId = 0L;
        assertEquals(cartId,cart.getId());
        assertEquals("testUsername",cart.getUser().getUsername());
        assertEquals(0,cart.getItems().size());
    }
    @Test
    public void removeFromCartInvalidUser() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("testUsername1");
        modifyCartRequest.setQuantity(10);
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
    @Test
    public void removeFromCartInvalidItem() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("testUsername");
        modifyCartRequest.setQuantity(10);
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
