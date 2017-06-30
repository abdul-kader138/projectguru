package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.ApprovalStatus;

import java.util.List;
import java.util.Set;

public interface ApprovalStatusDao {
    ApprovalStatus get(Long id);
    Long save(ApprovalStatus approvalStatus);
    void update(ApprovalStatus approvalStatus);
    List<ApprovalStatus> findByUserId(long userId);
    List<ApprovalStatus> findByUserIdAndRequestId(long userId,long requestId);
    ApprovalStatus findByRequestIdAndUserType(long requestId,String UserType);
    Set<ApprovalStatus> findByApprovedId(long userId);
    List<ApprovalStatus> findByApprovedById(long userId);
    void delete(Long id);
}
