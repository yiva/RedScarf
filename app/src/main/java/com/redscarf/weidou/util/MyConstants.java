package com.redscarf.weidou.util;

public class MyConstants {


	//移动距离
	public static final int FLING_MIN_DISTANCE = 63;

	//移动速度
	public static final int FLING_MIN_VELOCITY = 10;

	//正在加载
	public static final String LOADING = "正在加载...";

	//启动迟延
	public static final long SPLASH_TIME = 2000L;

	//欢迎页迟延
	public static final long WELCOME_TIME = 2500L;

	//重复提交次数
	public static final Integer MAX_RETRIES = 3;

	//请求时间（ms）
	public static final Integer REQUEST_LOAD_TIME = 5000;

	//Preference用户Cookie属性
	public static final String PREF_USER_COOKIE = "user_cookie";

	//Preference用户CookieName属性
	public static final String PREF_USER_COOKIE_NAME = "cookie_name";
	//Preference username属性
	public static final String PREF_USER_NAME = "user_name";

	//Preference password属性
	public static final String PREF_USER_PASSWORD = "password";

	//Preference user_id属性
	public static final String PREF_USER_ID = "userid";

	public static final String REPLACE_STRINGS = "(?:<p>|</p>)";

	//服务器地址
	public static final String URL = "http://weidou.co.uk/weipress/";

	//json API
	public static final String API = "wordou/";

	//BASE API
	public static final String API_BASE = URL+API;

	//头像地址
//	public static final String GRAVATAR = "http://weidou.co.uk/api/buddypressread/profile_get_profile/";

	/** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
	public static final String APP_KEY      = "804824912";

	/**
	 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
	 *
	 * <p>
	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
	 * 但是没有定义将无法使用 SDK 认证登录。
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 * </p>
	 */
	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
	 * 选择赋予应用的功能。
	 *
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
	 * 使用权限，高级权限需要进行申请。
	 *
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	 *
	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String SCOPE =
			"email,direct_messages_read,direct_messages_write,"
					+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
					+ "follow_app_official_microblog," + "invitation_write";

}
