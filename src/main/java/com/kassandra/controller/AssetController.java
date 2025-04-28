package com.kassandra.controller;

import com.kassandra.modal.Asset;
import com.kassandra.modal.User;
import com.kassandra.service.AssetService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset")
@Tag(name = "Assets", description = "User cryptocurrency assets management")
@SecurityRequirement(name = "bearerAuth")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Get asset by ID", description = "Returns details of a specific asset")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Asset details retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Asset.class))),
        @ApiResponse(responseCode = "404", description = "Asset not found")
    })
    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAssetById(
            @Parameter(description = "Asset ID", required = true)
            @PathVariable Long assetId) throws Exception {
        Asset asset = assetService.getAssetById(assetId);
        return ResponseEntity.ok().body(asset);
    }

    @Operation(summary = "Get user asset by coin ID", description = "Returns a specific asset owned by the authenticated user for a given coin")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Asset details retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Asset.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
        @ApiResponse(responseCode = "404", description = "Asset not found")
    })
    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(
            @Parameter(description = "Cryptocurrency ID", required = true)
            @PathVariable String coinId,
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwt
    )throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Asset asset = assetService.findAssetByUserIdAndCoinId(user.getId(), coinId);
        return ResponseEntity.ok().body(asset);
    }

    @Operation(summary = "Get all user assets", description = "Returns all assets owned by the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Assets retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
    })
    @GetMapping()
    public ResponseEntity<List<Asset>> getAssetsForUser (
            @Parameter(description = "JWT token for authentication", required = true)
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Asset> assets = assetService.getUsersAssets(user.getId());
        return ResponseEntity.ok().body(assets);
    }
}
