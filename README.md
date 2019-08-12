[![GitHub release](https://img.shields.io/badge/release-1.0.0-28a745.svg)](https://github.com/0nebean/com.alibaba.druid-0nebean.custom/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

![](https://img.shields.io/badge/belong_to-chemical--el-yellowgreen.svg)
![](https://img.shields.io/badge/support-onebean--data-red.svg)
![](https://img.shields.io/badge/dependency-spring--15.20-blue.svg)
![](https://img.shields.io/badge/middleware-mysql-lightgrey.svg)



Introduction
---
- 一言蔽之 (带有权限控制和代码生成的TMA架构SaaS结构后台管理系统)


- 框架特性[(查看明细)](https://github.com/0nebean/Aluminium/wiki/%E6%A1%86%E6%9E%B6%E7%89%B9%E6%80%A7)
  - 基于[Aluminium](https://0nebean.github.io/Aluminium/)
  - SaaS租户之间的数据沙箱完全隔离，用户，角色，菜单等数据分表独立存储
  - SaaS系统租户的超管账号由云外的[控制台](https://baidu.com)下发，并管控账号状态
  - 基于Aluminium 的代码生成功能，一次生成针对所有的租户开发
  - 基于Aluminium 的spring security权限控制层最大权限由每个SaaS租户的超管账号分配，个体权限分配互不影响
  
 
Documentation
---
[Aluminium Documentation](https://github.com/0nebean/Aluminium/wiki)
