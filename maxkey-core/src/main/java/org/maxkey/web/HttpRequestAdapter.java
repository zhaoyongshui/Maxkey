/*
 * Copyright [2021] [MaxKey of copyright http://www.maxkey.top]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.maxkey.web;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.maxkey.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;

@Component
public class HttpRequestAdapter {
	private static final Logger _logger = LoggerFactory.getLogger(HttpRequestAdapter.class);

    private String mediaType = MediaType.FORM;

    public static class MediaType{
        public static String JSON   =   "JSON";
        public static String XML    =   "XML";
        public static String FORM   =   "FORM";
    }

    public HttpRequestAdapter(){}

    public HttpRequestAdapter(String mediaType){
        this.mediaType = mediaType;
    }

	public String post(String url,Map<String, Object> parameterMap) {
		HashMap<String,String> headers = new HashMap<String,String>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		return post(url , parameterMap , headers);
	}

    public String post(String url,Map<String, Object> parameterMap,HashMap<String,String> headers) {
        // ??????httpClient??????
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        // ??????httpPost??????????????????
        HttpPost httpPost = new HttpPost(url);
        // ????????????????????????
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// ????????????????????????????????????
                .setConnectionRequestTimeout(35000)// ??????????????????????????????
                .setSocketTimeout(60000)// ????????????????????????????????????
                .build();
        // ???httpPost??????????????????
        httpPost.setConfig(requestConfig);
        // ???????????????
        if (null != headers && headers.size() > 0) {
        	  Set<Entry<String, String>> entrySet = headers.entrySet();
              // ??????????????????????????????
              Iterator<Entry<String, String>> iterator = entrySet.iterator();
              while (iterator.hasNext()) {
                  Entry<String, String> mapEntry = iterator.next();
                  _logger.trace("Name " + mapEntry.getKey() + " , Value " +mapEntry.getValue());
                  httpPost.addHeader(mapEntry.getKey(), mapEntry.getValue());
              }
        }

        // ??????post????????????
        if (null != parameterMap && parameterMap.size() > 0) {
            if(mediaType.equals(MediaType.FORM)) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                // ??????map??????entrySet????????????entity
                Set<Entry<String, Object>> entrySet = parameterMap.entrySet();
                // ??????????????????????????????
                Iterator<Entry<String, Object>> iterator = entrySet.iterator();
                while (iterator.hasNext()) {
                    Entry<String, Object> mapEntry = iterator.next();
                    _logger.debug("Name " + mapEntry.getKey() + " , Value " +mapEntry.getValue());
                    nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
                }

                // ???httpPost??????????????????????????????
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else if(mediaType.equals(MediaType.JSON)) {
                String jsonString = JsonUtils.gson2Json(parameterMap);
                StringEntity stringEntity =new StringEntity(jsonString, "UTF-8");
                stringEntity.setContentType("text/json");
                httpPost.setEntity(stringEntity);


            }
            _logger.debug("Post Message \n{} ", httpPost.getEntity().toString());
        }


        try {
            // httpClient????????????post??????,???????????????????????????
            httpResponse = httpClient.execute(httpPost);
            // ????????????????????????????????????
            HttpEntity entity = httpResponse.getEntity();
            String content = EntityUtils.toString(entity);
            _logger.debug("Http Response StatusCode {} , Content {}",
                    httpResponse.getStatusLine().getStatusCode(),
                    content
            );
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // ????????????
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


	public String get(String url) {
		HashMap<String,String> headers = new HashMap<String,String>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		return get(url ,  headers);
	}

    public String get(String url,HashMap<String,String> headers) {
        // ??????httpClient??????
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        // ??????httpPost??????????????????
        HttpGet httpGet = new HttpGet(url);
        // ????????????????????????
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// ????????????????????????????????????
                .setConnectionRequestTimeout(35000)// ??????????????????????????????
                .setSocketTimeout(60000)// ????????????????????????????????????
                .build();
        // ???httpGet??????????????????
        httpGet.setConfig(requestConfig);
        // ???????????????
        if (null != headers && headers.size() > 0) {
        	  Set<Entry<String, String>> entrySet = headers.entrySet();
              // ??????????????????????????????
              Iterator<Entry<String, String>> iterator = entrySet.iterator();
              while (iterator.hasNext()) {
                  Entry<String, String> mapEntry = iterator.next();
                  _logger.trace("Name " + mapEntry.getKey() + " , Value " +mapEntry.getValue());
                  httpGet.addHeader(mapEntry.getKey(), mapEntry.getValue());
              }
        }

        try {
            // httpClient????????????post??????,???????????????????????????
            httpResponse = httpClient.execute(httpGet);
            // ????????????????????????????????????
            HttpEntity entity = httpResponse.getEntity();
            String content = EntityUtils.toString(entity);
            _logger.debug("Http Response StatusCode {} , Content {}",
                    httpResponse.getStatusLine().getStatusCode(),
                    content
            );
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // ????????????
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
