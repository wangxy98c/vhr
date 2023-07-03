# vhr 练手 
这是一个练手项目，源项目vhr地址
https://github.com/lenve/vhr 

与源项目不同的地方主要在于前端，本项目前端使用了更新的 vue3 + ts + pinia 。使用组合式API

本人未系统学习过ts/js。前端代码并不规范（ts当js写了，全是any）。仅作为参考使用。
本项目后端代码基本与原项目一致。区别在于原项目将各个模块进行了拆分。本项目并没有进行拆分模块

另外聊天页面使用了vue-chat项目： https://github.com/Coffcer/vue-chat  （感谢）
对其进行了改造，使其适配于本项目。

前端：vue3 \ ts \ vue-router \ pinia \ element-ui \ axios \
后端：spring boot 2.7 \ druid \ redis \ RabbitMq \ 

需要自行使用docker部署RabbitMq。
docker-image 为：   rabbitmq:3-management
(docker run -d --hostname my-rabbit --name my-rabbit -p 15672:15672 rabbitmq:3-management)
