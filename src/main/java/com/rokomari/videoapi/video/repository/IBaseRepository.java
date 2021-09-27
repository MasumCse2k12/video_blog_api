package com.rokomari.videoapi.video.repository;

import java.util.List;
import java.util.Map;

public interface IBaseRepository {

    int updateByNativeQuery(String sql, Map<String, Object> params);
    <T> List<T> findByNativeQuery(String sql, Class<T> entity, Map<String, Object> params, Integer startIndex, Integer limit);
    <T> List<T> findByNativeQuery(String sql, Map<String, Object> params, Integer startIndex, Integer limit);
    <T> List<T> findByNativeQuery(String sql, Map<String, Object> params);
    <T> List<T> findByNativeQuery(String sql, Class<T> entity, Map<String, Object> params);
    <T> T findSingleResultByNativeQuery(String sql, Map<String, Object> params);
    <T> T findSingleResultByNativeQuery(String sql, Class<T> entity, Map<String, Object> params);
    Integer findCountByNativeQuery(String sql, Map<String, Object> param);
    int deleteByIdIn(Class entity, List<Integer> ids);
    <T> List<T> findByJpql(String sql, Class<T> entity, Map<String, Object> params, Integer startIndex, Integer limit);
    <T> T findSingleResultByJpql(String sql, Class<T> entity, Map<String, Object> params);
    Integer findCountByJpql(String sql, Map<String, Object> params);
}
