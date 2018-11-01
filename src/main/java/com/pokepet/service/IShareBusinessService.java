package com.pokepet.service;

import com.pokepet.model.ShareRecord;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Fade on 2018/10/19.
 */
public interface IShareBusinessService {

    List<Map<String,Object>> getShareRecords(String userId,String targetUserId,Date effectiveTime);

    ShareRecord getTargetShareRecord(String userId,String targetUserId,Date effectiveTime);


    void addShareRecord(ShareRecord shareRecord);

    List<Map<String,Object>> getTopShareRecords(String userId,Date effectiveTime);
}
