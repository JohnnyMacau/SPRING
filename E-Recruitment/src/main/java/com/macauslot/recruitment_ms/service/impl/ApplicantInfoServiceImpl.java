package com.macauslot.recruitment_ms.service.impl;

import com.macauslot.recruitment_ms.entity.*;
import com.macauslot.recruitment_ms.exception.*;
import com.macauslot.recruitment_ms.repository.*;
import com.macauslot.recruitment_ms.service.IApplicantInfoService;
import com.macauslot.recruitment_ms.service.IEmailService;
import com.macauslot.recruitment_ms.util.*;
import com.macauslot.recruitment_ms.vo.ApplicantPositionVO;
import com.macauslot.recruitment_ms.vo.JobApplyHistoryVO;
import com.macauslot.recruitment_ms.vo.VisitorSessionDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * @author jim.deng
 */
@Service
public class ApplicantInfoServiceImpl implements IApplicantInfoService {
    private final
    ApplicantInfoRepository applicantInfoRepository;

    private final IEmailService emailService;

    private final
    RelativeInfoRepository relativeInfoRepository;

    private final
    ApplicantPositionRepository applicantPositionRepository;

    private final
    EducationRepository educationRepository;

    private final
    CertificationRepository certificationRepository;

    private final
    WorkingExperienceRepository workingExperienceRepository;

    private final
    LanguageRepository languageRepository;


    private final
    OtherSkillRepository otherSkillRepository;


    private final
    OnShiftRepository onShiftRepository;
    private final ApplicantSourceDetailRepository applicantSourceDetailRepository;

    private final Map<Class<?>, BaseRepository> DB_MAP;


    @Autowired
    public ApplicantInfoServiceImpl(ApplicantInfoRepository applicantInfoRepository, IEmailService emailService, RelativeInfoRepository relativeInfoRepository, ApplicantPositionRepository applicantPositionRepository, EducationRepository educationRepository, CertificationRepository certificationRepository, WorkingExperienceRepository workingExperienceRepository, LanguageRepository languageRepository, OtherSkillRepository otherSkillRepository, OnShiftRepository onShiftRepository, ApplicantSourceDetailRepository applicantSourceDetailRepository) {
        this.applicantInfoRepository = applicantInfoRepository;
        this.emailService = emailService;
        this.relativeInfoRepository = relativeInfoRepository;
        this.applicantPositionRepository = applicantPositionRepository;//
        this.educationRepository = educationRepository;
        this.certificationRepository = certificationRepository;
        this.workingExperienceRepository = workingExperienceRepository;
        this.languageRepository = languageRepository;
        this.otherSkillRepository = otherSkillRepository;
        this.onShiftRepository = onShiftRepository;
        this.applicantSourceDetailRepository = applicantSourceDetailRepository;
        Map<Class<?>, BaseRepository> tmpMap = new HashMap<>();
        tmpMap.put(ApplicantInfo.class, this.applicantInfoRepository);
        tmpMap.put(RelativeInfo.class, this.relativeInfoRepository);
        tmpMap.put(Education.class, this.educationRepository);
        tmpMap.put(Certification.class, this.certificationRepository);
        tmpMap.put(WorkingExperience.class, this.workingExperienceRepository);
        tmpMap.put(Language.class, this.languageRepository);
        tmpMap.put(OtherSkill.class, this.otherSkillRepository);
        tmpMap.put(OnShift.class, this.onShiftRepository);
        tmpMap.put(ApplicantSourceDetail.class, this.applicantSourceDetailRepository);
        tmpMap.put(ApplicantPosition.class, this.applicantPositionRepository);

        DB_MAP = Collections.unmodifiableMap(tmpMap);

    }


