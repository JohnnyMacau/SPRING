package com.school.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/dataTables")
public class DataTablesController {


    private static final Integer SUCCESS = 200;
    private static final Integer FAILURE = 500;
    private static final Logger logger = LoggerFactory.getLogger(DataTablesController.class);




//    @RequestMapping("/planList")
//    public PageInfo<SurveyPlan> planList(
//            HttpServletRequest request,
//            @RequestParam(defaultValue = "0") Integer draw,
//            @RequestParam(defaultValue = "0") Integer start,
//            @RequestParam(defaultValue = "5") Integer length
//    ) {
//        String sidx = request.getParameter("columns[" +
//                request.getParameter("order[0][column]") +
//                "][data]");
//        String sord = request.getParameter("order[0][dir]");
//        List<SurveyPlan> surveyPlanList = surveyPlanRepository.findAllByOrderByStartDateDesc();
//        
//        Pageable pageable = PageRequest.of(start / length, length);
//
//        Page<SurveyPlan> page =  new PageImpl<>(surveyPlanList, pageable, Long.parseLong(""+surveyPlanList.size()));
//        
//        PageInfo<SurveyPlan> p =  PageInfo.convertPage2PageInfo(draw, page);
//        return p;
//    }

//    @RequestMapping("/loadPlanDetail/{plandId}")
//    public ResponseResult<PlanDetailVO> loadPlanDetail( HttpServletRequest request, @PathVariable("plandId") String plandId) {
//
//    	SurveyPlan plan = surveyPlanRepository.getOne(Integer.parseInt(plandId));
//    	List<SurveyDept> deptList = surveyDeptRepository.findBySurveyPlan(plan);
//    	
//    	PlanDetailVO planDetailVO = new PlanDetailVO();
//    	
//    	try {
//			List<DeptVO> depts = new ArrayList<DeptVO>();
//			for(SurveyDept surveyDept:deptList) {
//				DeptVO dept = new DeptVO();
//				dept.setDept(surveyDept.getDeptName());
//				dept.setDeptId(surveyDept.getDeptId());
//				dept.setService_type(surveyDept.getServiceType());
//				dept.setType(surveyDept.getType());
//				
//				List<QuestionGroupVO> groups = new ArrayList<QuestionGroupVO>();
//				List<SurveyQuestionGroup> groupList = surveyQuestionGroupRepository.findBysurveyDept(surveyDept);
//				for(SurveyQuestionGroup surveyQuestionGroup:groupList) {
//					QuestionGroupVO group = new QuestionGroupVO();
//					group.setQuestion_group_id(surveyQuestionGroup.getQuestionGroupId());
//					group.setQuestion_group_cn(surveyQuestionGroup.getQuestionGroupNameCn());
//					group.setQuestion_group_en(surveyQuestionGroup.getQuestionGroupNameEn());
//					group.setQuestion_group_sc(surveyQuestionGroup.getQuestionGroupNameSc());
//					group.setPercentage(surveyQuestionGroup.getPercentage());
//					groups.add(group);
//				}
//				dept.setGroups(groups);	
//				depts.add(dept);
//			}
//			planDetailVO.setPlanId(plan.getPlanId());
//			planDetailVO.setDepts(depts);
//		} catch (Exception e) {
//			logger.info(e.toString(), e);
//			e.printStackTrace();
//
//	    	return new ResponseResult<PlanDetailVO>(FAILURE);
//		}
//    	return new ResponseResult<PlanDetailVO>(SUCCESS, planDetailVO);
//    
//    }

}
