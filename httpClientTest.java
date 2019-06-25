package pub;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class httpClientTest {
    private static final Logger logger = LogManager.getLogger(httpClientTest.class);
    HttpClient httpClient= new HttpClient();

    private int loginRequest(String loginUrl,String userName,String password) throws Exception {
        if(logger.isDebugEnabled()){
            logger.debug("loginRequest userName{},password{}",userName,password);
        }
        // 模拟登陆，按实际服务器端要求选用 Post 或 Get 请求方式
        PostMethod postMethod = new PostMethod(loginUrl);
        // 设置登陆时要求的信息，用户名和密码 key对应登录页面输入框的name属性
        NameValuePair[] data = {
                new NameValuePair("username", "***"),
                new NameValuePair("password", "***")};
        postMethod.setRequestBody(data);
        // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        int statusCode=httpClient.executeMethod(postMethod);
        if(logger.isDebugEnabled()){
            logger.debug("loginRequest statusCode{}",statusCode);
        }
        return statusCode;
    }
    private String dataRequest(String dataUrl) throws Exception {
        // 获得登陆后的 Cookie
        Cookie[] cookies = httpClient.getState().getCookies();
        StringBuffer tmpcookies = new StringBuffer();
        for (Cookie c : cookies) {
            tmpcookies.append(c.toString() + ";");
            if(logger.isDebugEnabled()){
                logger.debug("dataRequest cookies{}",c.toString());
            }
        }
        // 进行登陆后的操作，如果是post请求，要改成对应的对象来处理
        GetMethod getMethod = new GetMethod(dataUrl);
        // 每次访问需授权的网址时需带上前面的 cookie 作为通行证
        getMethod.setRequestHeader("cookie", tmpcookies.toString());
        // 你还可以通过 PostMethod/GetMethod 设置更多的请求后数据
        // 例如，referer 从哪里来的，UA 像搜索引擎都会表明自己是谁，无良搜索引擎除外
        //postMethod.setRequestHeader("Referer", "http://passport.mop.com/");
        //postMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
        httpClient.executeMethod(getMethod);
        String result = getMethod.getResponseBodyAsString();
        if(logger.isDebugEnabled()){
            logger.debug("dataRequest result{}",result);
        }
        return result;
    }
}
