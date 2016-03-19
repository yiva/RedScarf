package com.redscarf.weidou.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.redscarf.weidou.pojo.FormImage;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.util.MyPreferences;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.BufferUnderflowException;
import java.util.List;
import java.util.UUID;

/**
 * 对数据的封装
 * Created by yeahwang on 2016/1/18.
 */
public class PostUploadRequest extends Request<String> {

    /**
     * 正确数据的时候回掉用
     */
    private ResponseListener mListener ;
    /*请求 数据通过参数的形式传入*/
    private List<FormImage> mListItem ;

    private String BOUNDARY = "--WebKitFormBoundary" + UUID.randomUUID().toString(); //数据分隔线
    private String MULTIPART_FORM_DATA = "multipart/form-data";

    public PostUploadRequest(String url, List<FormImage> listItem, ResponseListener listener) {
        super(Method.POST, url, listener);
        this.mListener = listener ;
        setShouldCache(false);
        mListItem = listItem ;
        setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * 这里开始解析数据
     * @param networkResponse Response from the network
     * @return
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String mString =
                    new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            Log.v("zgy", "====mString===" + mString);

            return Response.success(mString,
                    HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    /**
     * 回调正确的数据
     * @param response The parsed response returned by
     */
    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mListItem == null||mListItem.size() == 0){
            return super.getBody() ;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        StringBuffer sc= new StringBuffer() ;
            /*第一行*/
        sc.append(BOUNDARY);
        sc.append("\r\n") ;
            /*第二行*/
        sc.append("Content-Disposition: form-data;");
        sc.append(" name=\"");
        sc.append("userCookie");
        sc.append("\"") ;
        sc.append("\r\n") ;
            /*第三行*/
        sc.append("Content-Type: text/plain; charset=UTF-8");
        sc.append("\r\n") ;
            /*第四行*/
        sc.append("\r\n") ;
        sc.append(MyPreferences.getAppPerenceAttribute(MyConstants.PREF_USER_COOKIE));
        sc.append("\r\n") ;
        /*第一行*/
        sc.append(BOUNDARY);
        sc.append("\r\n") ;
            /*第二行*/
        sc.append("Content-Disposition: form-data;");
        sc.append(" name=\"");
        sc.append("userCookie_name");
        sc.append("\"") ;
        sc.append("\r\n") ;
            /*第三行*/
        sc.append("Content-Type: text/plain; charset=UTF-8");
        sc.append("\r\n") ;
            /*第四行*/
        sc.append("\r\n") ;
        sc.append(MyPreferences.getAppPerenceAttribute(MyConstants.PREF_USER_COOKIE_NAME));
        try {
            bos.write(sc.toString().getBytes("utf-8"));
            bos.write("\r\n".getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int N = mListItem.size() ;
        FormImage formImage ;
        for (int i = 0; i < N ;i++){
            formImage = mListItem.get(i) ;
            StringBuffer sb= new StringBuffer() ;
            /*第一行*/
            sb.append(BOUNDARY);
            sb.append("\r\n") ;
            /*第二行*/
            sb.append("Content-Disposition: form-data;");
            sb.append(" name=\"");
            sb.append(formImage.getName()) ;
            sb.append("\"") ;
            sb.append("; filename=\"") ;
            sb.append(formImage.getFileName()) ;
            sb.append("\"");
            sb.append("\r\n") ;
            /*第三行*/
            sb.append("Content-Type: ");
            sb.append(formImage.getMime()) ;
            sb.append("\r\n") ;
            /*第四行*/
            sb.append("\r\n") ;
            try {
                bos.write(sb.toString().getBytes("utf-8"));
                /*第五行*/
                bos.write(formImage.getValue());
                bos.write("\r\n".getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        /*结尾行*/
        String endLine = BOUNDARY + "--" + "\r\n";
        try {
            bos.write(endLine.toString().getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("zgy", "=====formImage====\n" + bos.toString()) ;
        return bos.toByteArray();
    }

    @Override
    public String getBodyContentType() {
        return MULTIPART_FORM_DATA+"; boundary="+BOUNDARY;
    }

}
