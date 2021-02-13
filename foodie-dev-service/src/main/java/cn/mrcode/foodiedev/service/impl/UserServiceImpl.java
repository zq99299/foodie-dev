package cn.mrcode.foodiedev.service.impl;

import cn.mrcode.foodiedev.common.enums.Sex;
import cn.mrcode.foodiedev.common.util.DateUtil;
import cn.mrcode.foodiedev.common.util.MD5Utils;
import cn.mrcode.foodiedev.mapper.UsersMapper;
import cn.mrcode.foodiedev.pojo.Users;
import cn.mrcode.foodiedev.pojo.bo.UserBO;
import cn.mrcode.foodiedev.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;
    /**
     * 用户默认的头像图片
     */
    public static final String USER_FACE = "http://localhost:900/xx.jpg";

    /**
     * id 生成器，需要在启动类上使用 @SpringBootApplication(scanBasePackages = {"cn.mrcode.foodiedev", "org.n3r.idworker"}) 扫描
     */
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);

        Users result = usersMapper.selectOneByExample(example);
        return result != null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) {
        Users users = new Users();
        users.setUsername(userBO.getUsername());
        try {
            // 使用自定义工具类对密码进行 MD5 加密
            users.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用 ID 生成器生成 ID
        users.setId(sid.nextShort());
        // 默认同用户昵称一样
        users.setNickname(userBO.getUsername());
        // 设置用户默认头像
        users.setFace(USER_FACE);
        // 默认生日为：1900-01-01
        users.setBirthday(DateUtil.stringToDate("1900-01-01"));
        // 使用 枚举代替数字类型
        users.setSex(Sex.secret.type);
        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());

        usersMapper.insert(users);
        // 返回用户是为了方便前端展示一些信息
        return users;
    }
}
