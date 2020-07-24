package org.stepik.database_service.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.stepik.database_service.datasets.UsersDataSet;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class UsersDAO {

    private Session session;

    public UsersDAO(Session session) {
        this.session = session;
    }

    public UsersDataSet get(long id) throws HibernateException {
        return (UsersDataSet) session.get(UsersDataSet.class, id);
    }

    public long getUserId(String name) throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UsersDataSet> criteria = builder.createQuery(UsersDataSet.class);
        Root<UsersDataSet> root = criteria.from(UsersDataSet.class);
        criteria
                .select(root)
                .where(builder.equal(root.get("name"), name));

        Query query = session.createQuery(criteria);
        return ((UsersDataSet) query.getSingleResult()).getId();
    }

    public long insertUser(String name) throws HibernateException {
        return (Long) session.save(new UsersDataSet(name));
    }
}
