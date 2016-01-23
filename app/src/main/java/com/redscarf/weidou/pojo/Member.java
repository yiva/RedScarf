package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * 用户信息类
 * @author yeahwang
 *
 */
public class Member implements Parcelable{

	private Integer id;
	private String username;
	private String password;
	private String name;
	private String photo;
	private String nicename;
	private String email;
	private String url;
	private String registered;
	private String displayname;
	private String firstname;
	private String lastname;
	private String nickname;
	private String description;
	private String capabilites;
	private String avatar;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRegistered() {
		return registered;
	}
	public void setRegistered(String registered) {
		this.registered = registered;
	}
	public String getDisplayname() {
		return displayname;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCapabilites() {
		return capabilites;
	}
	public void setCapabilites(String capabilites) {
		this.capabilites = capabilites;
	}
	public String getAvatar() {
		return "http:"+avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getNicename() {
		return nicename;
	}

	public void setNicename(String nicename) {
		this.nicename = nicename;
	}

	public Member(){
	}

	public Member(Parcel in) {
		super();
		this.id = in.readInt();
		this.username = in.readString();
		this.password = in.readString();
		this.name = in.readString();
		this.photo = in.readString();
		this.nicename = in.readString();
		this.email = in.readString();
		this.url = in.readString();
		this.registered = in.readString();
		this.displayname = in.readString();
		this.firstname = in.readString();
		this.lastname = in.readString();
		this.nickname = in.readString();
		this.description = in.readString();
		this.capabilites = in.readString();
		this.avatar = in.readString();

	}

	/**
	 * 序列化
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(username);
		dest.writeString(password);
		dest.writeString(name);
		dest.writeString(photo);
		dest.writeString(nicename);
		dest.writeString(email);
		dest.writeString(url);
		dest.writeString(registered);
		dest.writeString(displayname);
		dest.writeString(firstname);
		dest.writeString(lastname);
		dest.writeString(nickname);
		dest.writeString(description);
		dest.writeString(capabilites);
		dest.writeString(avatar);
	}

	public static final Parcelable.Creator<Member> CREATOR = new Creator<Member>(){

		@Override
		public Member createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Member(source);
		}

		@Override
		public Member[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Member[size];
		}

	};

}
