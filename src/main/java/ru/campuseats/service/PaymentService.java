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
import ru.campuseats.entity.User;
import ru.campuseats.enums.OrderStatus;
import ru.campuseats.mapper.PaymentMapper;
import ru.campuseats.repository.FoodOrderRepository;
import ru.campuseats.repository.PaymentTransactionRepository;
import ru.campuseats.repository.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final UserRepository userRepository;
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

        User user = order.getUser();
        BigDecimal orderTotal = order.getTotalAmount();
        BigDecimal bonusToUse = BigDecimal.ZERO;

        if (Boolean.TRUE.equals(request.getUseBonusPoints())
                && user.getBonusBalance() != null
                && user.getBonusBalance().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal maxAllowedBonus = orderTotal.multiply(new BigDecimal("0.50")).setScale(2, RoundingMode.HALF_UP);
            bonusToUse = user.getBonusBalance().min(maxAllowedBonus);
            user.setBonusBalance(user.getBonusBalance().subtract(bonusToUse));
        }

        BigDecimal finalAmountPaid = orderTotal.subtract(bonusToUse).setScale(2, RoundingMode.HALF_UP);
        BigDecimal cashback = finalAmountPaid.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);

        user.setBonusBalance(user.getBonusBalance().add(cashback));
        userRepository.save(user);

        order.setIsPaid(true);
        order.setUpdatedAt(LocalDateTime.now());
        foodOrderRepository.save(order);

        PaymentTransaction transaction = paymentMapper.toEntity(
                order, request, finalAmountPaid, bonusToUse, cashback
        );

        PaymentTransaction savedTx = paymentTransactionRepository.save(transaction);
        return paymentMapper.toReceiptDto(savedTx);
    }

    @Transactional(readOnly = true)
    public PaymentReceiptDto getReceiptByOrderId(Long orderId) {
        PaymentTransaction tx = paymentTransactionRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Квитанция об оплате для заказа с ID " + orderId + " не найдена"
                ));
        return paymentMapper.toReceiptDto(tx);
    }
}
