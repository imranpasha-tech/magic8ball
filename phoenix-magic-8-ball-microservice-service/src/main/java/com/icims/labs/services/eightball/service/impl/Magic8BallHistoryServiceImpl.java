package com.icims.labs.services.eightball.service.impl;

import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.repository.Magic8BallRepository;
import com.icims.labs.services.eightball.service.Magic8BallHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class Magic8BallHistoryServiceImpl implements Magic8BallHistoryService {

    @Autowired
    private Magic8BallRepository magic8BallRepository;

    @Override
    public List<History> getHistory() {
        return magic8BallRepository.findAll();
    }

    /**
     *
     * @param languageCode; cannot be null
     * @throws IllegalArgumentException; if languageCode is null
     * @return list of top 25 trending questions by language or empty list.
     */
    @Override
    public List<History> getTrendingQuestions(String languageCode) {
        Objects.requireNonNull(languageCode, "language code cannot be null");

        List<History> history = magic8BallRepository.getTrendingQuestionsByLanguage(languageCode, PageRequest.of(0, 25));

        if (!history.isEmpty())
            return history;

        return Collections.emptyList();
    }

}
