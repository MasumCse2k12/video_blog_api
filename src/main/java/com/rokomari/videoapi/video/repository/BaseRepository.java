package com.rokomari.videoapi.video.repository;

import com.rokomari.videoapi.common.utils.Defs;
import com.rokomari.videoapi.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class BaseRepository implements IBaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRepository.class);
    @Autowired
    private EntityManager em;

    @Transactional
    public <T> T persist(T entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }

    @Transactional
    public <T> T merge(T entity) {
        em.merge(entity);
        em.flush();
        return entity;
    }

    @Transactional
    public int deleteByIdIn(Class entity,List<Integer> ids) {
        Query query = em.createQuery("delete from "+entity.getSimpleName()+" o where o.id in("+Utils.listToString(ids)+")");
        return query.executeUpdate();
    }

    @Transactional
    public int updateByNativeQuery(String sql, Map<String,Object> params) {
        if(!Utils.isOk(sql)) {
            return 0;
        }

        printSQL(sql,params);
        Query query = em.createNativeQuery(sql);
        if(params != null && !params.isEmpty()) {
            params.forEach((k,v)->{
                query.setParameter(k,v);
            });
        }

        try {
            return query.executeUpdate();
        } catch (Throwable t) {
            t.printStackTrace();
            return 0;
        }
    }

    @Override
    public <T> List<T> findByNativeQuery(String sql, Class<T> entity,Map<String,Object> params, Integer startIndex, Integer limit) {
        if(!Utils.isOk(sql)) {
            return new ArrayList<T>();
        }

        if(startIndex == null || startIndex <0) {
            startIndex = 0;
        }

        if(limit == null || limit<0) {
            limit = 100;
        }

        printSQL(sql,params);
        Query query = em.createNativeQuery(sql,entity).setFirstResult(startIndex).setMaxResults(limit);
        if(params != null && params.size() >0) {
            params.forEach((k,v)->{
                query.setParameter(k,v);
            });
        }

        return query.getResultList();
    }

    @Override
    public <T> List<T> findByNativeQuery(String sql,Map<String,Object> params, Integer startIndex, Integer limit) {
        if(!Utils.isOk(sql)) {
            return new ArrayList<T>();
        }

        if(startIndex == null || startIndex <0) {
            startIndex = 0;
        }

        if(limit == null || limit<0) {
            limit = 100;
        }

        printSQL(sql,params);
        Query query = em.createNativeQuery(sql).setFirstResult(startIndex).setMaxResults(limit);
        if(params != null && params.size() >0) {
            params.forEach((k,v)->{
                query.setParameter(k,v);
            });
        }

        return query.getResultList();
    }

    @Override
    public <T> List<T> findByNativeQuery(String sql,Map<String,Object> params) {
        if(!Utils.isOk(sql)) {
            return new ArrayList<>();
        }
        printSQL(sql,params);
        Query query = em.createNativeQuery(sql);
        if(params != null && params.size() >0) {
            params.forEach((k,v)->query.setParameter(k,v));
        }

        return query.getResultList();
    }

    @Override
    public <T> List<T> findByNativeQuery(String sql, Class<T> entity, Map<String,Object> params) {
        if(!Utils.isOk(sql)) {
            return new ArrayList<>();
        }
        printSQL(sql,params);
        Query query = em.createNativeQuery(sql,entity);
        if(params != null && params.size() >0) {
            params.forEach((k,v)->{
                query.setParameter(k,v);
            });
        }

        return query.getResultList();
    }


    @Override
    public <T> T findSingleResultByNativeQuery(String sql,Map<String,Object> params) {
        if(!Utils.isOk(sql)) {
            return null;
        }
        printSQL(sql,params);
        Query query = em.createNativeQuery(sql);
        if(params != null && params.size() >0) {
            params.forEach((k,v)->{
                query.setParameter(k,v);
            });
        }
        query.setFirstResult(0).setMaxResults(1);
        try {
            return (T) query.getSingleResult();
        } catch (NoResultException ne) {
            return null;
        }
    }

    @Override
    public <T> T findSingleResultByNativeQuery(String sql,Class<T> entity,Map<String,Object> params) {
        if(!Utils.isOk(sql)) {
            return null;
        }
        printSQL(sql,params);
        Query query = em.createNativeQuery(sql,entity);
        if(params != null && params.size() >0) {
            params.forEach((k,v)->{
                query.setParameter(k,v);
            });
        }
        query.setFirstResult(0).setMaxResults(1);
        try {
            return (T) query.getSingleResult();
        } catch (NoResultException ne) {
            return null;
        }
    }

    @Override
    public Integer findCountByNativeQuery(String sql, Map<String, Object> params) {
        if(!Utils.isOk(sql)) {
            return 0;
        }
        printSQL(sql,params);
        Query query = em.createNativeQuery(sql);
        if(params != null && params.size() >0) {
            params.forEach((k,v)->{
                query.setParameter(k,v);
            });
        }

        return Utils.getIntegerFromObject(query.getSingleResult());
    }


    @Override
    public <T> List<T> findByJpql(String sql,Class<T> entity,Map<String,Object> params,Integer startIndex,Integer limit) {
        if(startIndex == null || startIndex <0) {
            startIndex = 0;
        }
        if(limit ==null || limit<0) {
            limit = 100;
        }
        printSQL(sql,params);
        Query query = em.createQuery(sql,entity);
        if(params != null && params.size() >0) {
            params.forEach((k,v)->query.setParameter(k,v));
        }
        query.setFirstResult(startIndex).setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public <T> T findSingleResultByJpql(String sql,Class<T> entity,Map<String,Object> params) {
        Query query = em.createQuery(sql, entity);
        if(params != null && params.size() >0) {
            params.forEach((k,v)->query.setParameter(k,v));
        }
        try {
            query.setFirstResult(0).setMaxResults(1);
            return (T) query.getResultList().get(0);
        } catch (Throwable t) {
            return null;
        }
    }

    @Override
    public Integer findCountByJpql(String sql, Map<String,Object> params) {
        Query query = em.createQuery(sql);
        if(params != null && !params.isEmpty()) {
            params.forEach((k,v)->query.setParameter(k,v));
        }
        return Utils.getIntegerFromObject(query.getSingleResult());
    }

    private void printSQL(String sql,Map<String,Object> params) {
        if(Defs.WRITE_SQL) {
            LOGGER.info("===RAW SQL:{}",sql);
            if(params != null && params.size()>0) {
                LOGGER.info("==PARAM START==");
                params.forEach((k,v)->LOGGER.info("==KEY:{},VALUE:{}",k,v));
                LOGGER.info("==PARAM END==");
            }

        }
    }
}
