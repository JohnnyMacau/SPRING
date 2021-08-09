package com.macauslot.recruitmentadmin.service.impl;

import com.macauslot.recruitmentadmin.dto.UserPO;
import com.macauslot.recruitmentadmin.exception.UserDataMissingException;
import com.macauslot.recruitmentadmin.exception.UserOrPasswordNotMatchException;
import com.macauslot.recruitmentadmin.repository.UserPermissionRepository;
import com.macauslot.recruitmentadmin.repository.UserRepository;
import com.macauslot.recruitmentadmin.service.IUserService;
import com.macauslot.recruitmentadmin.util.EncryptUtil;
import com.macauslot.recruitmentadmin.util.EntityUtils;
import com.macauslot.recruitmentadmin.vo.LoginUserVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    private final
    UserRepository userRepository;
    private final UserPermissionRepository userPermissionRepository;
    @Value("${password_expire_day}")
    private String expiredate;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,UserPermissionRepository userPermissionRepository) {
        this.userRepository = userRepository;
        this.userPermissionRepository = userPermissionRepository;
    }

    @Override
    public UserPO login(String username, String password) throws
            UserOrPasswordNotMatchException,
            UserDataMissingException {
        String MD5password = EncryptUtil.getMd5password(password);

        List<UserPO> list = getUser(username, MD5password);
        if (list.size() == 0) {
            throw new UserOrPasswordNotMatchException("用戶或密碼不正確");
        }
        List<LoginUserVO> listLoginUserVO = getLoginUser(username);
        if("0".equals(listLoginUserVO.get(0).getRecruimentAdminUser())) {
        	throw new UserOrPasswordNotMatchException("沒有權限登陸，請聯繫管理員分配權限");
        }
        UserPO user = list.get(0);
        if (!"A".equalsIgnoreCase(String.valueOf(user.getStatus()))) {
            throw new UserDataMissingException("StatusNotActive");
        } else if ("1".equals(String.valueOf(user.getExpired()))) {
            throw new UserDataMissingException("PasswordExpired");
        }
//        else if (StringUtils.isBlank(user.getEmail())) {
//            throw new UserDataMissingException("NoEmail");
//        }
//        else if (StringUtils.isBlank(user.getRelativeName()) ||
//                StringUtils.isBlank(user.getPhone()) ||
//                StringUtils.isBlank(user.getRelationShip())) {
//            throw new UserDataMissingException("NoEmergencyContact");
//        }
        else if (StringUtils.isBlank(user.getDeptCode())) {
            throw new UserDataMissingException("NoDeptCode");
        } else {
            return user.convertToVO();
        }
    }


    private List<UserPO> getUser(String username, String MD5password) {
        return EntityUtils.castEntity(
                userRepository.findUserDTO(username, MD5password, Integer.parseInt(expiredate)), UserPO.class);
    }
    private List<LoginUserVO> getLoginUser(String username){
    	return userPermissionRepository.findLoginUserVO(username);
    }
}
