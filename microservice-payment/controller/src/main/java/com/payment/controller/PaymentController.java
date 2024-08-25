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
    @PostMapping(value = "/add-product/{orderNo}")
    public ResponseEntity<BaseResponse> addProduct(@PathVariable String orderNo, @RequestBody ProductItemDto productItem) {
        return ResponseEntity.ok(orderService.addProduct(orderNo, productItem));
    }

    @Operation(summary = "order no: 3", tags = "ordering payment")
    @DeleteMapping(value = "/delete-product/{orderNo}/{productId}")
    public ResponseEntity<BaseResponse> deleteProduct(@PathVariable String orderNo, @PathVariable String productId) {
        return ResponseEntity.ok(orderService.deleteProduct(orderNo, productId));
    }

    @Operation(summary = "order no: 4", tags = "ordering payment")
    @GetMapping(value = "/payment/{orderNo}")
    public ResponseEntity<BaseResponse> payment(@PathVariable String orderNo) {
        return ResponseEntity.ok(orderService.payment(orderNo));
    }

    @Operation(summary = "order no: 4", tags = "ordering payment")
    @PostMapping(value = "/cancel/{orderNo}")
    public ResponseEntity<BaseResponse> cancel(@PathVariable String orderNo, @RequestBody OrderCanselDto orderCansel) {
        return ResponseEntity.ok(orderService.cancel(orderNo, orderCansel));
    }

    @Operation(summary = "order no: 5", tags = "ordering payment")
    @GetMapping(value = "/submit/{orderNo}")
    public ResponseEntity<BaseResponse> submit(@PathVariable String orderNo) {
        return ResponseEntity.ok(orderService.submit(orderNo));
    }

    @Operation(summary = "order no: 6", tags = "ordering payment")
    @GetMapping(value = "/complete/{orderNo}")
    public ResponseEntity<BaseResponse> complete(@PathVariable String orderNo) {
        return ResponseEntity.ok(orderService.complete(orderNo));
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity<BaseResponse> getAllOrder(Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrder(pageable));
    }

    @GetMapping(value = "/findOne/{orderId}")
    public ResponseEntity<BaseResponse> getOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping(value = "/findOne/{orderNo}")
    public ResponseEntity<BaseResponse> getOrderNo(@PathVariable String orderNo) {
        return ResponseEntity.ok(orderService.getOrderNo(orderNo));
    }
}
