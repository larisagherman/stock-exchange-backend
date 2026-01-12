package com.stock_exchange_backend.stock_exchange_backend;

import com.stock_exchange_backend.stock_exchange_backend.entity.Order;
import com.stock_exchange_backend.stock_exchange_backend.entity.OrderStatus;
import com.stock_exchange_backend.stock_exchange_backend.entity.Transaction;
import com.stock_exchange_backend.stock_exchange_backend.repository.OrderRepository;
import com.stock_exchange_backend.stock_exchange_backend.request.OrderRequest;
import com.stock_exchange_backend.stock_exchange_backend.services.MarketService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class MarketController {

    MarketService marketService;

    MarketController (MarketService marketService) {
        this.marketService = marketService;
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<Map<String, String>> placeOrder(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody OrderRequest orderRequest) {
        if (!isValidAuth(authHeader)) { // mock validation
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return marketService.placeOrder(orderRequest);
    }

    @GetMapping("/orderStatus/{orderId}")
    public ResponseEntity<Map<String, String>> orderStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long orderId) {
        if (!isValidAuth(authHeader)) { // mock validation
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return marketService.orderStatus(orderId);
    }

    @GetMapping("/allOrders")
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestHeader("Authorization") String authHeader) {
        if (!isValidAuth(authHeader)) { // mock validation
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Order> orders = marketService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/allTransactions")
    public ResponseEntity<List<Transaction>> getAllTransactions(
            @RequestHeader("Authorization") String authHeader) {
        if (!isValidAuth(authHeader)) { // mock validation
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Transaction> transactions = marketService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @DeleteMapping("/cancelOrder/{orderId}")
    public ResponseEntity<Map<String, String>> cancelOrder(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long orderId) {
        if (!isValidAuth(authHeader)) { // mock validation
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return marketService.cancelOrder(orderId);
    }

    @PutMapping("/updateOrder/{orderId}")
    public ResponseEntity<Map<String, String>> updateOrder(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long orderId,
            @RequestParam(required = false) Double newPrice,
            @RequestParam(required = false) Integer newQuantity) {
        if (!isValidAuth(authHeader)) { // mock validation
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return marketService.updateOrder(orderId, newPrice, newQuantity);
    }

    private boolean isValidAuth(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        String token = authHeader.substring(7);
        return token.equals("dGhpcyBpcyB0aGUgaGVhZGVyIGZvciBhdXRoCg");
    }
}
