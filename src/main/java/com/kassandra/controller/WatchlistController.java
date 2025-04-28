package com.kassandra.controller;

import com.kassandra.exception.UserException;
import com.kassandra.modal.Coin;
import com.kassandra.modal.User;
import com.kassandra.modal.Watchlist;
import com.kassandra.service.CoinService;
import com.kassandra.service.UserService;
import com.kassandra.service.WatchlistService;
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
@RequestMapping("/api/watchlist")
@Tag(name = "Watchlist", description = "Watchlist management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class WatchlistController {
    private final WatchlistService watchlistService;
    private final UserService userService;

    @Autowired
    private CoinService coinService;

    @Autowired
    public WatchlistController(WatchlistService watchlistService,
                               UserService userService) {
        this.watchlistService = watchlistService;
        this.userService=userService;
    }

    @Operation(summary = "Get user watchlist", description = "Returns the watchlist for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Watchlist retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Watchlist.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "404", description = "Watchlist not found")
    })
    @GetMapping("/user")
    public ResponseEntity<Watchlist> getUserWatchlist(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user=userService.findUserProfileByJwt(jwt);
        Watchlist watchlist = watchlistService.findUserWatchlist(user.getId());
        return ResponseEntity.ok(watchlist);

    }

    @Operation(summary = "Create watchlist", description = "Creates a new watchlist for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Watchlist created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Watchlist.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
    })
    @PostMapping("/create")
    public ResponseEntity<Watchlist> createWatchlist(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);
        Watchlist createdWatchlist = watchlistService.createWatchList(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWatchlist);
    }

    @Operation(summary = "Get watchlist by ID", description = "Returns a watchlist by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Watchlist retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Watchlist.class))),
        @ApiResponse(responseCode = "404", description = "Watchlist not found")
    })
    @GetMapping("/{watchlistId}")
    public ResponseEntity<Watchlist> getWatchlistById(
            @Parameter(description = "Watchlist ID", required = true)
            @PathVariable Long watchlistId) throws Exception {

        Watchlist watchlist = watchlistService.findById(watchlistId);
        return ResponseEntity.ok(watchlist);

    }

    @Operation(summary = "Add coin to watchlist", description = "Adds a cryptocurrency to the user's watchlist")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Coin added to watchlist successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Coin.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "404", description = "Coin or watchlist not found")
    })
    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchlist(
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Cryptocurrency ID to add to watchlist", required = true)
            @PathVariable String coinId) throws Exception {


        User user=userService.findUserProfileByJwt(jwt);
        Coin coin=coinService.findById(coinId);
        Coin addedCoin = watchlistService.addItemToWatchlist(coin, user);
        return ResponseEntity.ok(addedCoin);

    }
}