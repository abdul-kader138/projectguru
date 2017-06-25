package com.dreamchain.skeleton.service;


import com.dreamchain.skeleton.model.ApprovalStatus;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ApprovalStatusService {
    ApprovalStatus get(Long id);
    Map<String,Object> update(Map<String, Object> approvalObj,HttpServletRequest request) throws ParseException;
    List<ApprovalStatus> findByUserId(HttpServletRequest request);
    String delete(long requestId,long id,HttpServletRequest request);
}
