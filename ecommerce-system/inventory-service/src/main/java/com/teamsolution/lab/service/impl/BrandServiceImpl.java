package com.teamsolution.lab.service.impl;

import com.teamsolution.lab.dto.BrandDto;
import com.teamsolution.lab.dto.BrandListDto;
import com.teamsolution.lab.dto.request.BrandFilterRequest;
import com.teamsolution.lab.entity.Brand;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.mapper.BrandListMapper;
import com.teamsolution.lab.mapper.BrandMapper;
import com.teamsolution.lab.repository.BrandRepository;
import com.teamsolution.lab.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandListMapper brandListMapper;
    private final BrandMapper brandMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<BrandListDto> getActiveBrands(Pageable pageable, BrandFilterRequest request) {
        boolean hasKeyword = request.getKeyword() != null && !request.getKeyword().isBlank();

        Page<Brand> brands = hasKeyword
                ? brandRepository.findByIsDeleteFalseAndStatusIsTrueAndNameContainingIgnoreCase(request.getKeyword(), pageable)
                : brandRepository.findByIsDeleteFalseAndStatusIsTrue(pageable);

        return brands.map(brandListMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BrandDto getActiveBrandBySlug(String slug) {
        Brand brand = brandRepository.findByIsDeleteFalseAndStatusIsTrueAndSlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
        return brandMapper.toDto(brand);
    }
}