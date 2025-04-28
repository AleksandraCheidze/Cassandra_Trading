package com.kassandra.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kassandra.modal.Coin;
import com.kassandra.service.CoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
@Tag(name = "Coins", description = "Cryptocurrency information endpoints")
public class CoinController {

    @Autowired
    private CoinService coinService;

    @Autowired
    private ObjectMapper objectMapper;

    @Operation(summary = "Get coin list", description = "Returns a paginated list of cryptocurrencies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of coins retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping
    ResponseEntity<List<Coin>> getCoinList(
            @Parameter(description = "Page number for pagination", required = true)
            @RequestParam("page") int page) throws Exception {
        List<Coin> coins=coinService.getCoinList(page);
        return new ResponseEntity<>(coins, HttpStatus.OK);
    }

    @Operation(summary = "Get market chart", description = "Returns market chart data for a specific cryptocurrency")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Chart data retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Coin not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/{coinId}/chart")
    ResponseEntity<JsonNode> getMarketChart(
            @Parameter(description = "Cryptocurrency ID", required = true)
            @PathVariable String coinId,
            @Parameter(description = "Number of days of data to retrieve", required = true)
            @RequestParam("days")int days) throws Exception {
        String coins=coinService.getMarketChart(coinId,days);
        JsonNode jsonNode = objectMapper.readTree(coins);


        return ResponseEntity.ok(jsonNode);

    }


    @Operation(summary = "Search coins", description = "Search for cryptocurrencies by keyword")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search results retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/search")
    ResponseEntity<JsonNode> searchCoin(
            @Parameter(description = "Search keyword", required = true)
            @RequestParam("q") String keyword) throws Exception {
        String coin=coinService.searchCoin(keyword);
        JsonNode jsonNode = objectMapper.readTree(coin);

        return ResponseEntity.ok(jsonNode);

    }
    @Operation(summary = "Get top 50 coins", description = "Returns the top 50 cryptocurrencies by market cap")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Top 50 coins retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/top50")
    ResponseEntity<JsonNode> getTop50CoinByMarketCapRank() throws Exception {
        String coin=coinService.getTop50CoinsByMarketCapRank();
        JsonNode jsonNode = objectMapper.readTree(coin);

        return ResponseEntity.ok(jsonNode);

    }

    @Operation(summary = "Get trending coins", description = "Returns currently trending cryptocurrencies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trending coins retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/treading")
    ResponseEntity<JsonNode> getTreadingCoin() throws Exception {
        String coin=coinService.getTreadingCoins();
        JsonNode jsonNode = objectMapper.readTree(coin);
        return ResponseEntity.ok(jsonNode);

    }

    @Operation(summary = "Get coin details", description = "Returns detailed information about a specific cryptocurrency")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Coin details retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Coin not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/details/{coinId}")
    ResponseEntity<JsonNode> getCoinDetails(
            @Parameter(description = "Cryptocurrency ID", required = true)
            @PathVariable String coinId) throws Exception {
        String coin=coinService.getCoinDetails(coinId);
        JsonNode jsonNode = objectMapper.readTree(coin);

        return ResponseEntity.ok(jsonNode);

    }
}
