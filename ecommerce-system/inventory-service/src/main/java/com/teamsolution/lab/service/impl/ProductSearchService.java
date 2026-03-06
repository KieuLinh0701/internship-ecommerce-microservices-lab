package com.teamsolution.lab.service.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.json.JsonData;
import com.teamsolution.lab.document.ProductDocument;
import com.teamsolution.lab.dto.request.ProductFilterRequest;
import com.teamsolution.lab.enums.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public PageImpl<ProductDocument> search(ProductFilterRequest request, Pageable pageable) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        // get products active + not deleted
        boolQuery.filter(f -> f.term(t -> t.field("status").value(ProductStatus.ACTIVE.name())));
        boolQuery.filter(f -> f.term(t -> t.field("isDelete").value(false)));

        // Keyword
        if (hasValue(request.getKeyword())) {
            boolQuery.must(m -> m.multiMatch(mm -> mm
                    .fields("name^3", "description")
                    .query(request.getKeyword())
                    .fuzziness("AUTO")
            ));
        }

        // Filter category
        if (hasValue(request.getCategorySlug())) {
            boolQuery.filter(f -> f.term(t -> t
                    .field("categorySlug")
                    .value(request.getCategorySlug())
            ));
        }

        // Filter brand
        if (hasValue(request.getBrandSlug())) {
            boolQuery.filter(f -> f.term(t -> t
                    .field("brandSlug")
                    .value(request.getBrandSlug())
            ));
        }

        // Filter price range
        if (request.getMinPrice() != null || request.getMaxPrice() != null) {
            boolQuery.filter(f -> f.range(r -> r
                    .field("basePrice")
                    .gte(request.getMinPrice() != null ? JsonData.of(request.getMinPrice()) : null)
                    .lte(request.getMaxPrice() != null ? JsonData.of(request.getMaxPrice()) : null)
            ));
        }

        NativeQuery query = NativeQuery.builder()
                .withQuery(boolQuery.build()._toQuery())
                .withPageable(pageable)
                .build();

        SearchHits<ProductDocument> hits = elasticsearchOperations
                .search(query, ProductDocument.class);

        List<ProductDocument> content = hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();

        return new PageImpl<>(content, pageable, hits.getTotalHits());
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }
}