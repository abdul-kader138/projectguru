package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.TeamAllocation;

import java.util.List;

public interface TeamAllocationDao {
    TeamAllocation get(Long id);
    Long save(TeamAllocation teamAllocation);
    void update(TeamAllocation teamAllocation);
    void delete(TeamAllocation teamAllocation);
    List<TeamAllocation> findAll();
    List<TeamAllocation> AllAllocationByCheckedBy(long checkedBy);
    List<TeamAllocation> AllAllocationByRequestedBy(long requestedBy);
    TeamAllocation findByProductAndCategory(long companyId, long productId,long categoryId);
    List<Object> countOfAllocation(long categoryId); // request class
    List<Object> countOfAllocationByProduct(long productId); // request class
    TeamAllocation findByRequestById(long requestById);
}
