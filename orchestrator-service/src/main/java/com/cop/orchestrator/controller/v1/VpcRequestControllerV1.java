package com.cop.orchestrator.controller.v1;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cop.orchestrator.dto.VpcRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/request/vpc")
@Slf4j
@Tag(name = "VPC Request API", description = "Version 1 of the VPC Request API")
public class VpcRequestControllerV1 {

    @Operation(summary = "Create a new VPC Request", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VpcRequestDto.class))),
            @ApiResponse(description = "Missing Header", responseCode = "400") })
    @PostMapping
    public ResponseEntity createVpc(@RequestBody VpcRequestDto vpcDto) {
        log.info("Requested new VPC: {}", vpcDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search VPC Requests by Name", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VpcRequestDto.class))),
            @ApiResponse(description = "No VPC Requests Found", responseCode = "404", content = @Content(mediaType = "application/json")) })
    public ResponseEntity<Page<VpcRequestDto>> searchVpcRequestsByName(
            @Parameter(description = "Name to search in VPC names") @RequestParam(required = false) String name,
            @Parameter(description = "Pagination information", example = "{ \"page\": 0, \"size\": 5, \"sort\": [\"vpcName,desc\"] }") @PageableDefault(sort = "vpcName", direction = Sort.Direction.DESC, size = 5) Pageable pageable) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
