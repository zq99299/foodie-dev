package cn.mrcode.tcc.service;

import cn.mrcode.tcc.dao.db01.bean.User;
import cn.mrcode.tcc.dao.db01.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mrcode
 * @date 2022/2/10 21:31
 */
@Service
public class UserService {
    @Resource
    public UserMapper userMapper;

    public List<User> getAllUsers() {
        return userMapper.selectAllUsers();
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    public int delUser(Integer userId) {
        // 删除成功，返回 1，删除失败返回 0
        // 这里以删除受影响的行作为结果
        return userMapper.deleteByPrimaryKey(userId);
    }
}
