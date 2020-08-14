package com.icims.labs.services.eightball.repository;

import com.icims.labs.services.eightball.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Resource;

@Resource
public interface Magic8BallRepository extends JpaRepository<History, Integer> {

}
