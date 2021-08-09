package com.macauslot.recruitment_ms;

import com.macauslot.recruitment_ms.entity.Education;
import com.macauslot.recruitment_ms.repository.*;
import com.macauslot.recruitment_ms.util.EntityUtils;
import com.macauslot.recruitment_ms.util.TimeEnum;
import com.macauslot.recruitment_ms.vo.DeptPositionDescVO;
import com.macauslot.recruitment_ms.vo.DeptPositionDetailVO;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    ApplicantInfoRepository applicantInfoRepository;

    @Autowired
    ApplicantPositionRepository applicantPositionRepository;

    @Autowired
    RelativeInfoRepository relativeInfoRepository;
    @Autowired
    EducationRepository educationRepository;

    @Test
    public void test1() {
        Date date = new Date();
//        String dateStr1 = DateFormatUtils.format(date, TimeEnum.YYYY_MM_DD_HH_MM_SS.getName());
        String dateStr2 = DateFormatUtils.format(date, TimeEnum.YYYY_MM_DD.getName());
        List<Object[]> arr = departmentRepository.findDepartmentPositionDetail(dateStr2);
        List<DeptPositionDetailVO> p = EntityUtils.castEntity(arr, DeptPositionDetailVO.class);
        System.err.println(p.toString());

    }


    @Test
    public void test2() {
        Integer i = 10635;
        List<Object[]> arr = departmentRepository.findDepartmentPositionDescription(i);
        List<DeptPositionDescVO> p = EntityUtils.castEntity(arr, DeptPositionDescVO.class);
        System.err.println(p.size());
    }


    @Test
    public void test3() {
        Integer i = 7723;
        System.err.println("7777222" + applicantInfoRepository.findById(i));

    }

    @Test
    public void test4() {

        System.err.println("8888" + relativeInfoRepository.findByApplicantInfoId(7723));

    }

    @Test
    public void test5() {

 }

    @Test
    public void test6() {
        List<Education> data = educationRepository.findEducationByApplicantInfoIdOrderByApplicantInfoId(14454);
        System.err.println(data);

    }


}
