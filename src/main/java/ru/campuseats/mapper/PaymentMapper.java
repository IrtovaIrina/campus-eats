package ru.campuseats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.campuseats.dto.PaymentReceiptDto;
import ru.campuseats.dto.PaymentRequestDto;
import ru.campuseats.entity.FoodOrder;
import ru.campuseats.entity.PaymentTransaction;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "orderNumber", source = "order.orderNumber")
    @Mapping(target = "canteenName", source = "order.canteen.name")
    @Mapping(target = "paymentMethodDisplayName", expression = "java(tx.getPaymentMethod() != null ? tx.getPaymentMethod().getDisplayName() : \"\")")
    PaymentReceiptDto toReceiptDto(PaymentTransaction tx);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "order")
    @Mapping(target = "paymentMethod", source = "request.paymentMethod")
    @Mapping(target = "amountPaid", source = "finalAmountPaid")
    @Mapping(target = "transactionReference", expression = "java(generateTxRef(request))")
    @Mapping(target = "status", constant = "SUCCESS")
    @Mapping(target = "paidAt", expression = "java(java.time.LocalDateTime.now())")
    PaymentTransaction toEntity(
            FoodOrder order,
            PaymentRequestDto request,
            BigDecimal finalAmountPaid
    );

    default String generateTxRef(PaymentRequestDto request) {
        return "TXN-" + request.getPaymentMethod().name() + "-"
                + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}