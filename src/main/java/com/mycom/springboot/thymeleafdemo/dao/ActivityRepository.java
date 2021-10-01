package com.mycom.springboot.thymeleafdemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycom.springboot.thymeleafdemo.entity.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {

}
