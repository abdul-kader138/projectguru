package com.dreamchain.skeleton.dao.impl;

import com.dreamchain.skeleton.dao.ProjectDao;
import com.dreamchain.skeleton.model.Project;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectDaoImpl implements ProjectDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public Project get(Long id) {
        return hibernateTemplate.get(Project.class,id);
    }

    @Override
    public Long save(Project project) {
        return (Long)  hibernateTemplate.save(project);
    }

    @Override
    public void update(Project project) {
        hibernateTemplate.merge(project);
    }

    @Override
    public void delete(Project project) {
        hibernateTemplate.delete(project);
    }

    @Override
    public List<Project> findAll() {
        return hibernateTemplate.loadAll(Project.class);
    }

    @Override
    public Project findByProjectName(String projectName) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Project.class);
        Criterion cr = Restrictions.eq("name", projectName);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new Project();
        return (Project)lst.get(0);
    }
}
