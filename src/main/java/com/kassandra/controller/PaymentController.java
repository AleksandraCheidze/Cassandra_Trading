package com.kassandra.controller;

import com.kassandra.domain.PaymentMethod;
import com.kassandra.exception.UserException;
import com.kassandra.modal.PaymentOrder;
import com.kassandra.modal.User;
import com.kassandra.response.PaymentResponse;
import com.kassandra.service.PaymentService;
import com.kassandra.service.UserService;
import com.paypal.base.rest.PayPalRESTException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
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
@Tag(name = "Payments", description = "Payment processing endpoints")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Create payment", description = "Creates a payment using the specified payment method")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Payment created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
    })
    @PostMapping("/api/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(
            @Parameter(description = "Payment method (PAYPAL or STRIPE)", required = true)
            @PathVariable PaymentMethod paymentMethod,
            @Parameter(description = "Amount to pay", required = true)
            @PathVariable Long amount,
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentResponse paymentResponse;
        PaymentOrder order = paymentService.createOrder(user, amount, paymentMethod);

        if(paymentMethod.equals(PaymentMethod.PAYPAL)){
            paymentResponse=paymentService.createPaypalPaymentLink(user,amount,
                    order.getId());
        }
        else{
            paymentResponse=paymentService.createStripePaymentLink(user,amount, order.getId());
        }

        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }
}
