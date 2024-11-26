package com.example.demo1.service.item;

import com.example.demo1.domain.item.OrderLog;
import com.example.demo1.domain.item.Orders;
import com.example.demo1.exception.order.OrderNotFoundException;
import com.example.demo1.repository.order.OrderLogRepository;
import com.example.demo1.repository.order.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderLogService {

    private final OrdersRepository ordersRepository;
    private final OrderLogRepository orderLogRepository;

    public List<OrderLog> findByOrderIdx(Long orderIdx) {
        Orders orders = ordersRepository.findById(orderIdx)
                .orElseThrow(OrderNotFoundException::new);
        List<OrderLog> orderLogs = orderLogRepository.findByOrders(orders)
                .orElseThrow(OrderNotFoundException::new);
        return orderLogs;
    }
}