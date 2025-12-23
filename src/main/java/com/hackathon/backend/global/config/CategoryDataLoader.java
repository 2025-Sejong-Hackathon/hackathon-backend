package com.hackathon.backend.global.config;

import com.hackathon.backend.domain.groupbuy.entity.Category;
import com.hackathon.backend.domain.groupbuy.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 공동구매 카테고리 초기 데이터 로더
 */
@Slf4j
@Component
@Order(2) // GikbtiDataLoader(Order 1) 다음에 실행
@RequiredArgsConstructor
public class CategoryDataLoader implements ApplicationRunner {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // 이미 데이터가 있으면 건너뛰기
        if (categoryRepository.count() > 0) {
            log.info("카테고리 데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
            return;
        }

        log.info("공동구매 카테고리 초기 데이터 삽입 시작...");

        // 최상위 카테고리 생성
        Category foodCategory = Category.builder()
                .name("음식")
                .depth(0)
                .build();
        categoryRepository.save(foodCategory);

        Category itemCategory = Category.builder()
                .name("물품")
                .depth(0)
                .build();
        categoryRepository.save(itemCategory);

        // 음식 하위 카테고리
        String[] foodSubCategories = {
                "한식", "카페", "음료", "디저트", "일식",
                "중식", "치킨", "햄버거", "분식", "피자", "기타"
        };

        Map<String, Category> savedCategories = new HashMap<>();
        for (String subCategoryName : foodSubCategories) {
            Category subCategory = Category.builder()
                    .parent(foodCategory)
                    .name(subCategoryName)
                    .depth(1)
                    .build();
            Category saved = categoryRepository.save(subCategory);
            savedCategories.put(subCategoryName, saved);
        }

        log.info("공동구매 카테고리 초기 데이터 삽입 완료: 최상위 2개, 하위 {}개", foodSubCategories.length);
    }
}

