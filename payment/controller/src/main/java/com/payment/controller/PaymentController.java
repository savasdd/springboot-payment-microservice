package com.payment.controller;

import com.load.impl.DataLoad;
import com.payment.common.base.BaseResponse;
import com.payment.common.utils.HeaderUtil;
import com.payment.entity.vo.CanselV0;
import com.payment.entity.vo.*;
import com.payment.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/order")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentController {

    private final OrderService orderService;

    @Operation(summary = "1", tags = "orders")
    @PostMapping(value = "/create")
    public ResponseEntity<BaseResponse> createOrder(HttpServletRequest request, @RequestBody OrderV0 v0) {
        Long userId = HeaderUtil.getUserId(request);
        return new ResponseEntity<>(orderService.createOrder(userId, v0), HttpStatus.CREATED);
    }

    @Operation(summary = "2", tags = "orders")
    @PostMapping(value = "/payment")
    public ResponseEntity<BaseResponse> payment(@RequestBody PaymentV0 v0) {
        return ResponseEntity.ok(orderService.payment(v0));
    }

    @Operation(summary = "2", tags = "orders")
    @PostMapping(value = "/cancel")
    public ResponseEntity<BaseResponse> cancel(@RequestBody CanselV0 v0) {
        return ResponseEntity.ok(orderService.cancel(v0));
    }

    @Operation(summary = "3", tags = "orders")
    @PostMapping(value = "/submit")
    public ResponseEntity<BaseResponse> submit(@RequestBody SubmitV0 v0) {
        return ResponseEntity.ok(orderService.submit(v0));
    }

    @Operation(summary = "4", tags = "orders")
    @PostMapping(value = "/complete")
    public ResponseEntity<BaseResponse> complete(@RequestBody CompleteV0 v0) {
        return ResponseEntity.ok(orderService.complete(v0));
    }

    @Operation(tags = "items")
    @PostMapping(value = "/add-item")
    public ResponseEntity<BaseResponse> addItem(@RequestParam String orderNo, @RequestBody ProductItemV0 v0) {
        return ResponseEntity.ok(orderService.addItem(orderNo, v0));
    }

    @Operation(tags = "items")
    @PostMapping(value = "/remove-item")
    public ResponseEntity<BaseResponse> removeItem(@RequestParam String orderNo, @RequestBody ItemV0 v0) {
        return ResponseEntity.ok(orderService.removeItem(orderNo, v0));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<BaseResponse> getAllOrder() {
        return ResponseEntity.ok(orderService.getAllOrder());
    }

    @GetMapping(value = "/pageable")
    public ResponseEntity<BaseResponse> getPageable(Pageable pageable) {
        return ResponseEntity.ok(orderService.getPageable(pageable));
    }

    @PostMapping(value = "/pageable-load")
    public ResponseEntity<BaseResponse> getAllLoad(@RequestBody DataLoad load) {
        return ResponseEntity.ok(orderService.getAllLoad(load));
    }

    @GetMapping(value = "/findOne/{orderId}")
    public ResponseEntity<BaseResponse> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping(value = "/findOrder/{orderNo}")
    public ResponseEntity<BaseResponse> getOrderNo(@PathVariable String orderNo) {
        return ResponseEntity.ok(orderService.getOrderNo(orderNo));
    }
}
