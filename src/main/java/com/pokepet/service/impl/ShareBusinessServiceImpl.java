package com.pokepet.service.impl;

import com.pokepet.dao.ShareRecordMapper;
import com.pokepet.model.ShareRecord;
import com.pokepet.service.IShareBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Fade on 2018/10/19.
 */

@Service
public class ShareBusinessServiceImpl implements IShareBusinessService {

    @Autowired
    private ShareRecordMapper shareRecordMapper;


    @Override
    public List<Map<String, Object>> getShareRecords(String userId,String targetUserId ,Date effectiveTime) {
        return shareRecordMapper.getShareRecords(userId,targetUserId,effectiveTime);
    }

    @Override
    public ShareRecord getTargetShareRecord(String userId, String targetUserId, Date effectiveTime) {
        return shareRecordMapper.getTargetShareRecord(userId,targetUserId,effectiveTime);
    }

    @Override
    public void addShareRecord(ShareRecord shareRecord) {
        shareRecordMapper.insertSelective(shareRecord);
    }

    @Override
    public List<Map<String, Object>> getTopShareRecords(String userId, Date effectiveTime) {
        return shareRecordMapper.getTopShareRecords(userId,effectiveTime);

    }
}
