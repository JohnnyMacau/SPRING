package com.macauslot.recruitmentadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 *
 */
@NoRepositoryBean
public interface BaseRepository<T,ID extends Serializable> extends JpaRepository<T,ID>, JpaSpecificationExecutor<T> {
    @Transactional(rollbackFor = Exception.class)
    <S extends T> Iterable<S> batchInsert(Iterable<S> var1);

    @Transactional(rollbackFor = Exception.class)
    <S extends T> Iterable<S> batchUpdate(Iterable<S> var1);


    @Transactional(rollbackFor = Exception.class)
    <S extends T> int batchDelete(Iterable<S> var1);
}
