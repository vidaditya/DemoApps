package com.sample.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.demo.model.Developer;

public interface DeveloperRepository extends JpaRepository<Developer, String> {

}
