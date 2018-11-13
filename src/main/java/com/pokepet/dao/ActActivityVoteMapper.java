package com.pokepet.dao;

import org.apache.ibatis.annotations.Param;

import com.pokepet.model.ActActivityVote;

public interface ActActivityVoteMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_activity_vote
     *
     * @mbggenerated Sun Nov 04 13:37:07 GMT+08:00 2018
     */
    int deleteByPrimaryKey(Integer voteId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_activity_vote
     *
     * @mbggenerated Sun Nov 04 13:37:07 GMT+08:00 2018
     */
    int insert(ActActivityVote record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_activity_vote
     *
     * @mbggenerated Sun Nov 04 13:37:07 GMT+08:00 2018
     */
    int insertSelective(ActActivityVote record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_activity_vote
     *
     * @mbggenerated Sun Nov 04 13:37:07 GMT+08:00 2018
     */
    ActActivityVote selectByPrimaryKey(Integer voteId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_activity_vote
     *
     * @mbggenerated Sun Nov 04 13:37:07 GMT+08:00 2018
     */
    int updateByPrimaryKeySelective(ActActivityVote record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_activity_vote
     *
     * @mbggenerated Sun Nov 04 13:37:07 GMT+08:00 2018
     */
    int updateByPrimaryKey(ActActivityVote record);
    
    int getVoteCount(@Param("voterId") String voterId,@Param("registerId") String registerId);
    
    int getVoteCountByActivityIdAndUserIdAndDate(@Param("activityId") String activityId, @Param("voterId") String voterId, @Param("strDate") String strDate);
}