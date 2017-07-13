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
    List<Object> findAllStatus(String clientId);
    ChangeRequest findByName(String name, long categoryId);
    ChangeRequest findByCategoryId(long categoryId);
    ChangeRequest findByDepartmentId(long departmentId);
    ChangeRequest findByTeamAllocationId(long teamAllocationId);


}
