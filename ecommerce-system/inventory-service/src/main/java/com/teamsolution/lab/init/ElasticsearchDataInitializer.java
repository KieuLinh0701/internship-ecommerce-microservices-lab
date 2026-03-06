package com.teamsolution.lab.init;

import com.teamsolution.lab.document.ProductDocument;
import com.teamsolution.lab.entity.Product;
import com.teamsolution.lab.enums.ProductStatus;
import com.teamsolution.lab.mapper.ProductDocumentMapper;
import com.teamsolution.lab.repository.ProductRepository;
import com.teamsolution.lab.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ElasticsearchDataInitializer implements ApplicationRunner {

    private final ProductRepository productRepository;
    private final ProductSearchRepository productSearchRepository;
    private final ProductDocumentMapper productDocumentMapper;
    private final TransactionTemplate transactionTemplate;

    @Override
    public void run(ApplicationArguments args) {
        transactionTemplate.execute(status -> {

            productSearchRepository.deleteAll();

            List<Product> products = productRepository
                    .findByIsDeleteFalseAndStatus(ProductStatus.ACTIVE);

            List<ProductDocument> docs = products.stream()
                    .map(productDocumentMapper::toDocument)
                    .toList();

            productSearchRepository.saveAll(docs);
            return null;
        });
    }
}