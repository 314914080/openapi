package org.jeecg.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jeecg.config.httpclient.HttpClientUtils;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author zhengbo
 * @date 2022-03-04
 */
@Slf4j
@Component
public class HttpUtil {



	public static String httpPost(String apiName, String url, List<NameValuePair> params, String json, Map<String, String> headers) {
		try {

			URIBuilder uriBuilder = new URIBuilder(url);
			if(params!=null){

				uriBuilder.addParameters(params);
			}

			HttpPost httpPost = new HttpPost(uriBuilder.build());
			CloseableHttpClient httpClient = HttpClientUtils.acceptsUntrustedCertsHttpClient();
			if (headers != null && headers.size() != 0) {
				Iterator<String> ite = headers.keySet().iterator();

				while (ite.hasNext()) {
					String key = ite.next();
					String value = headers.get(key);
					httpPost.setHeader(key, value);
				}
			}
			StringEntity strEntity = new StringEntity(json, "UTF-8");
			strEntity.setContentEncoding("UTF-8");
			strEntity.setContentType("application/json");
			httpPost.setEntity(strEntity);
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).setSocketTimeout(5000).build();
			httpPost.setConfig(requestConfig);
			log.info(apiName+"=>url:"+httpPost.getURI());
			HttpResponse resp = httpClient.execute(httpPost);
			HttpEntity entity = resp.getEntity();
			String respContent = EntityUtils.toString(entity, "UTF-8").trim();
			log.info(apiName+"=>结果:"+respContent);
			httpPost.abort();
			return respContent;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(apiName+"post异常:"+e.getMessage());
			return "请求异常:"+e.getMessage();
		}
	}


	public  static Map<String, String>  httpPostGetCookie(String apiName, String url, List<NameValuePair> params, String json, Map<String, String> headers) {
		Map<String, String> httpResult = new HashMap<>();
		try {
			CookieStore cookieStore = new BasicCookieStore();
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

			URIBuilder uriBuilder = new URIBuilder(url);
			if(params!=null){

				uriBuilder.addParameters(params);
			}

			HttpPost httpPost = new HttpPost(uriBuilder.build());
			CloseableHttpClient httpClient = HttpClientUtils.acceptsUntrustedCertsHttpClient();
			if (headers != null && headers.size() != 0) {
				Iterator<String> ite = headers.keySet().iterator();

				while (ite.hasNext()) {
					String key = ite.next();
					String value = headers.get(key);
					httpPost.setHeader(key, value);
				}
			}
			StringEntity strEntity = new StringEntity(json, "UTF-8");
			strEntity.setContentEncoding("UTF-8");
			strEntity.setContentType("application/json");
			httpPost.setEntity(strEntity);
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(50000)
					.setConnectionRequestTimeout(50000).setSocketTimeout(50000).build();
			httpPost.setConfig(requestConfig);
			log.info(apiName+"=>url:"+httpPost.getURI());
			HttpResponse response = httpClient.execute(httpPost,localContext);
			if(response.getStatusLine().getStatusCode()==200){
				List<Cookie> cookieList = cookieStore.getCookies();
				for (Cookie cookie : cookieList) {
					if("kdservice-sessionid".equals(cookie.getName())){
						httpResult.put("kdservice-sessionid", cookie.getValue());
					}
				}
			}else{
				httpResult.put("msg","token获取错误:"+EntityUtils.toString(response.getEntity()));
			}
			httpPost.abort();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(apiName+"post异常:"+e.getMessage());
			return null;
		}
		return httpResult;
	}

	public static Map<String, String> httpPostCookie(String apiName,String username, String password, String url, Map<String, String> parameters, Map<String, String> headers) {
		String charset = "UTF-8";
		// Header[] headers=null;
		Map<String, String> httpResult = new HashMap<>();
		try {
			CookieStore cookieStore = new BasicCookieStore();
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
			CloseableHttpClient httpClient = HttpClientUtils.acceptsUntrustedCertsHttpClient();
			String encoding = DatatypeConverter.printBase64Binary((username + ":" + password).getBytes("UTF-8"));
			// 创建URI并设置参数
			URIBuilder builder = new URIBuilder(url);
			if (parameters != null && !parameters.isEmpty()) {
				builder.addParameters(assemblyParameter(parameters));
			}
			builder.addParameter("$top","1");
			if (charset != null) {
				builder.setCharset(Charset.forName(charset));
			}
			URI uri = builder.build();
			// 创建HttpGet
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setHeader("Authorization", "Basic " + encoding);
			httpGet.setHeader("Accept", "application/json");
			httpGet.setHeader("sap-language", "ZH");
			// 设置Header
			if (headers != null && headers.size() != 0) {
				Iterator<String> ite = headers.keySet().iterator();
				while (ite.hasNext()) {
					String key = ite.next();
					String value = headers.get(key);
					httpGet.setHeader(key, value);
				}
			}
			log.info(apiName+"=>url:"+httpGet.getURI());
			CloseableHttpResponse response=httpClient.execute(httpGet, localContext);
			String token = response.getLastHeader("x-csrf-token").getValue();

			if(response.getStatusLine().getStatusCode()==200){
				httpResult.put("x-csrf-token", token);
				List<Cookie> cookieList = cookieStore.getCookies();
				StringBuffer Cookiestr = new StringBuffer();
				for (Cookie cookie : cookieList) {
					Cookiestr.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
				}
				httpResult.put("Cookie", Cookiestr.toString());
			}else{
				httpResult.put("msg","token获取错误:"+EntityUtils.toString(response.getEntity()));
			}
			//    log.info(apiName+"=>result:"+JSON.toJSONString(httpResult));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(apiName+"token获取异常:"+e.getMessage());
			httpResult.put("msg","token获取异常:"+e.getMessage());
		}
		return httpResult;
	}
	/**
	 * 处理请求参数
	 *
	 * @param parameters
	 * @return
	 */
	private static List<NameValuePair> assemblyParameter(Map<String, String> parameters) {
		List<NameValuePair> allParameter = new ArrayList<>();
		if (parameters != null && !parameters.isEmpty()) {
			for (String name : parameters.keySet()) {
				NameValuePair parameter = new BasicNameValuePair(name, parameters.get(name));
				allParameter.add(parameter);
			}
		}
		return allParameter;
	}

}
