package com.sdongre.order_service.helper;


import com.sdongre.order_service.model.dto.order.CartDto;
import com.sdongre.order_service.model.dto.order.OrderDto;
import com.sdongre.order_service.model.dto.user.UserDto;
import com.sdongre.order_service.model.entity.Cart;
import com.sdongre.order_service.model.entity.Order;

import java.util.stream.Collectors;

public interface CartMappingHelper {
    static CartDto map(Cart cart) {
        if (cart == null) return null;
        return CartDto.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUserId())
                .orderDtos(cart.getOrders()
                        .stream()
                        .map(order -> OrderDto.builder()
                                .orderId(order.getOrderId())
                                .orderDate(order.getOrderDate())
                                .orderDesc(order.getOrderDesc())
                                .orderFee(order.getOrderFee())
                                .build())
                        .collect(Collectors.toSet()))
                .userDto(
                        UserDto.builder()
                                .id(cart.getUserId())
                                .build())
                .build();
    }

    static Cart map(final CartDto cartDto) {
        if (cartDto == null) return null;
        return Cart.builder()
                .cartId(cartDto.getCartId())
                .userId(cartDto.getUserId())
                .orders(cartDto.getOrderDtos()
                        .stream()
                        .map(orderDto -> Order.builder()
                                .orderId(orderDto.getOrderId())
                                .orderDate(orderDto.getOrderDate())
                                .orderDesc(orderDto.getOrderDesc())
                                .orderFee(orderDto.getOrderFee())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