    @Override
    public ApplicantInfo login(String username, String password) throws UserNotFoundException, PasswordNotMatchException {
        //????????????username??????????????????
        ApplicantInfo data = findApplicantInfoByUserNameAndApplicationOrg(username);
        //???????????????????????????null
        if (data == null) {
            //?????????null?????????????????????????????????UserNotFoundException??????
            throw new UserNotFoundException("??????????????????????????????????????????" + username + " ?????????!");
        }

        //????????????????????????????????????
        if (!"SLT".equalsIgnoreCase(data.getApplicationOrg())) {
            throw new UserNotFoundException("??????????????????????????????????????????" + username + " ?????????!");
        }


        if ("Y".equals(data.getLock())) {
            Instant now = Instant.now();
            Instant lockTime = data.getLockTime().toInstant();
            long intervalTime = Duration.between(lockTime, now).toMinutes();
            System.err.println("intervalTime---> " + intervalTime);
            if (intervalTime < TimeEnum2.system_lock_time.getNum()) {
                long rmainTime = TimeEnum2.system_lock_time.getNum() - intervalTime;
                throw new UserNotFoundException("????????????????????????,??????" + rmainTime + "??????????????????");
            } else {
                //??????????????????
                data.setTryCount(0);
                data.setLock("N");
            }
        }
        //?????????password????????????
        String MD5password = EncryptUtil.getMd5password(password);
        //????????????????????????
        if (MD5password.equals(data.getPassword())) {
            if (data.getTryCount() != 0) {
                data.setTryCount(0);
            }
//            save(data);//jpa??????????????????????????????.DynamicUpdate?
            dBUpdate(data);
            data.setPassword(null);
            return data;
        } else {
            if (data.getTryTime() == null) {
                data.setTryTime(new Date());
                data.setTryCount(1);
            } else {
                Instant now = Instant.now();
                Instant tryTime = data.getTryTime().toInstant();
                long intervalTime = Duration.between(tryTime, now).toMinutes();
                if (intervalTime > TimeEnum2.system_try_time.getNum() || data.getTryCount() == 0) {
                    data.setTryTime(new Date());
                    data.setTryCount(1);
                } else {
                    data.setTryCount(data.getTryCount() + 1);
                    if (data.getTryCount() >= (int) TimeEnum2.system_try_count.getNum()) {
                        data.setLock("Y");
                        data.setLockTime(new Date());
                    }
                }
            }
//            save(data);
            dBUpdate(data);
            throw new PasswordNotMatchException("????????????,???????????????!");
        }
    }


    @Override
    public ApplicantInfo getById(Integer id) {
        ApplicantInfo data = findById(id);
        data.setPassword(null);
        return data;
    }


    @Override
    public RelativeInfo getRelativeInfoByApplicantInfoId(Integer visitorId) {
        RelativeInfo data = findByApplicantInfoId(visitorId);

        if (data == null) {
            return null;
        }

        data.setRelativeId(null);
        return data;
    }


    @Override

    public ApplicantInfo regTop1(ApplicantInfo applicantInfo, RelativeInfo relativeInfo) throws DuplicateKeyException, InsertException {


        checkDuplicate(applicantInfo);

        String newPassword = getNewPassword();
        String md5password = EncryptUtil.getMd5password(newPassword);
        applicantInfo.setPassword(md5password);
        ApplicantInfo newData = insertApplicantInfo4RollBack(applicantInfo, relativeInfo);


        ApplicantInfo returnData = new ApplicantInfo();
        BeanCopyUtil.beanCopyExceptNull(newData, returnData);

        returnData.setPassword(newPassword);
        return returnData;
    }

    @Override
    public void checkDuplicate(ApplicantInfo applicantInfo) {
        ApplicantInfo data = findApplicantInfoByUserNameAndApplicationOrg(applicantInfo.getUserName());
        ApplicantInfo data2 = findApplicantInfoByIdCardNumberAndApplicationOrg(applicantInfo.getIdCardNumber());

        String tips;
        if ((data != null && "H".equalsIgnoreCase(data.getStatus()))||
                (data2 != null && "H".equalsIgnoreCase(data2.getStatus()))) {
            tips =  "(?????????,????????????????????????)";
        }else {
            tips = "(?????????)";
        }

        if (data != null && data2 != null) {
//            throw new DuplicateKeyException("????????????????????????????????????" + applicantInfo.getUserName() + ", ????????????" + applicantInfo.getIdCardNumber() + "????????????");
            throw new DuplicateKeyException(tips);
        } else if (data != null) {
//            throw new DuplicateUserNameException("????????????????????????????????????" + applicantInfo.getUserName() + "????????????");
            throw new DuplicateUserNameException(tips);
        } else if (data2 != null) {
//            throw new DuplicateIdCardNumException("??????????????????????????????????????????" + applicantInfo.getIdCardNumber() + "????????????");
            throw new DuplicateIdCardNumException(tips);
        }
    }

