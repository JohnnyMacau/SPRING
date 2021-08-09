package com.macauslot.recruitmentadmin.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class PageInfo<T> implements Serializable {
    private int draw; //回传draw
    private int recordsTotal; //记录总数
    private int recordsFiltered; //被过滤记录数
    private List<T> data; //数据
    private String error; //错误信息

    public static <t> PageInfo<t> convertPage2PageInfo(Integer draw, Page<t> page) {
        PageInfo<t> pageInfo = new PageInfo<>();
        pageInfo.setData(page.getContent());
        pageInfo.setDraw(draw);
        int total = Integer.parseInt(String.valueOf(page.getTotalElements()));
        pageInfo.setRecordsTotal(total);
        pageInfo.setRecordsFiltered(total);
        pageInfo.setError("");
        return pageInfo;
    }

    public static <t> PageInfo<t> convertList2PageInfo(Integer draw, List<t> list) {
        PageInfo<t> pageInfo = new PageInfo<>();
        pageInfo.setData(list);
        pageInfo.setDraw(draw);
        int total = list.size();
        pageInfo.setRecordsTotal(total);
        pageInfo.setRecordsFiltered(total);
        pageInfo.setError("");
        return pageInfo;
    }
}

