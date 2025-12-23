package com.hackathon.backend.domain.groupbuy.repository;

import com.hackathon.backend.domain.groupbuy.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 이름으로 카테고리 조회
     */
    Optional<Category> findByName(String name);

    /**
     * 최상위 카테고리 조회 (depth = 0)
     */
    List<Category> findByDepth(Integer depth);

    /**
     * 부모 카테고리로 자식 카테고리 조회
     */
    List<Category> findByParent(Category parent);

    /**
     * 부모가 없는 최상위 카테고리 조회
     */
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL ORDER BY c.name")
    List<Category> findTopLevelCategories();
}

