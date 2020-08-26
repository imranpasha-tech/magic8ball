package com.icims.labs.services.eightball.service.impl;

import com.amazonaws.services.comprehend.model.SentimentScore;
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

import java.time.LocalDateTime;
import java.util.List;
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
		if (sentimentResult.getSentiment().equals("MIXED")) {
			String sentiment = decideAnswer(sentimentResult.getScore());
			randomAnswer = mapSentimentToAnswers(sentiment);
		} else {
			randomAnswer = mapSentimentToAnswers(sentimentResult.getSentiment());
		}

		logger.info("Random answer is fetched: {}", randomAnswer);
		QuestionDTO questionDTO = buildQuestionDTO(userRequest, randomAnswer);
		saveQuestionHistory(questionDTO);
		SentimentAnswer answer = SentimentAnswer.builder().answer(randomAnswer).sentimentResult(sentimentResult)
				.build();
		return answer;
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

    private void saveQuestionHistory(QuestionDTO questionDTO) {
        Optional<History> history = magic8BallRepository.findByTruncatedQuestion(questionDTO.getTruncatedQuestion(), questionDTO.getLanguageCode());
        if(history.isPresent()){
            int frequency = history.get().getFrequency();
            history.get().setFrequency(++frequency);
            magic8BallRepository.save(history.get());
        }
        else {
            magic8BallRepository.save(buildQuestionHistory(questionDTO, 1));
        }
    }

    private String truncate(String question) {
        return question.replaceAll("[,;\\s]", "");
    }

    private History buildQuestionHistory(QuestionDTO questionDTO, int frequency) {
        return History.builder().question(questionDTO.getQuestion()).truncatedQuestion(questionDTO.getTruncatedQuestion())
                .frequency(frequency)
                .languageCode(questionDTO.getLanguageCode())
                .createdDate(LocalDateTime.now()).answer(questionDTO.getAnswer())
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
