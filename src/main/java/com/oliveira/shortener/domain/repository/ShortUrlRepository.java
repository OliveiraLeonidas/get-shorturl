package com.oliveira.shortener.domain.repository;

import com.oliveira.shortener.domain.entities.ShortUrl;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {


//    @Query("SELECT su FROM ShortUrl su WHERE su.isPrivate = false ORDER BY su.createdAt DESC")
//    @EntityGraph(attributePaths = {"createdBy"})
    @Query("SELECT su FROM ShortUrl su LEFT JOIN FETCH su.createdBy WHERE su.isPrivate = false ORDER BY su.createdAt DESC")
    List<ShortUrl> findPublicShortUrls();


    Boolean existsByShortKey(String shortKey);

    Optional<ShortUrl> findByShortKey(String shortKey);
}
