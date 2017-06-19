package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.ChangeRequest;

import java.util.List;

public interface ChangeRequestDao {
    ChangeRequest get(Long id);
    Long save(ChangeRequest changeRequest);
    void update(ChangeRequest changeRequest);
    void delete(ChangeRequest changeRequest);
    List<ChangeRequest> findAll();
    ChangeRequest findByChangeRequestName(String changeRequestName);
    ChangeRequest findByNewName(String CurrentChangeRequestName,String newChangeRequestName);
    List<Object> countOfCategory(long categoryId);

}
