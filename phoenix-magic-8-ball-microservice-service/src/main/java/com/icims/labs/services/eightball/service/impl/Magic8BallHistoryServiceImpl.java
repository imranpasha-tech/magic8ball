package com.icims.labs.services.eightball.service.impl;

import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.repository.Magic8BallRepository;
import com.icims.labs.services.eightball.service.Magic8BallHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Magic8BallHistoryServiceImpl implements Magic8BallHistoryService {

        @Autowired
    private Magic8BallRepository magic8BallRepository;

    @Override
    public List<History> getHistory() {
        return magic8BallRepository.findAll();
    }

    @Override
    public List<History> getTrendingQuestions(String languageCode) {
        return magic8BallRepository.getTrendingQuestionsByLanguage(languageCode, PageRequest.of(0, 25));
    }

}
