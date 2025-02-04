package com.luigi.desafiocaju.application.service;

import com.luigi.desafiocaju.domain.entity.Category;
import com.luigi.desafiocaju.domain.entity.Merchant;
import com.luigi.desafiocaju.domain.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CategoryService {

    @Autowired
    final MerchantRepository merchantRepository;


    public CategoryService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public Set<Category> findCategoriesWithFallback(String merchantName, String mcc) {
        return Stream.of(
                        findMerchantCategory(merchantName),
                        findMCCCategory(mcc),
                        Optional.of(Category.CASH)
                )
                .flatMap(Optional::stream)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Optional<Category> findMerchantCategory(String merchant) {
        return merchantRepository.findByName(merchant).map(Merchant::getCategory);
    }

    private Optional<Category> findMCCCategory(String mcc) {
        return Optional.ofNullable(Map.of(
                "5411", Category.FOOD,
                "5412", Category.FOOD,
                "5811", Category.MEAL,
                "5812", Category.MEAL
        ).get(mcc));
    }
}
