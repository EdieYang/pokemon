package com.pokepet.model;

import java.util.Date;

public class UserDonate {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_donate.id
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_donate.donate_activity
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    private String donateActivity;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_donate.user_id
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    private String userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_donate.donate_type
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    private String donateType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_donate.donate_time
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    private Date donateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_donate.donate_amount
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    private Double donateAmount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_donate.del_flag
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    private String delFlag;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_donate.id
     *
     * @return the value of user_donate.id
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_donate.id
     *
     * @param id the value for user_donate.id
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_donate.donate_activity
     *
     * @return the value of user_donate.donate_activity
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    public String getDonateActivity() {
        return donateActivity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_donate.donate_activity
     *
     * @param donateActivity the value for user_donate.donate_activity
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    public void setDonateActivity(String donateActivity) {
        this.donateActivity = donateActivity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_donate.user_id
     *
     * @return the value of user_donate.user_id
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_donate.user_id
     *
     * @param userId the value for user_donate.user_id
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_donate.donate_type
     *
     * @return the value of user_donate.donate_type
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    public String getDonateType() {
        return donateType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_donate.donate_type
     *
     * @param donateType the value for user_donate.donate_type
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    public void setDonateType(String donateType) {
        this.donateType = donateType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_donate.donate_time
     *
     * @return the value of user_donate.donate_time
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    public Date getDonateTime() {
        return donateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_donate.donate_time
     *
     * @param donateTime the value for user_donate.donate_time
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    public void setDonateTime(Date donateTime) {
        this.donateTime = donateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_donate.donate_amount
     *
     * @return the value of user_donate.donate_amount
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    public Double getDonateAmount() {
        return donateAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_donate.donate_amount
     *
     * @param donateAmount the value for user_donate.donate_amount
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    public void setDonateAmount(Double donateAmount) {
        this.donateAmount = donateAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_donate.del_flag
     *
     * @return the value of user_donate.del_flag
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    public String getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_donate.del_flag
     *
     * @param delFlag the value for user_donate.del_flag
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}