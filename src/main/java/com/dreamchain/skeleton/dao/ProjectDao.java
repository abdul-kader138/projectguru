package com.dreamchain.skeleton.dao;

import com.dreamchain.skeleton.model.Project;

import java.util.List;

public interface ProjectDao {

    Project get(Long id);
    Long save(Project project);
    void update(Project project);
    void delete(Project project);
    List<Project> findAll();
    Project findByProjectName(String projectName);
}
