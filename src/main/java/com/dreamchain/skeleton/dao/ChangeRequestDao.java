package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.ChangeRequest;

import java.util.List;
import java.util.Set;

public interface ChangeRequestDao {
    ChangeRequest get(Long id);
    Long save(ChangeRequest changeRequest);
    void update(ChangeRequest changeRequest);
    void delete(ChangeRequest changeRequest);
    List<ChangeRequest> findAll(Set<Long> requestId);
    ChangeRequest findByChangeRequestName(String changeRequestName);
    ChangeRequest findByNewName(String CurrentChangeRequestName,String newChangeRequestName);
    List<Object> countOfCategory(long categoryId);
    List<Object> findAllStatus();
    ChangeRequest findByName(String name, long companyId,long productId,long categoryId);
    ChangeRequest findByRequestById(long requestById);

}
