package com.teamsolution.lab.service.impl;

import com.teamsolution.lab.document.ProductDocument;
import com.teamsolution.lab.dto.ProductDetailDto;
import com.teamsolution.lab.dto.ProductImageDto;
import com.teamsolution.lab.dto.ProductListDto;
import com.teamsolution.lab.dto.ProductVariantDto;
import com.teamsolution.lab.dto.ref.AttributeValueRefDto;
import com.teamsolution.lab.dto.ref.AttributeWithValuesDto;
import com.teamsolution.lab.dto.ref.BrandRefDto;
import com.teamsolution.lab.dto.ref.CategoryRefDto;
import com.teamsolution.lab.dto.request.ProductFilterRequest;
import com.teamsolution.lab.entity.Attribute;
import com.teamsolution.lab.entity.AttributeValue;
import com.teamsolution.lab.entity.Brand;
import com.teamsolution.lab.entity.Product;
import com.teamsolution.lab.entity.ProductImage;
import com.teamsolution.lab.entity.ProductVariant;
import com.teamsolution.lab.entity.ProductVariantAttributeValue;
import com.teamsolution.lab.enums.ProductStatus;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.mapper.ProductImageMapper;
import com.teamsolution.lab.mapper.ProductVariantMapper;
import com.teamsolution.lab.repository.ProductRepository;
import com.teamsolution.lab.service.ProductService;
import com.teamsolution.lab.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl
        implements ProductService {
  private final ProductRepository productRepository;
    private final ProductSearchService productSearchService;
    private final ProductVariantService productVariantService;
    private final ProductVariantMapper productVariantMapper;
    private final ProductImageMapper productImageMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductListDto> getProducts(Pageable pageable, ProductFilterRequest filterRequest) {

        PageImpl<ProductDocument> esResult = productSearchService.search(filterRequest, pageable);

        return esResult.map(doc -> ProductListDto.builder()
                .id(UUID.fromString(doc.getId()))
                .name(doc.getName())
                .slug(doc.getSlug())
                .basePrice(doc.getBasePrice())
                .thumbnail(doc.getThumbnail())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailDto getProductBySlug(String slug) {
        Product product = productRepository
                .findBySlugAndIsDeleteFalseAndStatus(slug, ProductStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return buildProductDetail(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantDto getVariantById(UUID productId, UUID variantId) {
        productRepository.findByIdAndIsDeleteFalseAndStatus(productId, ProductStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Get Variant
        ProductVariant variant = productVariantService
                .findByIdAndProductId(productId, variantId);

        // Check valid variant
        if (!isVariantValid(variant)) {
            throw new ResourceNotFoundException("Variant not found");
        }

        return productVariantMapper.toDto(variant);
    }

    private ProductDetailDto buildProductDetail(Product product) {
        // 1. Check Brand: Brand is active and not deleted
        BrandRefDto brandRef = getActiveBrandRef(product.getBrand());

        // 2. Check images: Image is not deleted
        List<ProductImageDto> images = getValidImageDtos(product.getImages());

        // 3. Check product variants: Variant is not deleted and its attribute value is not deleted
        List<ProductVariant> validVariants = getValidProductVariant(product.getVariants());

        // 4. Build attributes from valid variants
        List<AttributeWithValuesDto> attributes = buildAttributes(validVariants);

        // 5. Map variants
        List<ProductVariantDto> variantDtos = validVariants.stream()
                .map(productVariantMapper::toDto)
                .toList();

        return ProductDetailDto.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .basePrice(product.getBasePrice())
                .status(product.getStatus())
                .category(product.getCategory() != null ? CategoryRefDto.builder()
                        .id(product.getCategory().getId())
                        .name(product.getCategory().getName())
                        .slug(product.getCategory().getSlug())
                        .build() : null)
                .brand(brandRef)
                .images(images)
                .attributes(attributes)
                .variants(variantDtos)
                .build();
    }

    // Build attributes
    private List<AttributeWithValuesDto> buildAttributes(List<ProductVariant> validVariants) {
        Map<UUID, AttributeWithValuesDto> attributeMap = new LinkedHashMap<>();

        validVariants.stream()
                .flatMap(v -> v.getAttributeValues().stream())
                .map(ProductVariantAttributeValue::getAttributeValue)
                .forEach(av -> {
                    Attribute attr = av.getAttribute();
                    UUID attrId = attr.getId();

                    attributeMap.computeIfAbsent(attrId, k ->
                            AttributeWithValuesDto.builder()
                                    .id(attrId)
                                    .name(attr.getName())
                                    .values(new ArrayList<>())
                                    .build()
                    );

                    List<AttributeValueRefDto> values = attributeMap.get(attrId).getValues();
                    boolean exists = values.stream().anyMatch(v -> v.getId().equals(av.getId()));
                    if (!exists) {
                        values.add(AttributeValueRefDto.builder()
                                .id(av.getId())
                                .value(av.getValue())
                                .build());
                    }
                });

        Map<UUID, Integer> attrSortOrderMap = validVariants.stream()
                .flatMap(v -> v.getAttributeValues().stream())
                .map(pvav -> pvav.getAttributeValue().getAttribute())
                .collect(Collectors.toMap(
                        Attribute::getId,
                        Attribute::getSortOrder,
                        (a, b) -> a
                ));

        Map<UUID, Integer> valueSortOrderMap = validVariants.stream()
                .flatMap(v -> v.getAttributeValues().stream())
                .map(ProductVariantAttributeValue::getAttributeValue)
                .collect(Collectors.toMap(
                        AttributeValue::getId,
                        AttributeValue::getSortOrder,
                        (a, b) -> a
                ));

        return attributeMap.values().stream()
                .sorted(Comparator.comparing(a -> attrSortOrderMap.getOrDefault(a.getId(), 0)))
                .peek(a -> a.getValues().sort(
                        Comparator.comparing(v -> valueSortOrderMap.getOrDefault(v.getId(), 0))
                ))
                .toList();
    }

    private BrandRefDto getActiveBrandRef(Brand brand) {
        BrandRefDto brandRef = null;
        if (brand != null
                && !brand.getIsDelete()
                && Boolean.TRUE.equals(brand.getStatus())) {
            brandRef = BrandRefDto.builder()
                    .id(brand.getId())
                    .name(brand.getName())
                    .slug(brand.getSlug())
                    .build();
        }
        return brandRef;
    }

    private List<ProductImageDto> getValidImageDtos(List<ProductImage> productImages) {
       return productImages == null ? List.of() :
               productImages.stream()
                        .filter(img -> !img.getIsDelete())
                        .sorted(Comparator.comparing(ProductImage::getSortOrder))
                        .map(productImageMapper::toDto)
                        .toList();
    }

    private List<ProductVariant> getValidProductVariant(List<ProductVariant> productVariants) {
        return productVariants == null ? List.of() :
                productVariants.stream()
                        .filter(v -> !v.getIsDelete())
                        .filter(v -> v.getAttributeValues() != null && !v.getAttributeValues().isEmpty())
                        .filter(this::isVariantValid)
                        .toList();
    }

    // A variant is valid only if ALL of its attribute values are valid
    private boolean isVariantValid(ProductVariant variant) {
        return variant.getAttributeValues().stream()
                .allMatch(pvav -> {
                    AttributeValue av = pvav.getAttributeValue();
                    if (av == null || av.getIsDelete()) return false;

                    Attribute attr = av.getAttribute();
                    if (attr == null || attr.getIsDelete()) return false;

                    return true;
                });
    }

}
