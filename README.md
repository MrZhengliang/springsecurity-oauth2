# springsecurity-oauth2
springsecurity-oauth2集成，jwt生成token

##本地部署调试
 - 1.目前支持password+jwt生成token
 - 2.可以反解析jwt内容
 - 3.获取token:http://127.0.0.1:8601/oauth/token?username=user1&password=123456&type=password&client_id=test&client_secret=test1234&grant_type=password
 - 4.反解析token:http://127.0.0.1:8601/jwt/check ;
    header配置Content-Type:application/json; Authorization:上一步获取的access_token值
![image](解析jwt生成的token.png)
## sql脚本
````SQL
oauth.sql
````

