package com.icims.labs.services.eightball.service.impl;

import com.amazonaws.services.comprehend.model.SentimentScore;
import com.icims.labs.services.eightball.commons.Magic8BallCommons;
import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.enums.Answers;
import com.icims.labs.services.eightball.model.QuestionDTO;
import com.icims.labs.services.eightball.model.SentimentAnswer;
import com.icims.labs.services.eightball.model.SentimentResult;
import com.icims.labs.services.eightball.model.UserRequest;
import com.icims.labs.services.eightball.repository.Magic8BallRepository;
import com.icims.labs.services.eightball.service.ComprehendService;
import com.icims.labs.services.eightball.service.Magic8BallService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

@Service
public class Magic8BallServiceImpl implements Magic8BallService {
    private static final Logger logger = LoggerFactory.getLogger(Magic8BallServiceImpl.class);
    private static final String POSITIVE = "POSITIVE";
    private static final String NEGATIVE = "NEGATIVE";
    private static final String NEUTRAL = "NEUTRAL";

    @Autowired
    private Magic8BallRepository magic8BallRepository;

    @Autowired
    private ComprehendService comprehendService;

    /**
     * Service method that fetches an answer by analyzing sentiment of the question.
     *
     * @return String
     */
	public SentimentAnswer getRandomAnswer(UserRequest userRequest) {
		SentimentResult sentimentResult = comprehendService.getQuestionSentiment(userRequest);
		String randomAnswer;
		String sentiment;
		if (sentimentResult.getSentiment().equals("MIXED")) {
			sentiment = decideAnswer(sentimentResult.getScore());
			randomAnswer = mapSentimentToAnswers(sentiment);
		} else {
			randomAnswer = mapSentimentToAnswers(sentimentResult.getSentiment());
			sentiment = sentimentResult.getSentiment();
		}

		logger.info("Random answer is fetched: {}", randomAnswer);
		QuestionDTO questionDTO = buildQuestionDTO(userRequest, randomAnswer);

		saveQuestionHistory(questionDTO, sentiment, userRequest.getUserId()); // saving sentiment and user info.
		SentimentResult result = SentimentResult.builder().score(sentimentResult.getScore()).sentiment(sentiment).build();
		return SentimentAnswer.builder().answer(randomAnswer).sentimentResult(result).build();
	}

	private String mapSentimentToAnswers(String sentiment) {
		if (sentiment.equals(POSITIVE)) {
			int randomNum = getRandomNumberInRange(0, 9);
			Answers ans = Answers.values()[randomNum];
			return ans.getAnswerKey();
		} else if (sentiment.equals(NEUTRAL)) {
			int randomNum = getRandomNumberInRange(10, 14);
			Answers ans = Answers.values()[randomNum];
			return ans.getAnswerKey();
		} else { // NEGATIVE sentiment
			int randomNum = getRandomNumberInRange(15, 19);
			Answers ans = Answers.values()[randomNum];
			return ans.getAnswerKey();
		}
	}


	private String decideAnswer(SentimentScore score) {
		if (score.getPositive() > score.getNegative() && score.getPositive() > score.getNeutral()) {
			return POSITIVE;
		} else if (score.getNegative() > score.getPositive() && score.getNegative() > score.getNeutral()) {
			return NEGATIVE;
		} else {
			return NEUTRAL;
		}
	}

	private static int getRandomNumberInRange(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

    private QuestionDTO buildQuestionDTO(UserRequest userRequest, String randomAnswer) {
        String truncatedQuestion = truncate(userRequest.getQuestion()).toLowerCase();
        return QuestionDTO.builder().question(userRequest.getQuestion()).truncatedQuestion(truncatedQuestion).languageCode(userRequest.getLanguage().getCode()).answer(randomAnswer).build();
    }

    @Trasactional
    private void saveQuestionHistory(QuestionDTO questionDTO, String sentiment, String userId) {
        Optional<History> history = magic8BallRepository.findByTruncatedQuestion(questionDTO.getTruncatedQuestion(), questionDTO.getLanguageCode());
        if(history.isPresent()){
            int frequency = history.get().getFrequency();
            history.get().setFrequency(++frequency);
            history.get().setAnswer(questionDTO.getAnswer());  	 // saves recently evaluated answer
            history.get().setSentiment(sentiment);        		 // saves recently evaluated sentiment
            magic8BallRepository.save(history.get());
        }
        else {
            magic8BallRepository.save(buildQuestionHistory(questionDTO, 1, sentiment, userId));
            magic8BallRepository.save(Magic8BallCommons.buildQuestionHistory(questionDTO, 1));
        }
    }

    private String truncate(String question) {
        return question.replaceAll("[,;\\s]", "");
    }

    private History buildQuestionHistory(QuestionDTO questionDTO, int frequency, String sentiment, String userId) {
        return History.builder().question(questionDTO.getQuestion()).truncatedQuestion(questionDTO.getTruncatedQuestion())
                .frequency(frequency)
                .languageCode(questionDTO.getLanguageCode())
                .createdDate(LocalDateTime.now()).answer(questionDTO.getAnswer())
                .sentiment(sentiment)
                .userId(userId)
                .build();

    }

    public List<History> getHistory() {
        return magic8BallRepository.findAll();
    }

    @Override
    public List<History> getTrendingQuestions(String languageCode) {
        return magic8BallRepository.getTrendingQuestionsByLanguage(languageCode, PageRequest.of(0, 25));
    }


}
