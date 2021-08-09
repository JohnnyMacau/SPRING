package com.macauslot.recruitmentadmin.service;

import com.macauslot.recruitmentadmin.repository.BaseRepository;
import com.macauslot.recruitmentadmin.entity.BaseEntity;
import com.macauslot.recruitmentadmin.util.EntityUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public abstract class AbstractBaseService {
    protected Map<Class<? extends BaseEntity>, BaseRepository> CRUD_DB_MAP;
    protected Map<Class<?>, BaseRepository> READ_DB_MAP;

    protected <T extends BaseEntity<T>> void dbAutohandle(List<T> oldDataList, List<T> newDataList) {

        int oldDataSize = oldDataList.size();
        int newDataSize = newDataList.size();

        if (oldDataSize == 0 && newDataSize == 0) {
            return;
        }

        BaseRepository<T, Integer> b = getBaseRepository(oldDataList, newDataList);


        try {
            for (int i = 0; i < newDataSize; i++) {
                if (i >= oldDataSize) {
                    dbInsert(b, newDataList.get(i));
                }
            }
            for (int i = 0; i < oldDataSize; i++) {
                if (i >= newDataSize) {
                    dbDelete(b, oldDataList.get(i));
                } else {
                    dbUpdate(b, oldDataList.get(i), newDataList.get(i));
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("dbAutohandle error");
        }


    }

    protected <T extends BaseEntity<T>> void dbAutohandle4Synching(List<T> oldDataList, List<T> newDataList) {
        int oldDataSize = oldDataList.size();
        int newDataSize = newDataList.size();


        BaseRepository<T, Integer> b = getBaseRepository(oldDataList, newDataList);


        try {
            for (int i = 0; i < newDataSize; i++) {
                if (i >= oldDataSize) {
                    dbInsert(b, newDataList.get(i));
                }
            }
            for (int i = 0; i < oldDataSize; i++) {
                T oldData = oldDataList.get(i);
                if (i >= newDataSize) {
                    dbDelete(b, oldData);
                } else {
                    T newData = newDataList.get(i);
                    if (!newData.equals(oldData)) {
                        System.err.println("dbUpdate---\n old: \n" + oldData);
                        dbUpdate4Sync(b, oldData, newData);
                        System.err.println(" new: \n" + newData);
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("dbAutohandle4Synching error");
        }
    }


    protected <T extends BaseEntity<T>> void dbAutohandleWithOutDelete(List<T> oldDataList, List<T> newDataList) {
        int oldDataSize = oldDataList.size();
        int newDataSize = newDataList.size();

        BaseRepository<T, Integer> b = getBaseRepository(oldDataList, newDataList);


        try {
            for (int i = 0; i < newDataSize; i++) {
                if (i >= oldDataSize) {
                    dbInsert(b, newDataList.get(i));
                }
            }
            for (int i = 0; i < oldDataSize; i++) {
                if (i >= newDataSize) {
                    throw new IllegalArgumentException("異常的刪除命令");
                } else {
                    dbUpdate(b, oldDataList.get(i), newDataList.get(i));
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("dbAutohandleWithOutDelete error");
        }
    }


    /**
     * data没有get(0)导致npe
     *
     * @param dataList
     * @param <T>
     */
    protected <T extends BaseEntity<T>> List<T> dbBatchInsert(List<T> dataList) {
        if (dataList.isEmpty()) {
            throw new IllegalArgumentException("输入数组为空--InsertException");
        }
        try {
            BaseRepository<T, Integer> baseRepository = getBaseRepository(dataList.get(0));
            return baseRepository.saveAll(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("输入内容过长,更新数据失败--InsertException");
        }
    }


    protected <T extends BaseEntity<T>> Iterable<T> emDbBatchInsert(List<T> dataList) {
        if (dataList.isEmpty()) {
            throw new IllegalArgumentException("输入数组为空--InsertException");
        }
        try {
            BaseRepository<T, Integer> baseRepository = getBaseRepository(dataList.get(0));
            return baseRepository.batchInsert(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("输入内容过长,更新数据失败--InsertException");
        }
    }


    protected <T extends BaseEntity<T>> T dbInsert(T data) {
        try {
            BaseRepository<T, Integer> baseRepository = getBaseRepository(data);
            return baseRepository.save(data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("输入内容过长,更新数据失败--InsertException");
        }
    }

    private <T extends BaseEntity<T>> T dbInsert(BaseRepository<T, Integer> b, T data) {
        return b.save(data);
    }


    protected <T extends BaseEntity<T>> void dbDelete(T data) {
        try {
            BaseRepository<T, Integer> baseRepository = getBaseRepository(data);
            baseRepository.delete(data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("更新数据失败");
        }
    }

    private <T extends BaseEntity<T>> void dbDelete(BaseRepository<T, Integer> b, T data) {
        b.delete(data);
    }

    protected <T extends BaseEntity<T>> T dbUpdate(T data) {
        return dbInsert(data);
    }

    protected <T extends BaseEntity<T>> T dbUpdate(T oldData, T newData) {
        try {
            BaseRepository<T, Integer> baseRepository = getBaseRepository(oldData);
            oldData.update(newData);
            return baseRepository.save(oldData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("输入内容过长,更新数据失败--UpdateException");
        }
    }

    private <T extends BaseEntity<T>> T dbUpdate(BaseRepository<T, Integer> b, T oldData, T newData) {
        oldData.update(newData);
        return b.save(oldData);
    }

  /*外部并没有用上  protected <T extends BaseEntity<T>> T dbUpdate4Sync(T oldData, T newData) {
        try {
            BaseRepository<T, Integer> baseRepository = getBaseRepository(oldData);
            oldData.update4Sync(newData);
            return baseRepository.save(oldData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("输入内容过长,更新数据失败--UpdateException");
        }
    }*/

    private <T extends BaseEntity<T>> T dbUpdate4Sync(BaseRepository<T, Integer> b, T oldData, T newData) {
        oldData.update4Sync(newData);
        return b.save(oldData);
    }


    protected <T extends BaseEntity<T>> void updateErpData(List<T> dataList) {
        if (dataList.isEmpty()) {
            return;
        }
        BaseRepository<T, Integer> baseRepository = getBaseRepository(dataList.get(0));
        baseRepository.deleteAll();
//        for (T data : dataList) {
//            baseRepository.save(data);
//        }
        emDbBatchInsert(dataList);
    }

    @Transactional(rollbackFor = Exception.class)
    public <T extends BaseEntity<T>> void erpDataSynching(List<Object[]> remoteDbDataList, Class<T> clazz) {
        BaseRepository<T, Integer> baseRepository = getBaseRepository(clazz);
        List<T> newDataList = EntityUtils.castEntity(remoteDbDataList, clazz);
     /*
     List<T> newDataList = new array
       Date lastUpdateTime = new Date();
      for (Object[] oldData : remoteDbDataList) {
            T newData = clazz.newInstance();

            List<String> fieldList = Common.getBeanFields(newData);
            int diff = 0;
            for (int i = 0; i < fieldList.size(); i++) {
                String field = fieldList.get(i);
                if (field.compareTo("id") == 0 || field.compareTo("lastUpdateTime") == 0) {
                    diff++;
                    continue;
                }
                String setFunctionName = field;
                setFunctionName = "set" + setFunctionName.substring(0, 1).toUpperCase() + setFunctionName.substring(1);
                String fieldValue = Common.objectToString(oldData[i - diff]);

                Common.invokeMethod(newData, setFunctionName, new String[]{fieldValue});
            }

            Common.invokeMethod(newData, "setLastUpdateTime", new Date[]{lastUpdateTime});
            newDataList.add(newData);
        }*/
        List<T> oldDataList = baseRepository.findAll();
        if (newDataList.equals(oldDataList)) {
//            logger.info("No update from erp view.");
            System.err.println("No update from erp view [ " + clazz.getName() + " ].");
        } else {
//            logger.info("erp view updated.");
            System.err.println("erp view updated [ " + clazz.getName() + " ].");
            // fun1
//            updateErpData(newDataList);
            // fun2
            dbAutohandle4Synching(oldDataList, newDataList);
        }
    }


    private <T extends BaseEntity<T>> BaseRepository<T, Integer> getBaseRepository(Class<? extends BaseEntity> clazz) {//(Class<T> clazz)
        BaseRepository<T, Integer> baseRepository = CRUD_DB_MAP.get(clazz);
        if (baseRepository == null) {
            if (clazz == null) {
                throw new IllegalArgumentException("parameter(Class<? extends BaseEntity> clazz) is null , npe error");
            } else {
                throw new IllegalArgumentException("CRUD_DB_MAP is not map this clazz (" + clazz.getName() +
                        "), npe error");
            }
        }
        return baseRepository;
    }


    private <T extends BaseEntity<T>> BaseRepository<T, Integer> getBaseRepository(T data) {
        if (data == null) {
            throw new IllegalArgumentException("parameter(T data) is null , npe error");
        }
       /* Class<? extends BaseEntity> clazz = data.getClass();
        BaseRepository<T, Integer> baseRepository = CRUD_DB_MAP.get(clazz);
        if (baseRepository == null) {
            if (clazz == null) {
                throw new IllegalArgumentException("parameter(Class<T> clazz) is null , npe error");
            } else {
                throw new IllegalArgumentException("CRUD_DB_MAP is not map this clazz (" + clazz.getName() +
                        "), npe error");
            }
        }*/
        return getBaseRepository(data.getClass());

    }


    private <T extends BaseEntity<T>> BaseRepository<T, Integer> getBaseRepository(List<T> oldDataList, List<T> newDataList) {
        BaseRepository<T, Integer> b;
        if (!oldDataList.isEmpty()) {
            b = getBaseRepository(oldDataList.get(0));
        } else if (!newDataList.isEmpty()) {
            b = getBaseRepository(newDataList.get(0));
        } else {
            throw new IllegalArgumentException("Both oldDataList and newDataList is Empty , cannot get BaseRepository");
        }
        return b;
    }
}
