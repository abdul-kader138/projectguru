package com.dreamchain.skeleton.service;


import com.dreamchain.skeleton.model.Project;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ProjectService {

    Project get(Long id);
    Map<String,Object> save(String projectName,long companyId) throws Exception;
    Map<String,Object> update(Map<String, String>  projectObj) throws ParseException;
    String delete(Long projectId);
    List<Project> findAll();
}
