package com.macauslot.recruitmentadmin.entity.auditor;


import com.macauslot.recruitmentadmin.dto.UserDTO;
import com.macauslot.recruitmentadmin.exception.UserLoginTimeoutException;
import com.macauslot.recruitmentadmin.util.SessionUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Objects;
import java.util.Optional;

@Configuration

public class UserAuditor implements AuditorAware<String> {
    //异步线程修改DB遇到AuditorAware，session为空导致出现问题 的解决方案
    private static ThreadLocal<String> mannualUsername = new ThreadLocal<>();

    public static void setMannualUsername(String username) {
        mannualUsername.set(username);
    }

    public static void clearMannualUsername() {
        mannualUsername.remove();
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        UserDTO userDTO = SessionUtil.getUserFromSession("user");
        String userId;
        if (userDTO != null) {
            userId = userDTO.getEmployeeId();
        } else {
            userId = mannualUsername.get();
        }


        if (userId != null) {
            return Optional.of(userId);
        } else {
            throw new UserLoginTimeoutException("用戶登錄已過期");
//            return Optional.empty();
        }
    }
}

