package com.redscarf.weidou.pojo;

/**
 * Created by XZR on 2016/7/2.
 */
public class FoodUrlAttribute {

    private String main_category;//主类别
    private String sub_category;//子类别
    private String topic;//主题
    private String cost;//£ ££ £££ ££££ 消费水平

    private String update_time;//更新时间
    private String price;//排序
    private String distance;//位置

    private String fisrt_key; //首字母

    private Integer cost_flag;
    private Integer topic_flag;
    private Integer sub_category_flag;
    private Integer price_flag;
    private Integer distance_flag;
    private Integer main_category_flag;
    private Integer update_time_flag;
    private String latitude;
    private String longitude;

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getPrice_flag() {
        return price_flag;
    }

    public void setPrice_flag(Integer price_flag) {
        this.price_flag = price_flag;
    }

    public FoodUrlAttribute() {
        this.cost = "";
        this.topic = "";
        this.sub_category = "";
        this.price = "";
        this.distance = "";
        this.main_category = "4";
        this.fisrt_key = "";
        this.update_time = "DESC";
        this.cost_flag = 0;
        this.topic_flag = 0;
        this.sub_category_flag = 0;
        this.price_flag = 0;
        this.distance_flag = 0;
        this.main_category_flag = 0;
        this.update_time_flag = 0;
        this.latitude = "";
        this.longitude = "";
    }

    public void clear() {
        this.cost = "";
        this.topic = "";
        this.sub_category = "";
        this.price = "";
        this.distance = "";
        this.main_category = "4";
        this.fisrt_key = "";
        this.update_time = "DESC";
        this.cost_flag = 0;
        this.topic_flag = 0;
        this.sub_category_flag = 0;
        this.price_flag = 0;
        this.distance_flag = 0;
        this.main_category_flag = 0;
        this.update_time_flag = 0;
        this.latitude = "";
        this.longitude = "";
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        String url = "";
        if (!"".equals(this.main_category)) {
            url = url + "&id=" + main_category;
        }
        if (!"".equals(this.cost)) {
            url = url + "&cost=" + this.cost;
        }

        if (!"".equals(this.price)) {
            url = url + "&orderby=post_cost&order=" + this.price;
        }

        if (!"".equals(this.distance)) {
            url = url + "&orderby=distance&order=" + this.distance;
        }

        if (!"".equals(this.topic)) {
            url = url + "&topic=" + this.topic;
        }

        if (!"".equals(this.sub_category)) {
            url = url + "&sub_category=" + this.sub_category;
        }

        if (!"".equals(this.update_time)) {
            url = url + "&orderby=" + this.update_time;
        }

        if (!"".equals(this.fisrt_key)) {
            url = url + "&meta_key=first_letter&meta_value=" + this.fisrt_key;
        }

        if (!"".equals(this.latitude) && !"".equals(this.longitude)) {
            url = url + "&lat=" + this.latitude + "&lng=" + this.longitude;
        }
        return url;
    }
}
