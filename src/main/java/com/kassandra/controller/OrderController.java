package com.kassandra.controller;

import com.kassandra.domain.OrderType;
import com.kassandra.modal.Coin;
import com.kassandra.modal.Order;
import com.kassandra.modal.User;
import com.kassandra.request.CreateOrderRequest;
import com.kassandra.service.CoinService;
import com.kassandra.service.OrderService;
import com.kassandra.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

//    @Autowired
//    private WalletTransactionService transactionService;

    @Operation(summary = "Create a new order", description = "Creates a new buy or sell order for a cryptocurrency")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or insufficient funds"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "404", description = "Coin not found")
    })
    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Order details", required = true)
            @RequestBody CreateOrderRequest req
            ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.findById(req.getCoinId());

        Order order = orderService.processOrder(coin,
                req.getQuantity(),
                req.getOrderType(),
                user);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Get order by ID", description = "Returns details of a specific order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order details retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "403", description = "Forbidden - User doesn't have access to this order"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwtToken,
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long orderId
    ) throws Exception {

        User user = userService.findUserProfileByJwt(jwtToken);

        Order order = orderService.getOrderById(orderId);
        if (order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.ok(order);
        }else {
            throw new Exception("you don't have access");
        }
    }

    @Operation(summary = "Get all user orders", description = "Returns all orders for the authenticated user with optional filtering")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
    })
    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersOfUser(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Filter by order type (BUY/SELL)", required = false)
            @RequestParam(required = false)OrderType order_type,
            @Parameter(description = "Filter by asset symbol", required = false)
            @RequestParam(required = false)String asset_symbol
    ) throws Exception {

        Long userId = userService.findUserProfileByJwt(jwt).getId();

        List<Order> userOrders = orderService.getAllOrdersOfUser(userId, order_type, asset_symbol);
        return ResponseEntity.ok(userOrders);
    }



}
