package com.school.entity;

public abstract class BaseEntity<T> {

    /**
     * beanCopyExceptNull                空属性不复制
     * beanCopyExceptNullWithIngore      空和指定属性不复制
     */
    public void update(T t) {
    }

    /**
     * beanCopyWithNullAndIgnore         指定属性不复制, 空会复制
     */
    public void update4Sync(T t) {
    }


    /**
     * beanCopyExceptNullWithIngoreAndNotice    空和指定属性不复制, 但空属于被强调的时候复制
     */
    public void update(T t, String... noticeProperties) {
    }

    public String[] getStrings(String... strings) {
        return strings;
    }
}
