package com.redscarf.weidou.network;

/**
 * Created by yeahwang on 2015/11/28.
 */
public enum RequestType {
    INDEXLIST       //首页分享listView请求
    ,HOTFOODLIST    //热门美食
    ,HOTSBUYLIST    //热门购物
    ,BUYLIST        //购物listView请求
    ,FOODLIST       //美食listView请求
    ,TAGLIST        //tag posts
    ,AUTHORLIST     //author post
    ,SEARCHLIST     //搜索页面
    ,HOTSEARCHLIST  //搜索缺省页面
    ,MODIFY_INDIVIDUAL  //个人信息修改
    ,FOOD_POST      //美食详细页面
    ,BRAND_POST     //折扣详细页面
    ,LOGIN_FIRST    //第一次登录
    ,LOGIN_AGAIN    //再次登录
    ,REGISTER       //注册
    ,MINE_PROFILE   //个人信息页面
    ,SEND_COMMENT   //发送评论
    ,CREATE_POST    //上传分享
    ,NONCE_VALUE    //获取临时key
    ,MAKE_FAVOURITE //收藏
    ,UNMAKE_FAVOURTIE   //取消收藏
    ,MY_FAOURITE        //我的收藏
    ,DISCOUNT_POST  //商品页面
    ,UPLOAD_AVATOR  //上传照片
    ,LOGIN_WEIBO    //微博登录
    ,FOOD_FILTER_LIST   //美食filter
    ,FOODLIST_WITH_FILTER   //带条件的美食查询
}
