package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.TeamAllocation;
import com.dreamchain.skeleton.model.User;

import java.util.List;

public interface TeamAllocationDao {
    TeamAllocation get(Long id);
    Long save(TeamAllocation teamAllocation);
    void update(TeamAllocation teamAllocation);
    void delete(TeamAllocation teamAllocation);
    List<TeamAllocation> findAll();
    TeamAllocation findByProductAndCategory(long companyId, long productId,long categoryId);
    List<Object> countOfAllocation(long categoryId); // request class
}
