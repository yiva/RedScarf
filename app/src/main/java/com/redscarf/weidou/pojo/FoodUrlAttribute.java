package com.redscarf.weidou.pojo;

/**
 * Created by XZR on 2016/7/2.
 */
public class FoodUrlAttribute {

    private String cost;//£ ££ £££ ££££ 消费水平
    private String topic;//主题
    private String sub_category;//子类别
    private String order;//排序
    private String distance;//位置
    private String main_category;//主类别
    private String fisrt_key; //首字母
    private Integer cost_flag;
    private Integer topic_flag;
    private Integer sub_category_flag;
    private Integer order_flag;
    private Integer distance_flag;
    private Integer main_category_flag;
    private Integer update_time_flag;

    public FoodUrlAttribute() {
        this.cost = "";
        this.topic = "";
        this.sub_category = "";
        this.order = "";
        this.distance = "";
        this.main_category = "4";
        this.fisrt_key = "";
        this.cost_flag = 0;
        this.topic_flag = 0;
        this.sub_category_flag = 0;
        this.order_flag = 0;
        this.distance_flag = 0;
        this.main_category_flag = 0;
        this.update_time_flag = 0;

    }

    public void clear(){
        this.cost = "";
        this.topic = "";
        this.sub_category = "";
        this.order = "";
        this.distance = "";
        this.main_category = "4";
        this.fisrt_key = "";
        this.cost_flag = 0;
        this.topic_flag = 0;
        this.sub_category_flag = 0;
        this.order_flag = 0;
        this.distance_flag = 0;
        this.main_category_flag = 0;
        this.update_time_flag = 0;
    }

    public Integer getUpdate_time_flag() {
        return update_time_flag;
    }

    public void setUpdate_time_flag(Integer update_time_flag) {
        this.update_time_flag = update_time_flag;
    }

    public Integer getCost_flag() {
        return cost_flag;
    }

    public void setCost_flag(Integer cost_flag) {
        this.cost_flag = cost_flag;
    }

    public Integer getTopic_flag() {
        return topic_flag;
    }

    public void setTopic_flag(Integer topic_flag) {
        this.topic_flag = topic_flag;
    }

    public Integer getSub_category_flag() {
        return sub_category_flag;
    }

    public void setSub_category_flag(Integer sub_category_flag) {
        this.sub_category_flag = sub_category_flag;
    }

    public Integer getOrder_flag() {
        return order_flag;
    }

    public void setOrder_flag(Integer order_flag) {
        this.order_flag = order_flag;
    }

    public Integer getDistance_flag() {
        return distance_flag;
    }

    public void setDistance_flag(Integer distance_flag) {
        this.distance_flag = distance_flag;
    }

    public Integer getMain_category_flag() {
        return main_category_flag;
    }

    public void setMain_category_flag(Integer main_category_flag) {
        this.main_category_flag = main_category_flag;
    }

    public String getFisrt_key() {
        return fisrt_key;
    }

    public void setFisrt_key(String fisrt_key) {
        this.fisrt_key = fisrt_key;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMain_category() {
        return main_category;
    }

    public void setMain_category(String main_category) {
        this.main_category = main_category;
    }


    @Override
    public String toString() {
        String url = "";
        if (!"".equals(this.cost)) {
            url = url + "&cost=" + this.cost;
        }

        if (!"".equals(this.topic)) {
            url = url + "&topic=" + this.topic;
        }

        if (!"".equals(this.sub_category)) {
            url = url + "&sub_category=" + this.sub_category;
        }

        if (!"".equals(this.order)) {
            url = url + "&orderby=post_cost&order=" + this.order;
        }

        if (!"".equals(this.main_category)) {
            url = "CatFood-" + this.order + "-" + url;
        }

        if (!"".equals(this.fisrt_key)) {
            url = "&meta_key=first_letter&meta_value="+this.fisrt_key;
        }
        return url;
    }
}
