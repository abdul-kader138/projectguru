package com.dreamchain.skeleton.dao.impl;

import com.dreamchain.skeleton.dao.CustomerDao;
import com.dreamchain.skeleton.model.Customer;
import com.dreamchain.skeleton.model.User;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDaoImpl implements CustomerDao{

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public Customer get(Long id) {
        return hibernateTemplate.get(Customer.class,id);
    }

    @Override
    public void save(Customer customer) {
            hibernateTemplate.merge(customer);

    }

    @Override
    public void update(Customer customer) {
        hibernateTemplate.update(customer);
    }

    @Override
    public void delete(Customer customer) {
      hibernateTemplate.delete(customer);
    }

    @Override
    public void remove(Customer customer) {
        hibernateTemplate.evict(customer);
    }

    @Override
    public List<Customer> findAll() {
        return hibernateTemplate.loadAll(Customer.class);
    }

    @Override
    public Customer findByCustomerCode(String customerCode) {
        Customer customer=null;
        DetachedCriteria dcr= DetachedCriteria.forClass(Customer.class);
        Criterion cr = Restrictions.eq("customerCode", customerCode);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return customer;
        return (Customer)lst.get(0);
    }

    @Override
    public Customer findByNid(String nid) {
        Customer customer=null;
        DetachedCriteria dcr= DetachedCriteria.forClass(Customer.class);
        Criterion cr = Restrictions.eq("nid", nid);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return customer;
        return (Customer)lst.get(0);
    }

    @Override
    public Customer findByEmailAndPhone(String email, String phone) {
        Customer customer=null;
        DetachedCriteria dcr= DetachedCriteria.forClass(Customer.class);
        Criterion crEmail = Restrictions.eq("email", email);
        Criterion crPhone = Restrictions.eq("phone", phone);
        Criterion cr = Restrictions.or(Restrictions.and(crEmail), crPhone);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return customer;
        return (Customer)lst.get(0);
    }


    @Override
    public Customer updateCustomerEmailAndPhone(String email, String phone,long id) {
        Customer customer=null;
        DetachedCriteria dcr= DetachedCriteria.forClass(Customer.class);
        Criterion crEmail = Restrictions.eq("email", email);
        Criterion crPhone = Restrictions.eq("phone", phone);
        Criterion crId = Restrictions.ne("id", id);
        Criterion cr = Restrictions.or(Restrictions.and(crEmail), crPhone);
        dcr.add(cr);
        dcr.add(crId);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return customer;
        return (Customer)lst.get(0);
    }
}
