package com.teamsolution.lab.mapper;

import com.teamsolution.lab.document.ProductDocument;
import com.teamsolution.lab.entity.Product;
import com.teamsolution.lab.entity.ProductImage;
import org.springframework.stereotype.Component;

@Component
public class ProductDocumentMapper {

    public ProductDocument toDocument(Product product) {
        String thumbnail = product.getImages() == null ? null :
                product.getImages().stream()
                        .filter(img -> !img.getIsDelete() && Boolean.TRUE.equals(img.getIsThumbnail()))
                        .findFirst()
                        .map(ProductImage::getImageUrl)
                        .orElse(null);

        return ProductDocument.builder()
                .id(product.getId().toString())
                .name(product.getName())
                .description(product.getDescription())
                .slug(product.getSlug())
                .basePrice(product.getBasePrice())
                .status(product.getStatus().name())
                .thumbnail(thumbnail)
                .categorySlug(product.getCategory() != null ? product.getCategory().getSlug() : null)
                .brandSlug(product.getBrand() != null ? product.getBrand().getSlug() : null)
                .isDelete(product.getIsDelete())
                .createdAt(product.getCreatedAt().toLocalDate())
                .build();
    }
}
