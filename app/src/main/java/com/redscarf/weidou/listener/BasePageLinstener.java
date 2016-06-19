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

    int PROGRESS_DISVISIBLE = 0;
    int PROGRESS_NO_CANCLE = 1;
    int PROGRESS_CANCLE = 2;

    void initView();
}
