package com.sample.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.demo.model.Team;

public interface TeamRepository extends JpaRepository<Team, String> {

}
