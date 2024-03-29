package com.redscarf.weidou.listener;

/**
 * Created by yeahwang on 2016/2/29.
 */
public interface BasePageLinstener {

    int MSG_INDEX = 1; //msg.what index
    int MSG_IS_FAVOURITE = 2;//make favourite
    int MSG_IS_NOT_FAVOURITE = 3;//unmake favourite
    int MSG_NONCE = 4;
    int MSG_NEXT_PAGE = 5;  //上拉加载更多
    int MSG_FOOD_FILTER = 6;//菜系
    int MSG_FOOD_INDEX_NOT_CHANG_SELECT = 7; //不改变美食上方字母
    int MSG_UPLOAD = 8;//文件上传
    int MSG_WEIBO_AVATAR = 9;
    int MSG_VALID = 12; //msg.what valid cookie
    int MSG_ERROR = -1;//请求异常

    int PROGRESS_DISVISIBLE = 0;
    int PROGRESS_NO_CANCELABLE = -1;//点击无法取消弹出框
    int PROGRESS_CANCELABLE = 1;//弹出框可以取消

    void initView();

}
