package com.sboot.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sboot.entity.Order;
import com.sboot.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class OrderControllerTest {

    private MockMvc mockMvc;
    private OrderService orderService;
    private OrderController orderController;

    @BeforeEach
    public void setup() {
        orderService = mock(OrderService.class);
        orderController = new OrderController(orderService);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void testCreateOrder() throws Exception {
        Order mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setStatus("Created");

        when(orderService.createOrder()).thenReturn(mockOrder);

        mockMvc.perform(MockMvcRequestBuilders.post("/orders/create")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Created"));

        verify(orderService, times(1)).createOrder();
    }

    @Test
    public void testGetOrder() throws Exception {
        Order mockOrder = new Order();
        mockOrder.setId(2L);
        mockOrder.setStatus("Processing");

        when(orderService.getOrderById(2L)).thenReturn(mockOrder);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Processing"));

        verify(orderService, times(1)).getOrderById(2L);
    }

    @Test
    public void testGetOrderStatus() throws Exception {
        Order mockOrder = new Order();
        mockOrder.setId(3L);
        mockOrder.setStatus("Shipped");

        when(orderService.getOrderById(3L)).thenReturn(mockOrder);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/3/status")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Order status: Shipped"));

        verify(orderService, times(1)).getOrderById(3L);
    }
}
