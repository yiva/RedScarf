package com.redscarf.weidou.adapter;

import java.util.List;





import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.FoodBody;
import com.redscarf.weidou.pojo.RedScarfBody;
import com.redscarf.weidou.util.BitmapCache;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FoodListAdapter extends BaseRedScarfAdapter<FoodBody>{

//	private ImageLoader imageLoader;
//	private RequestQueue queue;

	  public FoodListAdapter(Context mContext, List<FoodBody> listData)
	  {
	    super(mContext, listData);
//	    this.queue = Volley.newRequestQueue(mContext);
//	    this.imageLoader = new ImageLoader(this.queue, new BitmapCache());
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		  final ViewHolder viewHolder;
	    if (convertView == null){
	    	convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_food, parent, false);
	    	viewHolder = new ViewHolder();
	    	viewHolder.food_photo = ((NetworkImageView)convertView.findViewById(R.id.img_photo_food));
	    	viewHolder.food_ad_icon = ((ImageView)convertView.findViewById(R.id.img_ad_food));
	    	viewHolder.food_star = ((ImageView)convertView.findViewById(R.id.img_star_food));
	    	viewHolder.title = ((TextView)convertView.findViewById(R.id.txt_title_food));
	    	viewHolder.review = ((TextView)convertView.findViewById(R.id.txt_review_food));
	    	viewHolder.subtitle = ((TextView)convertView.findViewById(R.id.txt_subtitle_food));
	    	viewHolder.food_style = ((TextView)convertView.findViewById(R.id.txt_style_food));
	    	convertView.setTag(viewHolder);
	    }else {
	    	viewHolder = (ViewHolder) convertView.getTag();
		}
	    	viewHolder.title.setText(getItem(position).getTitle());
	    	viewHolder.review.setText("12Reviews");
	    	viewHolder.food_star.setImageResource(R.drawable.flower_2);
		  	viewHolder.subtitle.setText(Html.fromHtml(getItem(position).getSubtitle()));
		  	viewHolder.food_style.setText(getItem(position).getSubtype());
	    	viewHolder.position = position;
	    	viewHolder.food_photo.setImageResource(R.drawable.loading_large);
	    	String imageUrl = this.mRedScarfBodies.get(position).getPost_medium();
		    if ((imageUrl != null) && (!imageUrl.equals(""))){
		    	  viewHolder.food_photo.setDefaultImageResId(R.drawable.loading_large);
		    	  viewHolder.food_photo.setErrorImageResId(R.drawable.null_large);
		    	  viewHolder.food_photo.setBackgroundColor(0);
		    	  viewHolder.food_photo.setImageUrl(imageUrl, imageLoader);
		    }
			return convertView;
	  }

	  private static class ViewHolder
	  {
		  ImageView food_ad_icon;
		  NetworkImageView food_photo;
		  ImageView food_star;
		  TextView food_style;
		  int position;
		  TextView review;
		  TextView title;
		  TextView subtitle;
	  }
}
