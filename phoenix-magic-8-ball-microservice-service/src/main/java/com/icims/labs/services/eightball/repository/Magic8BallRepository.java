package com.icims.labs.services.eightball.repository;

import com.icims.labs.services.eightball.entity.History;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface Magic8BallRepository extends JpaRepository<History, Integer>, JpaSpecificationExecutor {

    @Query("SELECT m8 FROM History m8 WHERE m8.languageCode = :languageCode ORDER BY frequency DESC")
    List<History> getTrendingQuestionsByLanguage(@Param("languageCode") String languageCode, Pageable pageable);

    @Query("SELECT m8 FROM History m8 WHERE m8.truncatedQuestion = :truncatedQuestion and languageCode = :languageCode")
    Optional<History> findByTruncatedQuestion(String truncatedQuestion, String languageCode);
}
