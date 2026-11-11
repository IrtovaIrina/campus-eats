package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.campuseats.dto.PaymentReceiptDto;
import ru.campuseats.dto.PaymentRequestDto;
import ru.campuseats.entity.FoodOrder;
import ru.campuseats.entity.PaymentTransaction;
import ru.campuseats.enums.OrderStatus;
import ru.campuseats.mapper.PaymentMapper;
import ru.campuseats.repository.FoodOrderRepository;
import ru.campuseats.repository.PaymentTransactionRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    public PaymentReceiptDto processDemoPayment(PaymentRequestDto request) {
        FoodOrder order = foodOrderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Заказ с ID " + request.getOrderId() + " не найден"
                ));

        if (Boolean.TRUE.equals(order.getIsPaid()) || paymentTransactionRepository.existsByOrderId(order.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Заказ #" + order.getOrderNumber() + " уже оплачен"
            );
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Невозможно оплатить отменённый заказ"
            );
        }

        BigDecimal finalAmountPaid = order.getTotalAmount().setScale(2, RoundingMode.HALF_UP);

        order.setIsPaid(true);
        order.setUpdatedAt(LocalDateTime.now());
        foodOrderRepository.save(order);

        PaymentTransaction transaction = paymentMapper.toEntity(order, request, finalAmountPaid);
        PaymentTransaction savedTx = paymentTransactionRepository.save(transaction);
        return paymentMapper.toReceiptDto(savedTx);
    }

    @Transactional(readOnly = true)
    public PaymentReceiptDto getReceiptByOrderId(Long orderId) {
        PaymentTransaction tx = paymentTransactionRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Квитанция для заказа с ID " + orderId + " не найдена"
                ));
        return paymentMapper.toReceiptDto(tx);
    }
}