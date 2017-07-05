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
    List<ChangeRequest> findAllForDeveloper();
    ChangeRequest findByChangeRequestName(String changeRequestName);
    List<Object> countOfCategory(long categoryId);
    List<Object> findAllStatus();
    ChangeRequest findByName(String name, long categoryId);
    ChangeRequest findByCompanyId(long companyId);
    ChangeRequest findByProductId(long productId);
    ChangeRequest findByCategoryId(long categoryId);
    ChangeRequest findByTeamAllocationId(long teamAllocationId);


}
