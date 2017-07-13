package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.UserAllocation;

import java.util.List;

public interface UserAllocationDao {
    UserAllocation get(Long id);
    Long save(UserAllocation userAllocation);
    void update(UserAllocation userAllocation);
    void delete(UserAllocation userAllocation);
    List<UserAllocation> findAll();
    List<UserAllocation> AllAllocationByItCoordinator(long itCoordinator);
    List<UserAllocation> AllAllocationByApprovedBy(long approvedBy);
    UserAllocation findByProductAndCategory(long companyId, long productId,long categoryId);
    List<Object> countOfAllocation(long allocationId); // request class

}
