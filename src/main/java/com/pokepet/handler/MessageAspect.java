package com.pokepet.handler;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.dao.*;
import com.pokepet.model.MessageQueue;
import com.pokepet.model.UserLongRecord;
import com.pokepet.model.UserPet;
import com.pokepet.model.UserRecord;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
public class MessageAspect {

    @Autowired
    private MessageQueueMapper messageQueueMapper;

    @Autowired
    private UserRecordMapper userRecordMapper;

    @Autowired
    private UserLongRecordMapper userLongRecordMapper;


    private static final Logger log= LoggerFactory.getLogger(MessageAspect.class);

    //关注宠物切点
    @Pointcut("execution(public * com.pokepet.controller.UserFollowController.followPet(..))")
    public void followPetPointCut(){}

    //关注用户切点
    @Pointcut("execution(public * com.pokepet.controller.UserFollowController.followUser(..))")
    public void followUserPointCut(){}

    //用户点赞切单
    @Pointcut("execution(public * com.pokepet.controller.RecordController.recordLike(..))")
    public void recordLikePointCut(){}

    @Around("followPetPointCut()")
    public Object aroundFollowPetPointCut(ProceedingJoinPoint pjp) {
        log.info("{MessageAspect} =>followPet start.....");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        String userId=request.getParameter("userId");
        String petId= (String) pjp.getArgs()[0];


        try {  
            Object o =  pjp.proceed();  
            System.out.println("方法环绕proceed，结果是 :" + o);
            if(o.equals(true)){
               //加入消息队列
                MessageQueue messageQueue=new MessageQueue();
                messageQueue.setSenderId(userId);
                if(StringUtils.isNotEmpty(petId)){


                    Map<String,String> messageNeedInfo=messageQueueMapper.getMessageNeedFollowPetInfo(petId);
                    String petName=messageNeedInfo.get("petName");
                    String userName=messageNeedInfo.get("userName");

                    messageQueue.setSenderId(userId);
                    messageQueue.setReceiverId(messageNeedInfo.get("userId"));
                    messageQueue.setType("0");
                    messageQueue.setTargetId(petId);
                    int messageCount=messageQueueMapper.selectMessageCount(messageQueue);
                    if(messageCount==0){
                        JSONObject contentJson=new JSONObject();
                        contentJson.put("photoPath",messageNeedInfo.get("photoPath"));
                        contentJson.put("content",userName+"觉得"+petName+"很可爱并赞了它");

                        messageQueue.setMessageContent(contentJson.toJSONString());
                        messageQueue.setCreateTime(new Date());
                        messageQueue.setMessageId(UUID.randomUUID().toString().replace("-",""));
                        messageQueue.setReadState("0");
                        messageQueueMapper.insertSelective(messageQueue);
                    }

                }

            }
            return o;  
        } catch (Throwable e) {  
            e.printStackTrace();  
            return null;  
        }  
    }



