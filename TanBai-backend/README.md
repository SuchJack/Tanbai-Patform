# 快速启动

> 环境：MySQL、Redis、Nacos

1. 执行db目录下的SQL

2. 将`nacos`目录下的`shared-redis.yaml`和`tanbai-service-dev.yaml`上传到Nacos配置列表中。
> 需要将文件内的配置信息替换为自己的。

3. 将`resources`目录下的`bootstrap.yaml.temp`重命名为`bootstrap.yaml`并替换为自己的配置信息。

4. 启动项目

> Tips：
> `PosterConstant`常量`public static final String DEFAULT_POSTER_BACKGROUND = "https://pic-tanbai.moshanghongwangluo.com/user_avatar/default_poster/bg2.jpg";` 为海报背景图URL。
>
> 默认海报背景图URL需替换为自己的图片URL（海报本地文件在`resources/poster`目录下），同时需要在微信小程序后台配置域名白名单）