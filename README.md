# 📝 Personal Blog

[![Vue](https://img.shields.io/badge/Vue-3.4.21-4FC08D?logo=vue.js)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.5-6DB33F?logo=spring-boot)](https://spring.io/projects/spring-boot)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.4.5-3178C6?logo=typescript)](https://www.typescriptlang.org/)
[![Java](https://img.shields.io/badge/Java-17-007396?logo=java)](https://www.java.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> 一个基于 Vue 3 + Spring Boot 的现代化个人博客系统，采用 GitHub 风格设计。

![Blog Preview](https://via.placeholder.com/800x400/1a1a2e/ffffff?text=Blog+Preview)

## ✨ 特性

- 🎨 **GitHub 风格设计** - 简洁优雅的界面风格
- 🌙 **明暗主题切换** - 支持亮色/暗色模式
- 📝 **Markdown 支持** - 完整的 Markdown 渲染和代码高亮
- 🏷️ **标签分类** - 文章标签和分类管理
- 🔍 **全文搜索** - 支持文章标题和内容搜索
- 📅 **日历归档** - 文章发布日历展示
- 👤 **用户管理** - 管理员登录和头像上传
- 📱 **响应式设计** - 适配移动端和桌面端

## 🏗️ 项目架构

```
PersonalBlog/
├── blog-frontend/          # 前端项目 (Vue 3 + TypeScript)
│   ├── src/
│   │   ├── api/           # API 接口封装
│   │   ├── components/    # 公共组件
│   │   ├── layouts/       # 布局组件
│   │   ├── router/        # 路由配置
│   │   ├── stores/        # Pinia 状态管理
│   │   ├── styles/        # 全局样式
│   │   ├── utils/         # 工具函数
│   │   └── views/         # 页面视图
│   └── package.json
│
└── blog-backend/           # 后端项目 (Spring Boot)
    ├── blog-api/          # API 接口层
    ├── blog-application/  # 应用服务层
    ├── blog-domain/       # 领域层 (实体、DTO、VO)
    ├── blog-infrastructure/# 基础设施层
    ├── blog-common/       # 公共模块
    └── pom.xml
```

## 🛠️ 技术栈

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| [Vue](https://vuejs.org/) | 3.4.21 | 渐进式 JavaScript 框架 |
| [Vue Router](https://router.vuejs.org/) | 4.3.0 | 官方路由管理器 |
| [Pinia](https://pinia.vuejs.org/) | 2.1.7 | 状态管理库 |
| [Element Plus](https://element-plus.org/) | 2.6.3 | UI 组件库 |
| [TypeScript](https://www.typescriptlang.org/) | 5.4.5 | 类型安全的 JavaScript |
| [Vite](https://vitejs.dev/) | 5.2.8 | 下一代前端构建工具 |
| [Axios](https://axios-http.com/) | 1.6.8 | HTTP 客户端 |
| [Marked](https://marked.js.org/) | 12.0.1 | Markdown 解析器 |
| [Highlight.js](https://highlightjs.org/) | 11.9.0 | 代码语法高亮 |
| [Day.js](https://day.js.org/) | 1.11.10 | 日期处理库 |

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| [Spring Boot](https://spring.io/projects/spring-boot) | 3.0.5 | Java 应用框架 |
| [MyBatis Plus](https://baomidou.com/) | 3.5.3.1 | ORM 框架 |
| [JWT](https://github.com/auth0/java-jwt) | 4.4.0 | JSON Web Token |
| [Knife4j](https://doc.xiaominfo.com/) | 4.5.0 | API 文档工具 |
| [MySQL](https://www.mysql.com/) | 8.0+ | 关系型数据库 |
| [Maven](https://maven.apache.org/) | 3.8+ | 项目构建工具 |

## 🚀 快速开始

### 环境要求

- **Node.js** >= 18.0.0
- **Java** >= 17
- **MySQL** >= 8.0

### 1. 克隆项目

```bash
git clone https://github.com/yourusername/PersonalBlog.git
cd PersonalBlog
```

### 2. 数据库配置

创建 MySQL 数据库并执行初始化脚本：

```bash
mysql -u root -p < blog-backend/blog-api/src/main/resources/init.sql
```

### 3. 启动后端服务

```bash
cd blog-backend
mvn clean install
mvn spring-boot:run -pl blog-api
```

后端服务默认运行在 `http://localhost:8080`

API 文档地址：`http://localhost:8080/doc.html`

### 4. 启动前端服务

```bash
cd blog-frontend
npm install
npm run dev
```

前端服务默认运行在 `http://localhost:5173`

### 5. 访问博客

打开浏览器访问 `http://localhost:5173`
## 📁 项目结构详解

### 前端结构

```
blog-frontend/src/
├── api/                    # API 接口
│   ├── article.ts         # 文章接口
│   ├── auth.ts            # 认证接口
│   ├── category.ts        # 分类接口
│   ├── tag.ts             # 标签接口
│   └── user.ts            # 用户接口
├── components/            # 公共组件
│   ├── AppHeader.vue      # 顶部导航
│   ├── AppFooter.vue      # 页脚
│   ├── UserCard.vue       # 用户信息卡片
│   ├── BackToTop.vue      # 回到顶部
│   └── ...
├── layouts/               # 布局组件
│   ├── DefaultLayout.vue  # 默认布局
│   ├── AdminLayout.vue    # 后台布局
│   └── ArticleLayout.vue  # 文章详情布局
├── router/                # 路由配置
│   └── index.ts
├── stores/                # 状态管理
│   ├── theme.ts           # 主题状态
│   └── user.ts            # 用户状态
├── styles/                # 全局样式
│   ├── global.scss        # 全局样式
│   └── variables.scss     # 变量定义
├── utils/                 # 工具函数
│   ├── request.ts         # HTTP 请求封装
│   ├── markdown.ts        # Markdown 渲染
│   └── url.ts             # URL 处理
└── views/                 # 页面视图
    ├── home/              # 首页相关
    ├── article/           # 文章相关
    └── admin/             # 后台管理
```

### 后端结构

```
blog-backend/
├── blog-api/              # API 接口层
│   └── controller/        # REST 控制器
├── blog-application/      # 应用服务层
│   └── service/           # 业务逻辑
├── blog-domain/           # 领域层
│   ├── entity/            # 实体类
│   ├── dto/               # 数据传输对象
│   ├── vo/                # 视图对象
│   └── repository/        # 数据访问
├── blog-infrastructure/   # 基础设施层
│   ├── config/            # 配置类
│   └── security/          # 安全相关
└── blog-common/           # 公共模块
    ├── exception/         # 异常处理
    ├── result/            # 统一响应
    └── utils/             # 工具类
```

## 🔧 配置说明

### 后端配置

编辑 `blog-backend/blog-api/src/main/resources/application-dev.yml`：

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver

  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

jwt:
  secret: your_secret_key
  expiration: 2592000000  # 30天

file:
  upload:
    path: ./uploads
```

### 前端配置

编辑 `blog-frontend/.env`：

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

## 📸 功能截图

| 首页 | 文章详情 | 后台管理 |
|------|----------|----------|
| ![Home](https://via.placeholder.com/300x200/1a1a2e/ffffff?text=Home) | ![Article](https://via.placeholder.com/300x200/1a1a2e/ffffff?text=Article) | ![Admin](https://via.placeholder.com/300x200/1a1a2e/ffffff?text=Admin) |

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目基于 [MIT](LICENSE) 许可证开源。

## 🙏 致谢

- [Vue.js](https://vuejs.org/)
- [Element Plus](https://element-plus.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [MyBatis Plus](https://baomidou.com/)

---

<p align="center">
  Made with ❤️ by <a href="https://github.com/yourusername">Your Name</a>
</p>
"# PersonalBlog" 
