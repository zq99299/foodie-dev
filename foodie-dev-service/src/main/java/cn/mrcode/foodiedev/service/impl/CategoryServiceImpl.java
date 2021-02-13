package cn.mrcode.foodiedev.service.impl;

import cn.mrcode.foodiedev.mapper.CategoryMapper;
import cn.mrcode.foodiedev.mapper.CategoryMapperCustom;
import cn.mrcode.foodiedev.pojo.Carousel;
import cn.mrcode.foodiedev.pojo.Category;
import cn.mrcode.foodiedev.pojo.vo.CategoryVO;
import cn.mrcode.foodiedev.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author mrcode
 * @date 2021/2/13 22:10
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryMapperCustom categoryMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryAllRootLeveCat() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", 1);
        List<Category> list = categoryMapper.selectByExample(example);
        return list;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCatList(Integer rootCatId) {
        return categoryMapperCustom.getSubCatList(rootCatId);
    }
}
