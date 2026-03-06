package com.teamsolution.lab.controller;

import com.teamsolution.lab.dto.BrandDto;
import com.teamsolution.lab.dto.BrandListDto;
import com.teamsolution.lab.dto.request.BrandFilterRequest;
import com.teamsolution.lab.response.ApiResponse;
import com.teamsolution.lab.response.PageResponse;
import com.teamsolution.lab.service.BrandService;
import com.teamsolution.lab.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BrandListDto>>> getActiveBrands(
            BrandFilterRequest filterRequest) {

        Pageable pageable = PageableUtils.toPageable(
                filterRequest.getPage(),
                filterRequest.getSize(),
                filterRequest.getSortBy(),
                filterRequest.getDirection());

        Page<BrandListDto> brands = brandService.getActiveBrands(pageable, filterRequest);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(brands)));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<BrandDto>> getActiveBrandBySlug(@PathVariable String slug) {

        BrandDto brand = brandService.getActiveBrandBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(brand));
    }
}