package org.stepik.accounts.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.stepik.accounts.datasets.User;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.SQLException;

public class UserDAO {
    private Session session;

    public UserDAO(Session session) {
        this.session = session;
    }

    public long insertUser(User user) throws HibernateException {
        return (Long) session.save(user);
    }

    public User selectUser(String login, String password) throws HibernateException {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        if (login == null || password == null) {
            return null;
        }

        Predicate loginPredicate = criteriaBuilder.equal(root.get("login"), login);
        Predicate passwordPredicate = criteriaBuilder.equal(root.get("password"), password);

        criteriaQuery
                .select(root)
                .where(criteriaBuilder.and(loginPredicate, passwordPredicate));

        Query query = session.createQuery(criteriaQuery);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
