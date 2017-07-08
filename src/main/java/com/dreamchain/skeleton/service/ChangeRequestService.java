package com.dreamchain.skeleton.service;

import com.dreamchain.skeleton.model.ChangeRequest;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ChangeRequestService {
    ChangeRequest get(Long id);
    Map<String,Object> save(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;
    List<ChangeRequest> findAll();
    List<ChangeRequest> findAllForDeveloper();
}
