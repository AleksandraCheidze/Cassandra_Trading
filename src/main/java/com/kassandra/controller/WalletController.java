package com.kassandra.controller;

import com.kassandra.domain.WalletTransactionType;
import com.kassandra.modal.*;
import com.kassandra.response.PaymentResponse;
import com.kassandra.service.*;
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

import java.math.BigDecimal;
import java.util.List;


@RestController
@Tag(name = "Wallet", description = "Wallet management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;


    @Autowired
    private OrderService orderService;

    @Autowired
    private WalletTransactionService walletTransactionService;

    @Autowired
    private PaymentService paymentService;


    @Operation(summary = "Get user wallet", description = "Returns the wallet for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wallet retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Wallet.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @GetMapping("/api/wallet")
    public ResponseEntity<?> getUserWallet(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization")String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);

        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @Operation(summary = "Get wallet transactions", description = "Returns all transactions for the user's wallet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @GetMapping("/api/wallet/transactions")
    public ResponseEntity<List<WalletTransaction>> getWalletTransaction(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization")String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);

        List<WalletTransaction> transactions=walletTransactionService.getTransactions(wallet,null);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @Operation(summary = "Deposit money", description = "Deposits money into the user's wallet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Money deposited successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @PutMapping("/api/wallet/deposit/amount/{amount}")
    public ResponseEntity<PaymentResponse> depositMoney(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization")String jwt,
            @Parameter(description = "Amount to deposit", required = true)
            @PathVariable Long amount) throws Exception {
        User user =userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);

        PaymentResponse res = new PaymentResponse();
        res.setPayment_url("deposite success");
        walletService.addBalanceToWallet(wallet, amount);

        return new ResponseEntity<>(res,HttpStatus.OK);

    }


    @Operation(summary = "Add balance from payment", description = "Adds balance to wallet from a completed payment order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Balance added successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Wallet.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "404", description = "Wallet or payment order not found")
    })
    @PutMapping("/api/wallet/deposit")
    public ResponseEntity<Wallet> addBalanceToWallet(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization")String jwt,
            @Parameter(description = "Payment order ID", required = true)
            @RequestParam(name="order_id") Long orderId,
            @Parameter(description = "Payment ID from payment provider", required = true)
            @RequestParam(name="payment_id")String paymentId
    ) throws Exception {
        User user =userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);


        PaymentOrder order = paymentService.getPaymentOrderById(orderId);
        Boolean status=paymentService.proccedPaymentOrder(order,paymentId);
        PaymentResponse res = new PaymentResponse();
        res.setPayment_url("deposite success");

        if(wallet.getBalance()==null){
    wallet.setBalance(BigDecimal.valueOf(0));
        }
        if(status){
            wallet=walletService.addBalanceToWallet(wallet, order.getAmount());
        }


        return new ResponseEntity<>(wallet,HttpStatus.OK);

    }


    @Operation(summary = "Transfer between wallets", description = "Transfers money from user's wallet to another wallet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Transfer completed successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Wallet.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or insufficient funds"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @PutMapping("/api/wallet/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Recipient wallet ID", required = true)
            @PathVariable Long walletId,
            @Parameter(description = "Transaction details", required = true)
            @RequestBody WalletTransaction req) throws Exception {
        User senderUser = userService.findUserProfileByJwt(jwt);

        Wallet receiverWallet = walletService.findWalletById(walletId);

        Wallet wallet = walletService.walletToWalletTransfer(
                senderUser, receiverWallet,
                req.getAmount());

        walletTransactionService.createTransaction(
                wallet,
                WalletTransactionType.WALLET_TRANSFER,
                String.valueOf(receiverWallet.getId()),
                req.getPurpose(),
                req.getAmount()
        );

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }


    @Operation(summary = "Pay for order", description = "Pays for an order using the user's wallet balance")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment completed successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Wallet.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or insufficient funds"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "404", description = "Order or wallet not found")
    })
    @PutMapping("/api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long orderId,
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization")String jwt) throws Exception {
        User user =userService.findUserProfileByJwt(jwt);
        System.out.println("-------- "+orderId);
        Order order=orderService.getOrderById(orderId);

        Wallet wallet = walletService.payOrderPayment(order,user);

        return new ResponseEntity<>(wallet,HttpStatus.OK);

    }
}

