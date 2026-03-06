package com.teamsolution.lab.service;

import com.teamsolution.lab.dto.BrandDto;
import com.teamsolution.lab.dto.BrandListDto;
import com.teamsolution.lab.dto.request.BrandFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrandService {

  Page<BrandListDto> getActiveBrands(Pageable pageable, BrandFilterRequest request);

  BrandDto getActiveBrandBySlug(String slug);
}
