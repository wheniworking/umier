package com.longwei.umier.service;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.longwei.umier.dao.CourseGroupActivityDao;
import com.longwei.umier.dao.CourseGroupActivityOrderDao;
import com.longwei.umier.dao.WxUserDao;
import com.longwei.umier.entity.CourseGroupActivity;
import com.longwei.umier.entity.CourseGroupActivityOrder;
import com.longwei.umier.entity.WxUser;
import com.longwei.umier.interceptor.WxAuthInfoHolder;
import com.longwei.umier.utils.DateUtils;
import com.longwei.umier.utils.ReturnModel;
import com.longwei.umier.vo.ActivityInfoVo;
import com.longwei.umier.vo.GroupOrderVo;
import com.longwei.umier.vo.OrderVo;
import com.longwei.umier.vo.WxMpUserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupActivityService {

    @Autowired
    private CourseGroupActivityDao courseGroupActivityDao;

    @Autowired
    private CourseGroupActivityOrderDao courseGroupActivityOrderDao;
    @Autowired
    private WxUserDao wxUserDao;
    @Autowired
    private WxPayConfig payConfig;
    @Autowired
    private WxPayService payService;

    public Object getActivity(int id) {
        CourseGroupActivity activity = courseGroupActivityDao.getById(id);
        List<GroupOrderVo> groupOrderVos = courseGroupActivityOrderDao.getGroupByActivityId(id);
        List<String> ptIds = groupOrderVos.stream().map(GroupOrderVo::getPtId).collect(Collectors.toList());
        List<GroupOrderVo> participateGroupOrders = courseGroupActivityOrderDao.getParticipateGroupOrders(ptIds);
        Map<String, List<GroupOrderVo>> participateGroupMap =
                participateGroupOrders.stream().collect(Collectors.groupingBy(GroupOrderVo::getPtId));

        List<String> unionIds = groupOrderVos.stream().map(GroupOrderVo::getUnionId).collect(Collectors.toList());
        List<WxUser> wxUsers = wxUserDao.getUserByUnionIds(unionIds);
        Map<String, WxUser> wxUserMap = wxUsers.stream().collect(Collectors.toMap(WxUser::getUnionId, r -> r));
        for (GroupOrderVo vo : groupOrderVos) {
            List<GroupOrderVo> participates = participateGroupMap.get(vo.getPtId());
            int size = participates == null ? 0 : participates.size();
            vo.setPtNum(activity.getPtNum());
            vo.setDiffNum(activity.getPtNum() - size);
            vo.setLeftTime(DateUtils.diffTime(new Date(), DateUtils.addHour(vo.getCreateTime(), activity.getPtPeriod())));
            WxUser u = wxUserMap.get(vo.getUnionId());
            if (u != null) {
                vo.setNickName(u.getNickname());
                vo.setAvatar(u.getAvatar());
            }
        }

        ActivityInfoVo vo = new ActivityInfoVo();
        vo.setActivity(activity);
        vo.setGroupOrderVos(groupOrderVos);
        return vo;

    }

    public Object getGroupUserInfo(String ptId) {
        List<GroupOrderVo> groupOrderVos = courseGroupActivityOrderDao.getGroupOrdersByOrderId(ptId);
        List<String> unionIds = groupOrderVos.stream().map(GroupOrderVo::getUnionId).collect(Collectors.toList());
        return wxUserDao.getUserByUnionIds(unionIds);
    }

    public Map<String, String> orderCourse(OrderVo orderVo) {
       WxMpUserInfoVo mpUserInfoVo = WxAuthInfoHolder.get();
        CourseGroupActivity activity = courseGroupActivityDao.getById(orderVo.getActivityId());

//       private int id;
//    private String orderId;
//    private int groupActivityId;
//    private String unionId;
//    private double price;
//    private int type;
//
//    private int payState;
//    private int refundState;
//    private int ptId;
//
//    private Date createTime;
//    private int state;

        CourseGroupActivityOrder order = new CourseGroupActivityOrder();
        order.setGroupActivityId(orderVo.getActivityId());
        order.setOrderId(generateOrderId());
        order.setUnionId(mpUserInfoVo.getUnionId());
        order.setPayState(CourseGroupActivityOrder.PayState.NOT_PAY.getValue());
        order.setType(orderVo.getType());
        if(orderVo.getType() == CourseGroupActivityOrder.OrderType.SINGLE.getValue()){
            order.setPrice(activity.getSinglePrice());
            order.setPtId("");
        } else {
            order.setPrice(activity.getGroupPrice());
            order.setPtId(generatePtId());
        }
        order.setRefundState(CourseGroupActivityOrder.RefundState.NONE.getValue());
        order.setCreateTime(new Date());
        order.setState(0);

        courseGroupActivityOrderDao.insert(order);

        WxUser user = new WxUser();
        user.setUnionId(mpUserInfoVo.getUnionId());
        user.setPhone(orderVo.getPhone());
        user.setSex(mpUserInfoVo.getSex());
        user.setNickname(mpUserInfoVo.getNickname());
        user.setAvatar(mpUserInfoVo.getHeadImgUrl());
        user.setCreateTime(new Date());
        user.setState(0);

        wxUserDao.insert(user);

        //微信预支付
        ReturnModel returnModel = new ReturnModel();
        WxPayUnifiedOrderRequest prepayInfo = WxPayUnifiedOrderRequest.newBuilder()
                .openid(mpUserInfoVo.getOpenId())
                .outTradeNo(order.getOrderId())
                .totalFee((int)(order.getPrice()* 100))
                .body("优秘-课程")
                .tradeType("JSAPI")
                .spbillCreateIp(orderVo.getClientIp())
                .notifyUrl("")
                .build();
        Map<String, String> payInfo = null;
        try {
            payInfo = this.payService.createOrder(prepayInfo);
        } catch (WxPayException e) {
            e.printStackTrace();
        }
        return payInfo;
    }


    private String generateOrderId(){
        return "";
    }

    private String generatePtId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
