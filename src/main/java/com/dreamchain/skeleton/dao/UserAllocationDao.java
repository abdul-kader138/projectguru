package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.UserAllocation;

import java.util.List;

public interface UserAllocationDao {
    UserAllocation get(Long id);
    Long save(UserAllocation userAllocation);
    void update(UserAllocation userAllocation);
    void delete(UserAllocation userAllocation);
    List<UserAllocation> findAll();
    UserAllocation findByUserId(long itCoordinatorId, long approvedById, long companyId, long productId,long categoryId);
//    UserAllocation findByUserId(long requestById, long checkedById, long companyId, long departmentId, long productId,long categoryId);
//    UserAllocation findByUserIdAtUpdate(long requestById, long checkedById,
//                                        long companyId, long departmentId, long productId,long categoryId);
    List<Object> countOfAllocation(long allocationId); // request class

}