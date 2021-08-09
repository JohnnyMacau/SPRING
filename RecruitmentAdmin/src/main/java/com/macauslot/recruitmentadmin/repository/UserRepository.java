package com.macauslot.recruitmentadmin.repository;


import com.macauslot.recruitmentadmin.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends BaseRepository<User, Integer> {
    @Query(value = "SELECT CASE WHEN password_modify_date IS NULL OR unix_timestamp(password_modify_date) + :expiredate * 60 * 60 * 24 < unix_timestamp(CURRENT_TIMESTAMP) THEN 1 ELSE 0 END expired, ifnull(email, '') email, STATUS, user_name, rdm.`recruitment_dept`, ch_name, ec. NAME, ec.phone, ec.relation_ship, Remark FROM USER LEFT JOIN emergency_contact ec ON USER .id = ec.user_id LEFT JOIN recruitment_dept_map rdm ON USER .`dept_code` = rdm.`hr_portal_dept` WHERE user_name = :username AND PASSWORD = :password", nativeQuery = true)
    List<Object[]> findUserDTO(@Param("username") String username,
                               @Param("password") String password,
                               @Param("expiredate") Integer expiredate);


    User findByUserName(String userName);
    
    @Query(value = "select u from User u, RecruitmentDeptMap m where u.deptCode = m.hrPortalDept and m.recruitmentDept = :deptCode and status ='A'", nativeQuery = false)
    List<User> findByDeptCode(String deptCode);

    List<User> findByUserNameContaining(String userName);

}
