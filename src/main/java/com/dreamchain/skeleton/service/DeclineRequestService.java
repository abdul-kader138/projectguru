package com.dreamchain.skeleton.service;


import com.dreamchain.skeleton.model.DeclineRequest;

import java.text.ParseException;
import java.util.Map;

public interface DeclineRequestService {
    DeclineRequest get(Long id);
    Map<String,Object> save(Map<String, Object> declineRequestObj) throws ParseException;
}
