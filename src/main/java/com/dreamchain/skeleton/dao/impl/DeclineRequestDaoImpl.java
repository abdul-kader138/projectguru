package com.dreamchain.skeleton.dao.impl;

import com.dreamchain.skeleton.dao.DeclineRequestDao;
import com.dreamchain.skeleton.model.DeclineRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
@PropertySource("classpath:config.properties")
public class DeclineRequestDaoImpl implements DeclineRequestDao {

    @Autowired
    Environment environment;

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public Long save(DeclineRequest declineRequest) {
        return (Long) hibernateTemplate.save(declineRequest);
    }

    @Override
    public void delete(DeclineRequest declineRequest) {
        hibernateTemplate.delete(declineRequest);
    }

    @Override
    public DeclineRequest get(Long id) {
        return hibernateTemplate.get(DeclineRequest.class,id);
    }
}
