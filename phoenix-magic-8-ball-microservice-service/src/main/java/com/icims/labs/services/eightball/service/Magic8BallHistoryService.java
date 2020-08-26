package com.icims.labs.services.eightball.service;

import com.icims.labs.services.eightball.entity.History;

import java.util.List;

public interface Magic8BallHistoryService {

    List<History> getHistory();

    List<History> getTrendingQuestions(String languageCode);

}
