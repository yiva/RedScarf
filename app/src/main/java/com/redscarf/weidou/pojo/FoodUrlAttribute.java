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

    public FoodUrlAttribute() {
        this.cost = "";
        this.topic = "";
        this.sub_category = "";
        this.order = "";
        this.distance = "";
        this.main_category = "4";
        this.fisrt_key = "";
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

    public void clear(){
        this.cost = "";
        this.topic = "";
        this.sub_category = "";
        this.order = "";
        this.distance = "";
        this.main_category = "4";
        this.fisrt_key = "";
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
