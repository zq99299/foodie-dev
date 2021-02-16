package cn.mrcode.foodiedev.service;


import cn.mrcode.foodiedev.pojo.UserAddress;
import cn.mrcode.foodiedev.pojo.bo.AddressBO;

import java.util.List;

public interface AddressService {

    /**
     * 根据用户 id 查询用户的收货地址列表
     *
     * @param userId
     * @return
     */
    List<UserAddress> queryAll(String userId);

    /**
     * 用户新增地址
     *
     * @param addressBO
     */
    void addNewUserAddress(AddressBO addressBO);

    /**
     * 用户修改地址
     *
     * @param addressBO
     */
    void updateUserAddress(AddressBO addressBO);

    /**
     * 根据用户 id 和地址 id，删除对应的用户地址信息
     *
     * @param userId
     * @param addressId
     */
    void deleteUserAddress(String userId, String addressId);

    /**
     * 修改默认地址
     *
     * @param userId
     * @param addressId
     */
    void updateUserAddressToBeDefault(String userId, String addressId);

    /**
     * 根据用户 id 和地址 id，查询具体的用户地址对象信息
     *
     * @param userId
     * @param addressId
     * @return
     */
    UserAddress queryUserAddres(String userId, String addressId);
}
