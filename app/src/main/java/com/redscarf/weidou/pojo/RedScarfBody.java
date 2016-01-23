package com.redscarf.weidou.pojo;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.redscarf.weidou.util.MyConstants;

/**
 * RedScarf实体类
 * 接收网站数据
 * @author yeahwa
 *
 */
public class RedScarfBody implements Parcelable{

	private int id;//id
	private String content;//内容
	private String location;//地址
	private float latitude;//纬度
	private float longitude;//经度
	private String date;//时间
	private String tags;
	private String author;//作者
	private String author_id;//作者ID
	private String author_thumb;//作者头像
	private String post_medium;//产品图片地址
	private String comment_count;//评价数
	private String like_count;
	private String is_following;
	private String is_favorate;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public String getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}

	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getAuthor_thumb() {
		return "http:"+author_thumb;
	}
	public void setAuthor_thumb(String author_thumb) {
		this.author_thumb = author_thumb;
	}
	public String getPost_medium() {
		return MyConstants.URL+"wp-content/uploads"+post_medium;
	}
	public void setPost_medium(String post_medium) {
		this.post_medium = post_medium;
	}

	public String getLike_count() {
		return like_count;
	}

	public void setLike_count(String like_count) {
		this.like_count = like_count;
	}

	public String getIs_following() {
		return is_following;
	}

	public void setIs_following(String is_following) {
		this.is_following = is_following;
	}

	public String getIs_favorate() {
		return is_favorate;
	}

	public void setIs_favorate(String is_favorate) {
		this.is_favorate = is_favorate;
	}

	public RedScarfBody() {
		super();
	}

	public RedScarfBody(Parcel in) {
		super();
		this.id = in.readInt();
		this.content = in.readString();
		this.location = in.readString();
		this.latitude = in.readFloat();
		this.longitude = in.readFloat();
		this.date = in.readString();
		this.tags = in.readString();
		this.author = in.readString();
		this.author_thumb = in.readString();
		this.post_medium = in.readString();
		this.comment_count = in.readString();
		this.is_favorate = in.readString();
		this.is_following = in.readString();
		this.like_count = in.readString();
	}

	/**
	 * 序列化
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(content);
		dest.writeString(location);
		dest.writeFloat(latitude);
		dest.writeFloat(longitude);
		dest.writeString(date);
		dest.writeString(tags);
		dest.writeString(author);
		dest.writeString(author_thumb);
		dest.writeString(post_medium);
		dest.writeString(comment_count);
		dest.writeString(is_favorate);
		dest.writeString(is_following);
		dest.writeString(like_count);
	}

	public static final Parcelable.Creator<RedScarfBody> CREATOR = new Creator<RedScarfBody>(){

		@Override
		public RedScarfBody createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new RedScarfBody(source);
		}

		@Override
		public RedScarfBody[] newArray(int size) {
			// TODO Auto-generated method stub
			return new RedScarfBody[size];
		}

	};

}
