package com.pokepet.service.impl;

import com.pokepet.dao.PropertiesConfigMapper;
import com.pokepet.service.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Fade on 2018/10/29.
 */

@Service
public class PropertyServiceImpl implements IPropertyService {

    @Autowired
    private PropertiesConfigMapper propertiesConfigMapper;


    @Override
    public Map<String, Object> getPropertyStatus(Map<String, Object> map) {
        return propertiesConfigMapper.getPropertyStatus(map);
    }
}
