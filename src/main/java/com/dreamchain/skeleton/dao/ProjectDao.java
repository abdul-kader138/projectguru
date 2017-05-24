package com.dreamchain.skeleton.dao;

import com.dreamchain.skeleton.model.Project;

import java.util.List;

/**
 * Created by LAPTOP DREAM on 5/24/2017.
 */
public interface ProjectDao {

    Project get(Long id);
    Long save(Project project);
    void update(Project project);
    void delete(Project project);
    List<Project> findAll();
    Project findByProjectName(String projectName);
}