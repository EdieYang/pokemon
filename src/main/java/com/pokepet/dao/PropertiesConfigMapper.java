package com.pokepet.dao;

import com.pokepet.model.PropertiesConfig;

import java.util.Map;

public interface PropertiesConfigMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table properties_config
     *
     * @mbggenerated Mon Oct 29 19:58:15 CST 2018
     */
    int deleteByPrimaryKey(Integer confId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table properties_config
     *
     * @mbggenerated Mon Oct 29 19:58:15 CST 2018
     */
    int insert(PropertiesConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table properties_config
     *
     * @mbggenerated Mon Oct 29 19:58:15 CST 2018
     */
    int insertSelective(PropertiesConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table properties_config
     *
     * @mbggenerated Mon Oct 29 19:58:15 CST 2018
     */
    PropertiesConfig selectByPrimaryKey(Integer confId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table properties_config
     *
     * @mbggenerated Mon Oct 29 19:58:15 CST 2018
     */
    int updateByPrimaryKeySelective(PropertiesConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table properties_config
     *
     * @mbggenerated Mon Oct 29 19:58:15 CST 2018
     */
    int updateByPrimaryKey(PropertiesConfig record);


    Map<String,String> getPropertyStatus(Map<String, Object> map);
}