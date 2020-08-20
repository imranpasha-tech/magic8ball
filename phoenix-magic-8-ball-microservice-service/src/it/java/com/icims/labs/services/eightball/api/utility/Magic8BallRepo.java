package com.icims.labs.services.eightball.api.utility;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.repository.Magic8BallRepository;

public interface Magic8BallRepo extends Magic8BallRepository {

	@Query("SELECT m8 FROM History m8 WHERE m8.question =:question")
	List<History> findByQuestion(@Param("question") String question);
}
