package com.kassandra.controller;

import com.kassandra.exception.UserException;
import com.kassandra.modal.PaymentDetails;
import com.kassandra.modal.User;
import com.kassandra.service.PaymentDetailsService;
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

@RestController
@RequestMapping("/api")
@Tag(name = "Payment Details", description = "Bank account details for withdrawals")
@SecurityRequirement(name = "bearerAuth")
public class PaymentDetailsController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @Operation(summary = "Add payment details", description = "Adds bank account details for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Payment details added successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentDetails.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
    })
    @PostMapping("/payment-details")
    public ResponseEntity<PaymentDetails> addPaymentDetails(
            @Parameter(description = "Payment details", required = true)
            @RequestBody PaymentDetails paymentDetailsRequest,
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentDetails paymentDetails=paymentDetailsService.addPaymentDetails(
                paymentDetailsRequest.getAccountNumber(),
                paymentDetailsRequest.getAccountHolderName(),
                paymentDetailsRequest.getIfsc(),
                paymentDetailsRequest.getBankName(),
                user
        );
        return new ResponseEntity<>(paymentDetails, HttpStatus.CREATED);
    }

    @Operation(summary = "Get user payment details", description = "Returns the bank account details for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment details retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentDetails.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "404", description = "Payment details not found")
    })
    @GetMapping("/payment-details")
    public ResponseEntity<PaymentDetails> getUsersPaymentDetails(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentDetails paymentDetails=paymentDetailsService.getUsersPaymentDetails(user);
        return new ResponseEntity<>(paymentDetails, HttpStatus.CREATED);
    }
}