    @Around("followUserPointCut()")
    public Object aroundFollowUserPointCut(ProceedingJoinPoint pjp) {
        log.info("{MessageAspect} =>followUser start.....");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        String followUserId=request.getParameter("followUserId");
        String userId= (String) pjp.getArgs()[0];


        try {
            Object o =  pjp.proceed();
            System.out.println("方法环绕proceed，结果是 :" + o);
            if(o.equals(true)){
                //加入消息队列
                MessageQueue messageQueue=new MessageQueue();
                messageQueue.setSenderId(followUserId);
                if(StringUtils.isNotEmpty(userId)){

                    Map<String,String> messageNeedInfo=messageQueueMapper.getMessageNeedFollowUserInfo(followUserId);
                    String photoPath=messageNeedInfo.get("photoPath");
                    String userName=messageNeedInfo.get("userName");

                    messageQueue.setSenderId(followUserId);
                    messageQueue.setReceiverId(userId);
                    messageQueue.setTargetId(userId);
                    messageQueue.setType("1");
                    int messageCount=messageQueueMapper.selectMessageCount(messageQueue);
                    if(messageCount==0){
                        JSONObject contentJson=new JSONObject();
                        contentJson.put("photoPath",photoPath);
                        contentJson.put("content",userName+" 觉得你很棒并开始关注你了");

                        messageQueue.setMessageContent(contentJson.toJSONString());
                        messageQueue.setCreateTime(new Date());
                        messageQueue.setMessageId(UUID.randomUUID().toString().replace("-",""));
                        messageQueue.setReadState("0");
                        messageQueueMapper.insertSelective(messageQueue);
                    }

                }

            }
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }






    @Around("(recordLikePointCut())")
    public Object aroundUserLikePointCut(ProceedingJoinPoint pjp) {
        log.info("{MessageAspect} =>followUser start.....");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        String popLikeUserId=request.getParameter("userId");
        String recordId= request.getParameter("recordId");
        String recordType=request.getParameter("recordType");

        try {
            Object o =  pjp.proceed();
            System.out.println("方法环绕proceed，结果是 :" + o);
            if(o.equals(true)){
                //加入消息队列
                MessageQueue messageQueue=new MessageQueue();
                messageQueue.setSenderId(popLikeUserId);
                if(StringUtils.isNotEmpty(popLikeUserId)){

                    Map<String,String> messageNeedInfo=messageQueueMapper.getMessageNeedFollowUserInfo(popLikeUserId);
                    String photoPath=messageNeedInfo.get("photoPath");
                    String userName=messageNeedInfo.get("userName");
                    String receiverUserId="";
                    String recordTitle="";
                    if(recordType.equals("0")||recordType.equals("1") ){
                        UserLongRecord record=userLongRecordMapper.selectByPrimaryKey(recordId);
                        receiverUserId=record.getUserId();
                        recordTitle=record.getTitle();
                    }else{
                        UserRecord record=userRecordMapper.selectByPrimaryKey(recordId);
                        receiverUserId=record.getUserId();
                        recordTitle=record.getTitle();

                    }

                    messageQueue.setSenderId(popLikeUserId);
                    messageQueue.setReceiverId(receiverUserId);
                    messageQueue.setTargetId(recordId);
                    messageQueue.setType("2");
                    int messageCount=messageQueueMapper.selectMessageCount(messageQueue);
                    if(messageCount==0){
                        JSONObject contentJson=new JSONObject();
                        contentJson.put("photoPath",photoPath);
                        contentJson.put("content",userName+"点赞了 "+recordTitle);

                        messageQueue.setMessageContent(contentJson.toJSONString());
                        messageQueue.setCreateTime(new Date());
                        messageQueue.setMessageId(UUID.randomUUID().toString().replace("-",""));
                        messageQueue.setReadState("0");
                        messageQueueMapper.insertSelective(messageQueue);
                    }

                }

            }
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }











//    @Before("followPetPointCut()")
//    public void deBefore(JoinPoint joinPoint) throws Throwable {
//        // 接收到请求，记录请求内容
////        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
////        HttpServletRequest request = attributes.getRequest();
////        // 记录下请求内容
////        String petId=request.getParameter("userId");
////        System.out.println("petId:"+petId);
////        System.out.println("URL : " + request.getRequestURL().toString());
////        System.out.println("HTTP_METHOD : " + request.getMethod());
////        System.out.println("IP : " + request.getRemoteAddr());
////        System.out.println("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
////        System.out.println("ARGS : " + Arrays.toString(joinPoint.getArgs()));
//
//    }
//
//    @AfterReturning(returning = "ret", pointcut = "followPetPointCut()")
//    public void doAfterReturning(JoinPoint joinPoint,Object ret) throws Throwable {
//        // 处理完请求，返回内容
//        System.out.println("方法的返回值 : " + ret);
//        if(ret.equals(true)){
//
//        }
//    }
//
//    //后置异常通知
//    @AfterThrowing("followPetPointCut()")
//    public void throwss(JoinPoint jp){
//        System.out.println("方法异常时执行.....");
//    }
//
//    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
//    @After("followPetPointCut()")
//    public void after(JoinPoint jp){
//        System.out.println("方法最后执行.....");
//    }
}  