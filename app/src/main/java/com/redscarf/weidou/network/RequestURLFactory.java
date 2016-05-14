package com.redscarf.weidou.network;

import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.util.MyPreferences;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.finalteam.toolsfinal.StringUtils;

/**
 * Created by yeahwang on 2015/11/28.
 */
public class RequestURLFactory {

    private static String baseUrl = MyConstants.API_BASE;

    /**
     * @param type       {INDEXLIST|BUYLIST|HOTFOODLIST|HOTBUYLIST|FOODLIST|TAGLIST|AUTHORLIST}
     * @param attributes
     * @return result of format-URL
     * @see if type = INDEXLIST then indexlistview, attributes:[0:pageNum]
     * if type = BUYLIST then buylistview, attributes:[0:pageNum]
     */
    public static String getRequestListURL(RequestType type, String... attributes) {
        String res = "";
        switch (type) {
            case INDEXLIST://indexlist IndexFragment
                res = baseUrl + "?json=get_category_posts&id=183&count=10&page=" + attributes[0];
                break;
            case HOTFOODLIST:
                res = baseUrl + "?json=get_category_posts&id=288&count=10&page=" + attributes[0];
                break;
            case HOTSBUYLIST:
                res = baseUrl + "?json=get_category_posts&id=289&count=10&page=" + attributes[0];
                break;
            case BUYLIST://buylist BuyFragment
                res = baseUrl + "?json=get_category_posts&id=" + attributes[0] + "&count=10&page=" + attributes[1];
                break;
            case FOODLIST://foodlist FoodFragment
                res = baseUrl + "?json=get_category_posts&id=" + attributes[0] + "&count=10&page=" + attributes[1];
                break;
            case TAGLIST:
                res = baseUrl + "?json=get_tag_posts&tag_slug=" + attributes[0];
                break;
            case AUTHORLIST:
                res = baseUrl + "?json=get_author_posts&id=" + attributes[0];
                break;
            case HOTSEARCHLIST:
                res = baseUrl + "?json=get_hot_" + attributes[0] + "&count=10";//[foodposts；discountposts；searches]
                break;
            case SEARCHLIST:
                res = baseUrl + "?json=get_search_category_results&search=" + attributes[0];
                break;
            default:
                res = "false";
                break;

        }
        return res;
    }

    /**
     * @param type       [MODIFY_INDIVIDUAL //个人信息修改
     *                   ,FOOD_POST      //美食详细页面
     *                   ,BRAND_POST     //折扣详细页面
     *                   ,MINE_PROFILE   //个人信息页面
     *                   ,SEND_COMMENT   //发送评论
     *                   ,CREATE_POST    //上传分享]
     * @param attributes
     * @return
     * @// TODO: 2015/11/28
     * modify individaul 两个参数
     * food post 一个参数
     * brand post 一个参数
     * mine profile 一个参数
     * send comment 三个参数
     * create post 六个参数
     * //
     */
    public static String getRequestURL(RequestType type, String... attributes) {
        String res = "";
        switch (type) {
            case FOOD_POST:
                res = baseUrl + "?json=get_foodpost&post_id=" + attributes[0];
                break;
            case BRAND_POST:
                res = baseUrl + "?json=get_brandpost&post_id=" + attributes[0];
                break;
            case DISCOUNT_POST:
                res = baseUrl + "?json=get_discountpost&post_id=" + attributes[0];
                break;
            case SEND_COMMENT:
                res = baseUrl + "user/post_comment/?cookie=" + MyPreferences.getAppPerenceAttribute(MyConstants.PREF_USER_COOKIE)
                        + "&post_id=" + attributes[0] + "&content=" + attributes[1] + "&comment_status=" + attributes[2];
                break;
            /*
            imageFile:图片字节码
             */
            case CREATE_POST:
                res = baseUrl + "posts/create_post/?nonce=" + attributes[0] + "&status=publish&title=" + attributes[1]
                        + "&content=" + attributes[2] + "&categories=" + attributes[3] + "&tags=" + attributes[4] + "&imageFile=" + attributes[5];
                break;
            default:
                res = "false";
                break;
        }
        String author_id = String.valueOf(MyPreferences.getAppPerenceAttribute(MyConstants.PREF_USER_ID));
        if (!StringUtils.isBlank(author_id)) {
            res = res + "&author_id=" + author_id;
        }
        return res;
    }

    /**
     * 必须带author_id的post
     *
     * @param type
     * @param attributes
     * @return
     */
    public static String getRequestURLWithAuthor(RequestType type, String... attributes) {
        String author_id = String.valueOf(MyPreferences.getAppPerenceAttribute(MyConstants.PREF_USER_ID));
        if (StringUtils.isBlank(author_id)) {
            return "false";
        }
        String res = "";
        switch (type) {
            case MAKE_FAVOURITE:
                res = baseUrl + "?json=mark_activity_favorite&post_id=" + attributes[0] + "&author_id=" + author_id;
                break;
            case UNMAKE_FAVOURTIE:
                res = baseUrl + "?json=unmark_activity_favorite&post_id=" + attributes[0] + "&author_id=" + author_id;
                break;
            case MY_FAOURITE:
                res = baseUrl + "?json=get_author_favorites&author_id=" + author_id + "&count=-1" +
                        "&page=1";
                break;
            default:
                res = "false";
                break;
        }
        return res;
    }

    /**
     * @param type       [LOGIN_FIRST    //第一次登录
     *                   ,LOGIN_AGAIN    //再次登录]
     * @param attributes
     * @return
     * @// TODO: 2015/11/28 login first 两个参数，login again 一个参数
     */
    public static String sysRequestURL(RequestType type, String... attributes) {
        String res = "";
        switch (type) {
            case MINE_PROFILE:
                res = baseUrl + "user/get_userinfo/?user_id=" + attributes[0];
                break;
            case MODIFY_INDIVIDUAL:
                res = baseUrl + "update_user_meta/?cookie=" + MyPreferences.getAppPerenceAttribute(MyConstants.PREF_USER_COOKIE)
                        + "&meta_key=" + attributes[0] + "&meta_value=" + attributes[1];
                break;
            case LOGIN_FIRST:
                res = baseUrl + "user/generate_auth_cookie/?username=" + attributes[0] + "&password=" + attributes[1];
                break;
            case LOGIN_AGAIN:
                res = baseUrl + "user/validate_auth_cookie/?cookie=" + attributes[0];
                break;
            case REGISTER:
                res = baseUrl + "user/register/?username=" + attributes[0] + "&email=" + attributes
                        [1] + "&display_name=" + attributes[2] + "&nickname=" + attributes[3] +
                        "&user_pass=" + attributes[4] + "&nonce="
                        + attributes[5] + "&notify=no";
                break;
            /*
            controller:posts
            method:create_post
             */
            case NONCE_VALUE:
                res = baseUrl + "get_nonce/?controller=" + attributes[0] + "&method=" + attributes[1];
                break;
            case UPLOAD_AVATOR:
                try {
                    res = baseUrl + "user/upload_avatar/?cookie=" +
                            URLEncoder.encode(attributes[0], "utf-8") + "&file=" + URLEncoder.encode(attributes[1], "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            default:
                res = "false";
                break;
        }
        return res;
    }

}
