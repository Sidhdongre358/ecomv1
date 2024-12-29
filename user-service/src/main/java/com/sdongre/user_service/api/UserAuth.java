package com.sdongre.user_service.api;

import com.sdongre.user_service.model.dto.request.Login;
import com.sdongre.user_service.model.dto.request.SignUp;
import com.sdongre.user_service.model.dto.response.InformationMessage;
import com.sdongre.user_service.model.dto.response.JwtResponseMessage;
import com.sdongre.user_service.model.dto.response.ResponseMessage;
import com.sdongre.user_service.model.dto.response.TokenValidationResponse;
import com.sdongre.user_service.security.jwt.JwtProvider;
import com.sdongre.user_service.security.validate.AuthorityTokenUtil;
import com.sdongre.user_service.security.validate.TokenValidate;
import com.sdongre.user_service.service.EmailService;
import com.sdongre.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "User Auth API", description = "Operations related to user Auth")
public class UserAuth {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private EmailService emailService;


    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping({"/signup", "/register"})
    public Mono<ResponseMessage> register(@Valid @RequestBody SignUp signUp) {
        return userService.register(signUp)
                .map(user -> new ResponseMessage("Create user: " + signUp.getUsername() + " successfully."))
                .onErrorResume(error -> Mono.just(new ResponseMessage("Error occurred while creating the account.")));
    }

    @Operation(summary = "User login", description = "Logs in a user with the provided credentials.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping({"/signin", "/login"})
    public Mono<ResponseEntity<JwtResponseMessage>> login(@Valid @RequestBody Login signInForm) {
        return userService.login(signInForm)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    JwtResponseMessage errorjwtResponseMessage = new JwtResponseMessage(
                            null,
                            null,
                            new InformationMessage()
                    );
                    return Mono.just(new ResponseEntity<>(errorjwtResponseMessage, HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    @Operation(summary = "User logout", description = "Logs out the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logged out successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public Mono<ResponseEntity<String>> logout() {
        log.info("Logout endpoint called");
        return userService.logout()
                .then(Mono.just(new ResponseEntity<>("Logged out successfully.", HttpStatus.OK)))
                .onErrorResume(error -> {
                    log.error("Logout failed", error);
                    return Mono.just(new ResponseEntity<>("Logout failed.", HttpStatus.BAD_REQUEST));
                });
    }


//    @Operation(summary = "Reset Password", description = "Resets the user's password with the provided token.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Password reset successful"),
//            @ApiResponse(responseCode = "400", description = "Invalid token")
//    })
//    @PostMapping("/reset-password")
//    public Mono<ResponseEntity<String>> resetPassword(@RequestParam("token") String token, @RequestBody ResetPasswordRequest resetPasswordRequest) {
//        if (isValidToken(token)) {
//            // Token hợp lệ, đặt mật khẩu mới và cập nhật trong cơ sở dữ liệu
//            updatePassword(userEmail, resetPasswordRequest.getNewPassword());
//            return Mono.just(ResponseEntity.ok("Password reset successful"));
//        } else {
//            // Token không hợp lệ
//            return Mono.just(ResponseEntity.badRequest().body("Invalid token"));
//        }
//    }

    //    @PostMapping({"/refresh", "/refresh-token"})
//    public Mono<ResponseEntity<JwtResponseMessage>> refresh(@RequestHeader("Refresh-Token") String refreshToken) {
//        return userService.refreshToken(refreshToken)
//                .map(newAccessToken -> {
//                    JwtResponseMessage jwtResponseMessage = new JwtResponseMessage(newAccessToken, null, null);
//                    return ResponseEntity.ok(jwtResponseMessage);
//                })
//                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
//    }

    @Operation(summary = "Validate JWT token", description = "Validates the provided JWT token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping({"/validateToken", "/validate-token"})
    public Mono<ResponseEntity<TokenValidationResponse>> validateToken(@RequestHeader(name = "Authorization") String authorizationToken) {
        TokenValidate validate = new TokenValidate();
        if (validate.validateToken(authorizationToken)) {
            return Mono.just(ResponseEntity.ok(new TokenValidationResponse("Valid token")));
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenValidationResponse("Invalid token")));
        }
    }

    @Operation(summary = "Check user authority", description = "Checks if the user has the specified authority.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role access API"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping({"/hasAuthority", "/authorization"})
    public Mono<ResponseEntity<TokenValidationResponse>> getAuthority(@RequestHeader(name = "Authorization") String authorizationToken,
                                                                      @RequestParam String requiredRole) {
        AuthorityTokenUtil authorityTokenUtil = new AuthorityTokenUtil();
        List<String> authorities = authorityTokenUtil.checkPermission(authorizationToken);

        if (authorities.contains(requiredRole)) {
            return Mono.just(ResponseEntity.ok(new TokenValidationResponse("Role access api")));
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenValidationResponse("Invalid token")));
        }
    }

}
