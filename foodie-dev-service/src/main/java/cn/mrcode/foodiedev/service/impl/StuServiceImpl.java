package cn.mrcode.foodiedev.service.impl;

import cn.mrcode.foodiedev.mapper.StuMapper;
import cn.mrcode.foodiedev.pojo.Stu;
import cn.mrcode.foodiedev.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class StuServiceImpl implements StuService {
    @Autowired
    private StuMapper stuMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Stu getStuInfo(int id) {
        return stuMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveStu() {
        Stu record = new Stu();
        record.setName("jack");
        record.setAge(19);
        stuMapper.insert(record);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateStu(int id) {
        Stu record = new Stu();
        record.setId(id);
        record.setName("jack-update");
        record.setAge(50);
        stuMapper.updateByPrimaryKey(record);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteStu(int id) {
        stuMapper.deleteByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveParent() {
        Stu record = new Stu();
        record.setName("jack-1");
        record.setAge(19);
        stuMapper.insert(record);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveChildren() {
        Stu record = new Stu();
        record.setName("child-1");
        record.setAge(19);
        stuMapper.insert(record);
//        int a = 1 / 0;
        record = new Stu();
        record.setName("child-2");
        record.setAge(19);
        stuMapper.insert(record);
    }
}
