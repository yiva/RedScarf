package com.redscarf.weidou.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.redscarf.weidou.activity.BasicViewActivity;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.customwidget.HorizontalListView;
import com.redscarf.weidou.pojo.RedScarfBody;
import com.redscarf.weidou.util.BitmapCache;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 首页ListView
 * Share
 */
public class IndexListAdapter extends BaseRedScarfAdapter<RedScarfBody> {

	private final static String TAG = IndexListAdapter.class.getSimpleName();

	private ListView mlist;
	protected ImageLoader logoLoader;

	public IndexListAdapter(Context context, List<RedScarfBody> listData) {
		super(context, listData);
		this.logoLoader = new ImageLoader(queue, new BitmapCache());
	}

	public IndexListAdapter(Context context, List<RedScarfBody> listData,
							ListView list) {
		super(context, listData);
		this.mlist = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		// String logo_url = makeUserProfile();
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listview_index, parent, false);
			viewHolder = new ViewHolder();

			viewHolder.logo = (NetworkImageView) convertView
					.findViewById(R.id.img_index_user_logo);
			viewHolder.username = (TextView) convertView
					.findViewById(R.id.txt_username_index);
			viewHolder.location = (TextView) convertView
					.findViewById(R.id.txt_location_index);
			viewHolder.photo = (NetworkImageView) convertView
					.findViewById(R.id.img_index_product);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.content_index);
			viewHolder.tag = (HorizontalListView) convertView
					.findViewById(R.id.tag_index);
			viewHolder.discuss_counts = (TextView) convertView
					.findViewById(R.id.discuss_index);
			viewHolder.zan_counts = (TextView) convertView
					.findViewById(R.id.zan_index);
			viewHolder.favourite_counts = (TextView) convertView
					.findViewById(R.id.favourite_index);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//格式化数据
		formatIndexRedScarfBody(getItem(position));
		viewHolder.username.setText(getItem(position).getAuthor());
		viewHolder.location.setText(getItem(position).getLocation());
		String content = getItem(position).getContent();
		if (content != null && content.length() > 65) {
			StringBuffer str = new StringBuffer(content.substring(0, 65));
			str.append("...");
			content = str.toString();
		}
		viewHolder.content.setText(Html.fromHtml(content));
		viewHolder.tag.setAdapter(new TagListAdapter(mContext, Arrays.asList(getItem(position).getTags().split(","))));
		viewHolder.discuss_counts.setText("评论"+getItem(position).getComment_count());
		viewHolder.zan_counts.setText("赞5");
		viewHolder.favourite_counts.setText("收藏");
		viewHolder.photo.setTag(getItem(position).getPost_medium());
		viewHolder.logo.setTag(getItem(position).getAuthor_thumb());
		String imageUrl = ((RedScarfBody) this.mRedScarfBodies.get(position))
				.getPost_medium();
//		String logoUrl = this.mRedScarfBodies.get(position).getAuthor_thumb();
		//goods image
		viewHolder.photo.setBackgroundResource(R.drawable.loading_large);
		if ((imageUrl != null) && (!imageUrl.equals(""))) {
			viewHolder.photo.setDefaultImageResId(R.drawable.loading_large);
			viewHolder.photo.setErrorImageResId(R.drawable.loading_large);
			viewHolder.photo.setBackgroundColor(0);
			viewHolder.photo.setImageUrl(imageUrl, imageLoader);
		}
		//子控件事件
		viewHolder.tag.setOnItemClickListener(new onTagItemClick(position));//tag_post
		viewHolder.logo.setOnClickListener(new onProfileClick(position));//author_post
		return convertView;
	}

	private static class ViewHolder {
		NetworkImageView logo; // 头像
		TextView username;// 用户名
		TextView location;// 位置
		NetworkImageView photo;// 照片
		TextView content;// 文本内容
		HorizontalListView tag;// 文章标签
		TextView discuss_counts;// 评论数
		TextView zan_counts;// 赞数
		TextView favourite_counts;// 收藏数
	}

	/**
	 * 获取头像
	 */
	private static String makeGavator(JSONObject json) {
		String url = "";
		if (!json.isNull("profilefields")) {
			JSONObject iObj;
			try {
				iObj = json.getJSONObject("profilefields").getJSONObject(
						"photo");
				url = "http:" + iObj.getString("avatar");
			} catch (JSONException e) {
				e.printStackTrace();
				url = "http://www.gravatar.com/avatar/7ec1c1c52e3d93791872c38be751908b?d=mm&amp;s=150&amp;r=G";
				Log.e(TAG, e.getMessage());
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				url = "http://www.gravatar.com/avatar/7ec1c1c52e3d93791872c38be751908b?d=mm&amp;s=150&amp;r=G";
			}

		}
		return url;
	}

	/**
	 * 子控件事件
	 */
	protected class ItemWidget{
		protected int pos;//位置
		public ItemWidget(int p){
			this.pos = p;
		}
	}

	/**
	 * 标签点击事件
	 */
	private class onTagItemClick extends ItemWidget implements AdapterView.OnItemClickListener{

		public onTagItemClick(int p) {
			super(p);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String[] tags = mRedScarfBodies.get(pos).getTags().split(",");
			Bundle datas = new Bundle();
			datas.putString("flag", "tag_post");
			datas.putString("tag_slug", tags[position]);
			Intent i_tag = new Intent(mContext, BasicViewActivity.class);
			i_tag.putExtras(datas);
			mContext.startActivity(i_tag);
//			Toast.makeText(mContext, "hello", Toast.LENGTH_SHORT).show();
		}
	}

	private class onProfileClick extends ItemWidget implements View.OnClickListener{

		public onProfileClick(int p) {
			super(p);
		}
		@Override
		public void onClick(View v) {
			Bundle datas = new Bundle();
			datas.putString("flag", "author_post");
			datas.putString("author_id", mRedScarfBodies.get(pos).getAuthor_id());
			Intent i_author = new Intent(mContext, BasicViewActivity.class);
			i_author.putExtras(datas);
			mContext.startActivity(i_author);
		}
	}


}
