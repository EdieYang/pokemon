package com.pokepet.handler;

import com.pokepet.service.IPetManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Configurable
@EnableScheduling
public class ScheduledTasks{

    @Autowired
    private IPetManageService petManageService;

    private static final Logger log= LoggerFactory.getLogger(ScheduledTasks.class);

    //每日凌晨刷新宠物活力值
    @Scheduled(cron = "0 0 0 * * ? ")
    public void reportCurrentByCron(){
        log.info("Scheduling Tasks fill energy for all pets: The time is now " + dateFormat ().format (new Date()));
        int rows=petManageService.countAllPets();
        int totalRows=petManageService.updatePetsEnergyToOneHundredPercent();
    }

    private SimpleDateFormat dateFormat(){
        return new SimpleDateFormat ("yyyy-dd-MM HH:mm:ss");
    }
    
}