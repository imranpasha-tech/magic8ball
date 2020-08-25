package com.icims.labs.services.eightball.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.repository.Magic8BallRepository;
import com.icims.labs.services.eightball.utility.AbstractDataTestContainer;
import com.icims.labs.services.eightball.utility.Magic8BallRepo;


/**
 * Integration tests for {@link Magic8BallRepository}
 * 
 * @author imran.pasha {@literal ipasha@icims.com}
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Magic8BallIntegrationTest extends AbstractDataTestContainer{

	@Autowired
	private Magic8BallRepo repo;
	
	@Before
	public void init() {
		repo.deleteAll();
		assertThat(repo.count()).isZero();
	}

	@Test
	public void persistFailsWhenQuestionLengthIsOver120() {
		// Question length is more than 120 characters long
		String question = "blahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahah";
		History history = History.builder().question(question).frequency(1).languageCode("en_US")
				.createdDate(LocalDateTime.now()).build();

		Assertions.assertThatThrownBy(() -> repo.save(history)).as("question length is too long")
				.isInstanceOf(DataIntegrityViolationException.class);
	}

	@Test
	public void persistSuccessWhenQuestionLengthIsLessThan120() {
		String question = "blahblahblah?";
		History history = History.builder().question(question).frequency(1).languageCode("en_US")
				.createdDate(LocalDateTime.now()).build();

		Assertions.assertThat(repo.save(history)).as("Succcessfully saved question")
				.hasFieldOrPropertyWithValue("question", "blahblahblah?");
	}

	@Test
	public void persistSucceedsWhenAllFieldsAreValid() {
		String question = "will it snow?";
		LocalDateTime now = LocalDateTime.now();

		History history = History.builder().question(question).frequency(1).languageCode("en_US").createdDate(now)
				.build();

		Assertions.assertThat(repo.save(history)).as("entity saved successfully")
				.hasFieldOrPropertyWithValue("question", "will it snow?").hasFieldOrPropertyWithValue("frequency", 1)
				.hasFieldOrPropertyWithValue("languageCode", "en_US").hasFieldOrPropertyWithValue("createdDate", now);
	}
	
	@Test
	public void persistSucceedsWhenAllFiledsAreValidAndWithoutQuestionMark() {
		String question = "will it snow";
		LocalDateTime now = LocalDateTime.now();

		History history = History.builder().question(question).frequency(1).languageCode("en_US").createdDate(now)
				.build();

		Assertions.assertThat(repo.save(history)).as("entity saved successfully")
				.hasFieldOrPropertyWithValue("question", "will it snow").hasFieldOrPropertyWithValue("frequency", 1)
				.hasFieldOrPropertyWithValue("languageCode", "en_US").hasFieldOrPropertyWithValue("createdDate", now);
	}
}
