package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.TeamAllocation;

import java.util.List;

public interface TeamAllocationDao {
    TeamAllocation get(Long id);
    Long save(TeamAllocation teamAllocation);
    void update(TeamAllocation teamAllocation);
    void delete(TeamAllocation teamAllocation);
    List<TeamAllocation> findAll();
    TeamAllocation findByUserId(long requestById, long checkedById, long companyId, long productId,long categoryId);
    //    UserAllocation findByUserId(long requestById, long checkedById, long companyId, long departmentId, long productId,long categoryId);
//    UserAllocation findByUserIdAtUpdate(long requestById, long checkedById,
//                                        long companyId, long departmentId, long productId,long categoryId);
    List<Object> countOfAllocation(long allocationId); // request class
}
