package com.redscarf.weidou.pojo;

/**
 * Created by yeahwang on 2015/12/11.
 */
public class GridBody {

    private Integer backgroudColor;
    private String title;
    private Integer drawableSource;
    private Integer postId;

    public GridBody(Integer backgroudColor, String title, Integer drawableSource, Integer postId) {
        this.backgroudColor = backgroudColor;
        this.title = title;
        this.drawableSource = drawableSource;
        this.postId = postId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getBackgroudColor() {
        return backgroudColor;
    }

    public void setBackgroudColor(Integer backgroudColor) {
        this.backgroudColor = backgroudColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDrawableSource() {
        return drawableSource;
    }

    public void setDrawableSource(Integer drawableSource) {
        this.drawableSource = drawableSource;
    }
}
