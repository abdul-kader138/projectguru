package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.ApprovalStatus;

import java.util.List;

public interface ApprovalStatusDao {
    ApprovalStatus get(Long id);
    Long save(ApprovalStatus approvalStatus);
    void update(ApprovalStatus approvalStatus);
    List<ApprovalStatus> findByUserId(long userId);
    ApprovalStatus findByCompanyAndProductAndCategory(long companyId,long productId,long categoryId,String name,String UserType);
    ApprovalStatus findById(long id);
}
