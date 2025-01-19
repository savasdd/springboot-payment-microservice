package com.payment.controller;

import com.load.impl.DataLoad;
import com.payment.common.base.BaseResponse;
import com.payment.common.utils.HeaderUtil;
import com.payment.entity.dto.OrderCanselDto;
import com.payment.entity.vo.ItemV0;
import com.payment.entity.vo.OrderV0;
import com.payment.entity.dto.ProductItemDto;
import com.payment.entity.vo.ProductItemV0;
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
    public ResponseEntity<BaseResponse> createOrder(HttpServletRequest request, @RequestBody OrderV0 order) {
        Long userId = HeaderUtil.getUserId(request);
        return new ResponseEntity<>(orderService.createOrder(userId, order), HttpStatus.CREATED);
    }

    @Operation(summary = "2", tags = "orders")
    @GetMapping(value = "/payment/{orderNo}")
    public ResponseEntity<BaseResponse> payment(@PathVariable String orderNo) {
        return ResponseEntity.ok(orderService.payment(orderNo));
    }

    @Operation(summary = "2", tags = "orders")
    @PostMapping(value = "/cancel/{orderNo}")
    public ResponseEntity<BaseResponse> cancel(@PathVariable String orderNo, @RequestBody OrderCanselDto orderCansel) {
        return ResponseEntity.ok(orderService.cancel(orderNo, orderCansel));
    }

    @Operation(summary = "3", tags = "orders")
    @GetMapping(value = "/submit/{orderNo}")
    public ResponseEntity<BaseResponse> submit(@PathVariable String orderNo) {
        return ResponseEntity.ok(orderService.submit(orderNo));
    }

    @Operation(summary = "4", tags = "orders")
    @GetMapping(value = "/complete/{orderNo}")
    public ResponseEntity<BaseResponse> complete(@PathVariable String orderNo) {
        return ResponseEntity.ok(orderService.complete(orderNo));
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
