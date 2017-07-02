package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.DeclineRequest;

public interface DeclineRequestDao {
    Long save(DeclineRequest declineRequest);
    void delete(DeclineRequest declineRequest);
    DeclineRequest get(Long id);
}
