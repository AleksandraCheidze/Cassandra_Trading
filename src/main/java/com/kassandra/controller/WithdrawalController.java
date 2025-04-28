package com.kassandra.controller;

import com.kassandra.domain.WalletTransactionType;
import com.kassandra.modal.User;
import com.kassandra.modal.Wallet;
import com.kassandra.modal.WalletTransaction;
import com.kassandra.modal.Withdrawal;
import com.kassandra.service.UserService;
import com.kassandra.service.WalletService;
import com.kassandra.service.WalletTransactionService;
import com.kassandra.service.WithdrawalService;
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
@Tag(name = "Withdrawals", description = "Withdrawal management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private WalletTransactionService walletTransactionService;

    @Operation(summary = "Request withdrawal", description = "Creates a new withdrawal request from the user's wallet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Withdrawal request created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Withdrawal.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or insufficient funds"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<?> withdrawalRequest(
            @Parameter(description = "Amount to withdraw", required = true)
            @PathVariable Long amount,
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization")String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);
        Wallet userWallet=walletService.getUserWallet(user);

        Withdrawal withdrawal=withdrawalService.requestWithdrawal(amount,user);
        walletService.addBalanceToWallet(userWallet, -withdrawal.getAmount());

        WalletTransaction walletTransaction = walletTransactionService.createTransaction(
                userWallet,
                WalletTransactionType.WITHDRAWAL,null,
                "bank account withdrawal",
                withdrawal.getAmount()
        );

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }
    @Operation(summary = "Process withdrawal request", description = "Approves or rejects a withdrawal request (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Withdrawal processed successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Withdrawal.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "403", description = "Forbidden - User is not an admin"),
        @ApiResponse(responseCode = "404", description = "Withdrawal request not found")
    })
    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawal(
            @Parameter(description = "Withdrawal request ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Whether to accept the withdrawal (true) or reject it (false)", required = true)
            @PathVariable boolean accept,
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization")String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);

        Withdrawal withdrawal=withdrawalService.procedWithdrawal(id,accept);

        Wallet userWallet=walletService.getUserWallet(user);
        if(!accept){
            walletService.addBalanceToWallet(userWallet, withdrawal.getAmount());
        }

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }
    @Operation(summary = "Get user withdrawal history", description = "Returns all withdrawal requests for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Withdrawal history retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
    })
    @GetMapping("/api/withdrawal")
    public ResponseEntity<List<Withdrawal>> getWithdrawalHistory(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization")String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);

        List<Withdrawal> withdrawal=withdrawalService.getUsersWithdrawalHistory(user);

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @Operation(summary = "Get all withdrawal requests", description = "Returns all withdrawal requests (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Withdrawal requests retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "403", description = "Forbidden - User is not an admin")
    })
    @GetMapping("/api/admin/withdrawal")
    public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization")String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);

        List<Withdrawal> withdrawal=withdrawalService.getAllWithdrawalRequest();

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }
}