    //    @Transactional(rollbackFor = Exception.class)
    public ApplicantInfo insertApplicantInfo4RollBack(ApplicantInfo applicantInfo, RelativeInfo relativeInfo) {
        applicantInfo.setIncompletePageNo(7);
        applicantInfo.setStatus("Y");

        ApplicantInfo newData;
        try {
//            newData = save(applicantInfo);
            newData = dBInsert(applicantInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InsertException("???????????????");
        }
        if (relativeInfo != null) {
            relativeInfo.setApplicantInfoId(newData.getApplicantInfoId());
//            saveRelatibeInfo(relativeInfo);
            dBInsert(relativeInfo);
        }
        return newData;
    }

    @Override
    public void changePassword(Integer visitorId, String oldPassword, String newPassword) {
        ApplicantInfo data = findById(visitorId);
//        ??????????????????????????????
        if (!EncryptUtil.getMd5password(oldPassword).equals(data.getPassword())) {
            throw new PasswordNotMatchException("??????????????????????????????");
        }
        data.setPassword(EncryptUtil.getMd5password(newPassword));
        dBUpdate(data);
//        save(data);
    }

    @Override
    public void forgetPassword(String idCardNumber, Integer idTypeId) {
//        ApplicantInfo data = findApplicantInfoByUserNameAndApplicationOrg(emailAddress);
        ApplicantInfo data = findApplicantInfoByIdCardNumberAndApplicationOrgAndIdTypeId(idCardNumber, idTypeId);

        if (data == null) {
            throw new UserNotFoundException("????????????????????????");
        }

        //????????????????????????????????????
        if (!"SLT".equalsIgnoreCase(data.getApplicationOrg())) {
            throw new UserNotFoundException("????????????????????????");
        }

        String newPassword = getNewPassword();
        data.setPassword(newPassword);


        try {
            emailService.sendForgetPwdMail(data);
            data.setPassword(EncryptUtil.getMd5password(newPassword));
            dBUpdate(data);
        }
        catch (EmailException e1) {
            throw new EmailException("email fail!");
        }
        catch (Exception e) {
            throw new EmailException("email success,other reason fail!");
        }

    }

    private String getNewPassword() {
        int i = 1234567890;
        String s = "abcdefghijklmnopqrstuvwxyz";
        String S = s.toLowerCase();
        String word = s + S + i;
        char[] c = word.toCharArray();
        int charArrayLen = c.length;
        Random rd = new Random();//??????????????????????????????????????????
        StringBuilder newPasswordsb = new StringBuilder();
        for (int k = 0; k < 6; k++) {//??????6???
            int index = rd.nextInt(charArrayLen);
            newPasswordsb.append(c[index]);//????????????????????????
        }
        return newPasswordsb.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void updateTop1(Integer visitorId, ApplicantInfo applicantInfo, RelativeInfo relativeInfo) {
        try {
            ApplicantInfo data = findById(visitorId);
//            data.update(applicantInfo);
//            save(data);
            dBUpdate(data, applicantInfo);

        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateException("???????????????");
        }
        RelativeInfo data2 = findByApplicantInfoId(visitorId);
        if (relativeInfo != null) {
            if (data2 != null) {
                dBUpdate(data2, relativeInfo);
            } else {
                relativeInfo.setApplicantInfoId(visitorId);
                dBInsert(relativeInfo);
            }
        } else {
            if (data2 != null) {
                dBDelete(data2);
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)

    public ApplicantPosition updateTop2(Integer visitorId, ApplicantPosition newData) {
//        List<ApplicantPosition> oldData = findByApplicantInfoIdAndJobPriority(visitorId);
        int ingCount = applicantPositionRepository.findAllProcessing(visitorId).size();
//        System.err.println("ingCount:"+ingCount);
        if (ingCount > 3) {
            throw new InsertException("????????????,??????????????????(" + ingCount + ")??????");
        }

        /*int oldDataSize = oldData.size();
        int newDataSize = newData.size();
        for (int i = 0; i < newDataSize; i++) {
            if (i >= oldDataSize) {
                insertApplicantPosition(newData.get(i));
            }
        }
        for (int i = 0; i < oldDataSize; i++) {
            if (i >= newDataSize) {
                deleteApplicantPosition(oldData.get(i));
            } else {
                updateApplicantPosition(oldData.get(i), newData.get(i));
            }
        }*/
        return dBInsert(newData);

//        ApplicantInfo data0 = findById(visitorId);
    /*    //??????????????????-----??? ??????????????????.
        if (data0.getIncompletePageNo() < 3) {
            applicantInfo.setIncompletePageNo(3);
            SessionUtil.setSessionAttribute("incompletePageNo", 3);
        }*/


//        dBUpdate(data0, applicantInfo);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)

    public void updateTop3(Integer visitorId, List<Education> educationList, List<Certification> certificationList) {

        List<Education> data = findEducationByApplicantInfoIdOrderByApplicantInfoId(visitorId);
        List<Certification> data2 = findCertificationByApplicantInfoIdOrderByApplicantInfoId(visitorId);

        dBAutoHandle(data, educationList);
        dBAutoHandle(data2, certificationList);

    /*    ApplicantInfo data0 = findById(visitorId);
        //????????????4???-----??? ???4????????????.
        if (data0.getIncompletePageNo() < 4) {
            data0.setIncompletePageNo(4);
            SessionUtil.setSessionAttribute("incompletePageNo", 4);
//            save(data0);
            dBUpdate(data0);
        }*/


    }


    @Override
    @Transactional(rollbackFor = Exception.class)

    public void updateTop4(Integer visitorId, List<WorkingExperience> workingExperienceList, ApplicantInfo applicantInfo) {
        List<WorkingExperience> data = findWorkingExperienceByApplicantInfoIdOrderByApplicantInfoId(visitorId);

        dBAutoHandle(data, workingExperienceList);

        ApplicantInfo data0 = findById(visitorId);
/*        //????????????5???-----??? ???5????????????.
        if (data0.getIncompletePageNo() < 5) {
            applicantInfo.setIncompletePageNo(5);
            SessionUtil.setSessionAttribute("incompletePageNo", 5);
        }*/
//        data0.update(applicantInfo);
//        save(data0);


        dBUpdate(data0, applicantInfo);

    }

    private void inner_updateTop4(Integer visitorId, List<WorkingExperience> workingExperienceList) {
        List<WorkingExperience> data = findWorkingExperienceByApplicantInfoIdOrderByApplicantInfoId(visitorId);
        dBAutoHandle(data, workingExperienceList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTop5(Integer visitorId, List<Language> languageList, List<OtherSkill> otherSkillList) {
        List<Language> data = findLanguageByApplicantInfoIdOrderByApplicantInfoId(visitorId);

        List<OtherSkill> data2 = findOtherSkillByApplicantInfoIdOrderByApplicantInfoId(visitorId);

        dBAutoHandle(data, languageList);
        dBAutoHandle(data2, otherSkillList);

        ApplicantInfo data0 = findById(visitorId);
        //2021???2???2???16:15:44 ??????HR???????????????????????????????????????
        //2021???3???9???16:47:15 ???????????????????????????????????????????????????Y
//        if ("H".equalsIgnoreCase(data0.getStatus())||"O".equalsIgnoreCase(data0.getStatus())) {
            data0.setIncompletePageNo(7);
            data0.setStatus("Y");
//        }
        dBUpdate(data0);

/*        //????????????6???-----??? ???6????????????.
        if (data0.getIncompletePageNo() < 6) {
            data0.setIncompletePageNo(6);
            SessionUtil.setSessionAttribute("incompletePageNo", 6);
//            save(data0);
            dBUpdate(data0);

        }*/
/*        //????????????7???-----??? ??????????????????.
        if (data0.getIncompletePageNo() < 7) {
            data0.setIncompletePageNo(7);
            data0.setStatus("Y");
            SessionUtil.setSessionAttribute("incompletePageNo", 7);
//            save(data0);
            dBUpdate(data0);

        }*/
    }



    private void inner_updateTop5(Integer visitorId, List<Language> languageList, List<OtherSkill> otherSkillList) {
        List<Language> data = findLanguageByApplicantInfoIdOrderByApplicantInfoId(visitorId);
        List<OtherSkill> data2 = findOtherSkillByApplicantInfoIdOrderByApplicantInfoId(visitorId);
        dBAutoHandle(data, languageList);
        dBAutoHandle(data2, otherSkillList);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)

    public void updateTop6(Integer visitorId, Integer applicantPositionId, List<OnShift> onShiftList) {

//        List<OnShift> data = findOnShiftByApplicantInfoId(visitorId);
        List<OnShift> data = onShiftRepository.findOnShiftByApplicantInfoIdAndApplicantPositionId(visitorId, applicantPositionId);
        dBAutoHandle(data, onShiftList);

    /*    ApplicantInfo data0 = findById(visitorId);
        //????????????7???-----??? ??????????????????.
        if (data0.getIncompletePageNo() < 7) {
            data0.setIncompletePageNo(7);
            data0.setStatus("Y");
            SessionUtil.setSessionAttribute("incompletePageNo", 7);
//            save(data0);
            dBUpdate(data0);

        }*/

    }


    @Override
    public List<WorkingExperience> getWorkingExperienceByApplicantInfoId(Integer visitorId) {
        return findWorkingExperienceByApplicantInfoIdOrderByApplicantInfoId(visitorId);
    }

    @Override
    public List<Education> getEducationByApplicantInfoId(Integer visitorId) {
        return findEducationByApplicantInfoIdOrderByApplicantInfoId(visitorId);

    }

    @Override
    public List<Certification> getCertificationByApplicantInfoId(Integer visitorId) {
        return findCertificationByApplicantInfoIdOrderByApplicantInfoId(visitorId);
    }


    @Override
    public List<Language> getLanguageByApplicantInfoId(Integer visitorId) {
        return findLanguageByApplicantInfoIdOrderByApplicantInfoId(visitorId);
    }

    @Override
    public List<OtherSkill> getOtherSkillByApplicantInfoId(Integer visitorId) {
        return findOtherSkillByApplicantInfoIdOrderByApplicantInfoId(visitorId);
    }


    @Override
    public List<OnShift> getOnShiftByApplicantInfoId(Integer visitorId) {
        return findOnShiftByApplicantInfoId(visitorId);
    }

    @Override
    public List<ApplicantPositionVO> getHistoryApplicantPositionByApplicantInfoId(Integer visitorId) {
        return findHistoryByApplicantInfoId(visitorId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApplicantInfo insertVistorSessionData(VisitorSessionDataVO visitorSessionDataVO) {
        ApplicantInfo applicantInfo = visitorSessionDataVO.getApplicantInfo();
        RelativeInfo relativeInfo = visitorSessionDataVO.getRelativeInfo();
        List<Education> educationList = visitorSessionDataVO.getEducationList();
        List<Certification> certificationList = visitorSessionDataVO.getCertificationList();
        List<WorkingExperience> workingExperienceList = visitorSessionDataVO.getWorkingExperienceList();
        List<Language> languageList = visitorSessionDataVO.getLanguageList();
        List<OtherSkill> otherSkillList = visitorSessionDataVO.getOtherSkillList();

        ApplicantInfo dbResult = regTop1(applicantInfo, relativeInfo);
        Integer visitorId = dbResult.getApplicantInfoId();

        for (Education education : educationList) {
            education.setApplicantInfoId(visitorId);
        }
        for (Certification certification : certificationList) {
            certification.setApplicantInfoId(visitorId);
        }

        updateTop3(visitorId, educationList, certificationList);

        for (WorkingExperience workingExperience : workingExperienceList) {
            workingExperience.setApplicantInfoId(visitorId);
        }

        inner_updateTop4(visitorId, workingExperienceList);

        for (Language language : languageList) {
            language.setApplicantInfoId(visitorId);
        }
        for (OtherSkill otherSkill : otherSkillList) {
            otherSkill.setApplicantInfoId(visitorId);
        }
        inner_updateTop5(visitorId, languageList, otherSkillList);


        //        emailService.sendTemplateMailByAsynchronousMode(newData, this::dBUpdate);//?????????ok???? ?????????! ????????????????????????
        /*save????????????????????????????????????????????????????????????????????????????????????id?????????????????????id???0
        ??????GenerateValue???????????????hibernate??????????????????????????????
        ?????????????????????????????????hibernate???????????????id????????????????????????
        ????????????????????????????????????????????????id??????????????????????????????id*/
        try {
            emailService.sendTemplateMailByAsynchronousMode(dbResult, sendResult -> {
                        System.err.println(Thread.currentThread().getName() + "???????????????: " + sendResult.getEmailStatus());
                        try {
                            ApplicantInfo dataSecond = findById(dbResult.getApplicantInfoId());
                            dataSecond.setEmailStatus(sendResult.getEmailStatus());
                            //                        save(dataSecond);
                            dBUpdate(dataSecond);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new InsertException("???????????????");
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("---- emailService.sendTemplateMailByAsynchronousMode error ----");
        }

        return dbResult;

    }



    @Override
    public boolean checkOldData(Integer visitorId){
        ApplicantInfo applicantInfo = findById(visitorId);
        return "O".equalsIgnoreCase(applicantInfo.getStatus());
    }




    @Override
    public List<ApplicantSourceDetail> getApplicantSourceDetailListByMessageId(Integer messageId) {
        return applicantSourceDetailRepository.findAllByMessageIdAndStatusOrderByOrderNum(messageId, "A");
    }

    @Override
    public List<ApplicantPosition> getAllByApplicantInfoIdAndProcessStage(Integer applicantInfoId) {
        return applicantPositionRepository.findAllProcessing(applicantInfoId);
    }

    @Override
    public List<JobApplyHistoryVO> getJobApplyHistory(Integer applicantId) {
        List<JobApplyHistoryVO> jobApplyHistoryVOList = EntityUtils.castEntity(applicantPositionRepository.findJobApplyHistory(applicantId), JobApplyHistoryVO.class);
        for (JobApplyHistoryVO jobApplyHistoryVO : jobApplyHistoryVOList) {
            String process_stage = jobApplyHistoryVO.getProcess_stage();
           /* if (process_stage.compareTo("11")==0||process_stage.compareTo("12")==0) {
                jobApplyHistoryVO.setProcess_stage("?????????");
            } else if ("ed".equalsIgnoreCase(process_stage)) {
                jobApplyHistoryVO.setProcess_stage("?????????");
            }*/
            for (FlowEnum flowEnum : FlowEnum.values()) {
                if (flowEnum.getCode().equals(process_stage)) {
                    jobApplyHistoryVO.setProcess_stage(flowEnum.getBriefStatus());
                }
            }

        }
        return jobApplyHistoryVOList;
    }


    @Transactional(rollbackFor = Exception.class)
    public <T> void dBAutoHandle(List<T> oldData, List<T> newData) {
        int oldDataSize = oldData.size();
        int newDataSize = newData.size();
        for (int i = 0; i < newDataSize; i++) {
            if (i >= oldDataSize) {
                dBInsert(newData.get(i));
            }
        }
        for (int i = 0; i < oldDataSize; i++) {
            if (i >= newDataSize) {
                dBDelete(oldData.get(i));
            } else {
                dBUpdate(oldData.get(i), newData.get(i));
            }
        }
    }


    private <T> T dBInsert(T data) {
        try {
            BaseRepository<T, Integer> baseRepository = DB_MAP.get(data.getClass());
            return baseRepository.save(data);
        } catch (Exception e) {
            throw new InsertException("??????????????????,??????????????????");
        }
    }

    private <T> void dBDelete(T data) {
        try {
            BaseRepository baseRepository = DB_MAP.get(data.getClass());
            baseRepository.delete(data);
        } catch (Exception e) {
            throw new DeleteException("??????????????????");
        }
    }

    private <T> Object dBUpdate(T data) {
        return dBInsert(data);
    }

    private <T> Object dBUpdate(T oldData, T newData) {
        try {
            BaseRepository baseRepository = DB_MAP.get(oldData.getClass());
            ((BaseEntity4Update<T>) oldData).update(newData);
            return baseRepository.save(oldData);
        } catch (Exception e) {
            throw new UpdateException("??????????????????,??????????????????");
        }
    }


    /*private void insertApplicantPosition(ApplicantPosition a1) {
        try {
            applicantPositionRepository.insert(a1.getApplicantInfoId(), a1.getDeptPosDetailId(), a1.getJobPriority());
        } catch (Exception e) {
            throw new InsertException("??????????????????,??????????????????");
        }
    }

    private void updateApplicantPosition(ApplicantPosition old_data, ApplicantPosition new_data) {
        if (old_data.getDeptPosDetailId().equals(new_data.getDeptPosDetailId())) {
            return;
        }
//        System.err.println("old-----" + old_data + "\n new~~~~ " + new_data);
        try {
            applicantPositionRepository.update(new_data.getApplicantInfoId(), new_data.getDeptPosDetailId(), old_data.getJobPriority());
        } catch (Exception e) {
            throw new InsertException("??????????????????,??????????????????");
        }
    }

    private void deleteApplicantPosition(ApplicantPosition a1) {
        try {
            applicantPositionRepository.delete(a1.getApplicantInfoId(), a1.getJobPriority());
        } catch (Exception e) {
            throw new InsertException("??????????????????,??????????????????");
        }
    }*/

    private List<OnShift> findOnShiftByApplicantInfoId(Integer visitorId) {
        return onShiftRepository.findOnShiftByApplicantInfoId(visitorId);
    }


    private List<Language> findLanguageByApplicantInfoIdOrderByApplicantInfoId(Integer visitorId) {
        return languageRepository.findLanguageByApplicantInfoIdOrderByApplicantInfoId(visitorId);
    }


    private List<OtherSkill> findOtherSkillByApplicantInfoIdOrderByApplicantInfoId(Integer visitorId) {
        return otherSkillRepository.findOtherSkillByApplicantInfoIdOrderByApplicantInfoId(visitorId);

    }


    private List<WorkingExperience> findWorkingExperienceByApplicantInfoIdOrderByApplicantInfoId(Integer visitorId) {
        return workingExperienceRepository.findWorkingExperienceByApplicantInfoIdOrderByApplicantInfoId(visitorId);
    }


    private List<Education> findEducationByApplicantInfoIdOrderByApplicantInfoId(Integer visitorId) {
        return educationRepository.findEducationByApplicantInfoIdOrderByApplicantInfoId(visitorId);
    }

    private List<Certification> findCertificationByApplicantInfoIdOrderByApplicantInfoId(Integer visitorId) {
        return certificationRepository.findCertificationByApplicantInfoIdOrderByApplicantInfoId(visitorId);
    }


    //    private List<ApplicantPositionVO> findHistoryByJobPriorityAndApplicantInfoId(Integer jobPriority, Integer visitorId) {
//        return EntityUtils.castEntity(applicantPositionRepository.findHistoryByJobPriorityAndApplicantInfoId(jobPriority, visitorId), ApplicantPositionVO.class);
//    }
    private List<ApplicantPositionVO> findHistoryByApplicantInfoId(Integer visitorId) {
        return EntityUtils.castEntity(applicantPositionRepository.findHistoryByApplicantInfoId(visitorId), ApplicantPositionVO.class);
    }

    /*  private ApplicantPosition findByApplicantInfoIdAndJobPriority(Integer visitorId, int jobPriority) {
          try {
              List<ApplicantPosition> positionVOS = EntityUtils.castEntity(applicantPositionRepository.findByApplicantInfoIdAndJobPriority(visitorId, jobPriority), ApplicantPosition.class);
              if (!positionVOS.isEmpty()) {
                  return positionVOS.get(0);
              } else {
                  return null;
              }
          } catch (Exception e) {
              throw new DuplicateKeyException("??????????????????");
          }
      }*/
    private List<ApplicantPosition> findByApplicantInfoIdAndJobPriority(Integer visitorId) {
        return EntityUtils.castEntity(applicantPositionRepository.findByApplicantInfoIdAndJobPriority(visitorId), ApplicantPosition.class);
    }

    private ApplicantInfo findById(Integer id) {
        Optional<ApplicantInfo> applicantInfo = applicantInfoRepository.findById(id);
        return applicantInfo.orElseThrow(UserNotFoundException::new);
    }


    private ApplicantInfo findApplicantInfoByUserNameAndApplicationOrg(String userName) {
        try {
            return applicantInfoRepository.findApplicantInfoByUserNameAndApplicationOrg(userName,"SLT");
        } catch (Exception e) {
            throw new DuplicateKeyException("??????????????????");
        }
    }

    private ApplicantInfo findApplicantInfoByIdCardNumberAndApplicationOrg(String idCardNumber) {
        try {
            return applicantInfoRepository.findApplicantInfoByIdCardNumberAndApplicationOrg(idCardNumber,"SLT");
        } catch (Exception e) {
            throw new DuplicateKeyException("??????????????????");
        }
    }

    private ApplicantInfo findApplicantInfoByIdCardNumberAndApplicationOrgAndIdTypeId(String idCardNumber, Integer idTypeId) {
        try {
            return applicantInfoRepository.findApplicantInfoByIdCardNumberAndApplicationOrgAndIdTypeId(idCardNumber,"SLT", idTypeId);
        } catch (Exception e) {
            throw new DuplicateKeyException("??????????????????");
        }
    }

    private RelativeInfo findByApplicantInfoId(Integer applicantInfoId) {
        try {
            return relativeInfoRepository.findByApplicantInfoId(applicantInfoId);
        } catch (Exception e) {
            throw new DuplicateKeyException("??????????????????");
        }
    }


}
