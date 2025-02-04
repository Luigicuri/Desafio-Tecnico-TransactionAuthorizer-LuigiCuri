package com.luigi.desafiocaju.application.service;

import com.luigi.desafiocaju.domain.entity.Category;
import com.luigi.desafiocaju.domain.entity.Merchant;
import com.luigi.desafiocaju.domain.repository.MerchantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    MerchantRepository merchantRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testFindCategoriesWithFallbackMCCAndMerchantCategoryFound() {
        String merchantName = "Merchant";
        String mcc = "5812";

        when(merchantRepository.findByName(merchantName))
                .thenReturn(Optional.of(new Merchant(1L, merchantName, Category.FOOD)));

        Set<Category> categories = categoryService.findCategoriesWithFallback(merchantName, mcc);

        Set<Category> expectedCategories = new LinkedHashSet<>();

        expectedCategories.add(Category.FOOD);
        expectedCategories.add(Category.MEAL);
        expectedCategories.add(Category.CASH);

        assertEquals(expectedCategories, categories,
                "Must return the CATEGORIES from a merchant, MCC, and FALLBACK");
    }

    @Test
    void testFindCategoriesWithFallbackAndOnlyMerchantCategoryFound() {
        String merchantName = "Merchant";
        String mcc = "1234";

        when(merchantRepository.findByName(merchantName))
                .thenReturn(Optional.of(new Merchant(1L, merchantName, Category.FOOD)));

        Set<Category> categories = categoryService.findCategoriesWithFallback(merchantName, mcc);

        Set<Category> expectedCategories = new LinkedHashSet<>();
        expectedCategories.add(Category.FOOD);
        expectedCategories.add(Category.CASH);

        assertEquals(expectedCategories, categories,
                "Must return the CATEGORIES from MCC and Fallback when MERCHANT is not found");
    }

    @Test
    void testFindCategoriesWithFallbackAndOnlyMCCCategoryFound() {
        String merchantName = "Merchant";
        String mcc = "5412";

        when(merchantRepository.findByName(merchantName))
                .thenReturn(Optional.empty());

        Set<Category> categories = categoryService.findCategoriesWithFallback(merchantName, mcc);

        Set<Category> expectedCategories = new LinkedHashSet<>();
        expectedCategories.add(Category.FOOD);
        expectedCategories.add(Category.CASH);

        assertEquals(expectedCategories, categories,
                "Must return CATEGORIES from MCC and Fallback when merchant is not found");
    }


    @Test
    void testFindCategoriesWithFallbackAndFallbackOnly() {
        String merchantName = "Merchant";
        String mcc = "1234";

        when(merchantRepository.findByName(merchantName))
                .thenReturn(Optional.empty());

        Set<Category> categories = categoryService.findCategoriesWithFallback(merchantName, mcc);

        Set<Category> expectedCategories = new LinkedHashSet<>();
        expectedCategories.add(Category.CASH);

        assertEquals(expectedCategories, categories,
                "Must return ONLY the Fallback category when neither MERCHANT or MCC are matched");
    }

}