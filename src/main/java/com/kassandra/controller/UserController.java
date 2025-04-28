package com.kassandra.controller;

import com.kassandra.request.ForgotPasswordTokenRequest;
import com.kassandra.domain.VerificationType;
import com.kassandra.modal.ForgotPasswordToken;
import com.kassandra.modal.User;
import com.kassandra.modal.VerificationCode;
import com.kassandra.request.ResetPasswordRequest;
// import com.kassandra.response.ApiResponse; // Используем полное имя класса для избежания конфликта с аннотацией Swagger
import com.kassandra.response.AuthResponse;
import com.kassandra.service.*;
import com.kassandra.service.impl.EmailService;
import com.kassandra.utils.OtpUtils;
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

import java.util.UUID;

@RestController
@Tag(name = "Users", description = "User management endpoints")
public class UserController {
    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    private String jwt;

    @Operation(summary = "Get user profile", description = "Returns the profile of the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @Operation(summary = "Send verification OTP", description = "Sends a verification OTP to the user's email or phone")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OTP sent successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Verification type (EMAIL or MOBILE)", required = true)
            @PathVariable VerificationType verificationType) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.
                getVerificationCodeByUser(user.getId());

        if (verificationCode == null) {
            verificationCode = verificationCodeService.
                    sendVerificationCode(user, verificationType);
        }
        if (verificationType.equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        }

        return new ResponseEntity<>("verification otp sent successfully", HttpStatus.OK);
    }

    @Operation(summary = "Enable two-factor authentication", description = "Enables two-factor authentication for the user by verifying OTP")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Two-factor authentication enabled successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "400", description = "Invalid OTP")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(
            @Parameter(description = "OTP code", required = true)
            @PathVariable String otp,
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL) ?
                verificationCode.getEmail() : verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtp().equals(otp);

        if (isVerified) {
            User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(),
                    sendTo, user);
            verificationCodeService.deleteVerificationCodeById(verificationCode);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        throw new Exception("wrong otp");
    }
    @Operation(summary = "Send password reset OTP", description = "Sends a password reset OTP to the user's email or phone")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset OTP sent successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(
            @Parameter(description = "Password reset request details", required = true)
            @RequestBody ForgotPasswordTokenRequest req) throws Exception {

        User user = userService.findUserByEmail(req.getSendTo());
        String otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());

        if (token==null) {
            token = forgotPasswordService.createToken(user,id, otp, req.getVerificationType(), req.getSendTo());
        }

        if (req.getVerificationType().equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(
                    user.getEmail(),
                    token.getOtp());
        }
        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("Password reset otp sent successfully");


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Reset password", description = "Resets the user's password by verifying OTP")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.kassandra.response.ApiResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "400", description = "Invalid OTP"),
        @ApiResponse(responseCode = "404", description = "Reset token not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<com.kassandra.response.ApiResponse> resetPassword(
            @Parameter(description = "Reset token ID", required = true)
            @RequestParam String id,
            @Parameter(description = "Reset password request details", required = true)
            @RequestBody ResetPasswordRequest req,
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwt) throws Exception {

        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);

        boolean isVerified = forgotPasswordToken.getOtp().equals(req.getOtp());

        if (isVerified){
            userService.updatePassword(forgotPasswordToken.getUser(), req.getPassword());
            com.kassandra.response.ApiResponse res = new com.kassandra.response.ApiResponse();
            res.setMessage("password update successfully");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        throw new Exception("wrong otp");
    }


}
