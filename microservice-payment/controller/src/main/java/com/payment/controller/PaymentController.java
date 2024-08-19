package com.payment.controller;

import com.payment.common.base.BaseResponse;
import com.payment.entity.dto.OrderCanselDto;
import com.payment.entity.dto.OrderDto;
import com.payment.entity.dto.ProductItemDto;
import com.payment.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/order")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentController {

    private final OrderService orderService;

    @Operation(summary = "order no: 1", tags = "ordering payment")
    @PostMapping(value = "/create")
    public ResponseEntity<BaseResponse> createOrder(@RequestBody OrderDto order) {
        return new ResponseEntity<>(orderService.createOrder(order), HttpStatus.CREATED);
    }


    @Operation(summary = "order no: 2", tags = "ordering payment")
    @PostMapping(value = "/add-product/{orderId}")
    public ResponseEntity<BaseResponse> addProduct(@PathVariable String orderId, @RequestBody ProductItemDto productItem) {
        return ResponseEntity.ok(orderService.addProduct(orderId, productItem));
    }

    @Operation(summary = "order no: 3", tags = "ordering payment")
    @DeleteMapping(value = "/delete-product/{orderId}/{productId}")
    public ResponseEntity<BaseResponse> deleteProduct(@PathVariable String orderId, @PathVariable String productId) {
        return ResponseEntity.ok(orderService.deleteProduct(orderId, productId));
    }

    @Operation(summary = "order no: 4", tags = "ordering payment")
    @GetMapping(value = "/payment/{orderId}/{paymentId}")
    public ResponseEntity<BaseResponse> payment(@PathVariable String orderId, @PathVariable String paymentId) {
        return ResponseEntity.ok(orderService.payment(orderId, paymentId));
    }

    @Operation(summary = "order no: 4", tags = "ordering payment")
    @PostMapping(value = "/cancel/{orderId}")
    public ResponseEntity<BaseResponse> cancel(@PathVariable String orderId, @RequestBody OrderCanselDto orderCansel) {
        return ResponseEntity.ok(orderService.cancel(orderId, orderCansel));
    }

    @Operation(summary = "order no: 5", tags = "ordering payment")
    @GetMapping(value = "/submit/{orderId}")
    public ResponseEntity<BaseResponse> submit(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.submit(orderId));
    }

    @Operation(summary = "order no: 6", tags = "ordering payment")
    @GetMapping(value = "/complete/{orderId}")
    public ResponseEntity<BaseResponse> complete(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.complete(orderId));
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity<BaseResponse> getAllOrder(Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrder(pageable));
    }

    @GetMapping(value = "/findOne/{orderId}")
    public ResponseEntity<BaseResponse> getOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }
}
