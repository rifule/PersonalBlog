# 📝 Personal Blog

[![Vue](https://img.shields.io/badge/Vue-3.4.21-4FC08D?logo=vue.js)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.5-6DB33F?logo=spring-boot)](https://spring.io/projects/spring-boot)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.4.5-3178C6?logo=typescript)](https://www.typescriptlang.org/)
[![Java](https://img.shields.io/badge/Java-17-007396?logo=java)](https://www.java.com/)

> 一个基于 Vue 3 + Spring Boot 的现代化个人博客系统。

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
├── blog-frontend/          # 前端项目 (Vue 3 + TypeScript + Vite)
│   ├── src/
│   │   ├── api/           # API 接口封装
│   │   ├── components/    # 公共组件
│   │   ├── composables/   # 组合式 API (Hooks)
│   │   ├── layouts/       # 布局组件
│   │   ├── router/        # 路由配置
│   │   ├── stores/        # Pinia 状态管理
│   │   ├── styles/        # 全局样式
│   │   ├── types/         # TypeScript 类型定义
│   │   ├── utils/         # 工具函数
│   │   └── views/         # 页面视图
│   └── package.json
│
└── blog-backend/           # 后端项目 (Spring Boot + Maven)
    ├── blog-api/          # API 接口层 (Controller)
    ├── blog-application/  # 应用服务层 (Service)
    ├── blog-domain/       # 领域层 (Entity, DTO, VO, Repository)
    ├── blog-infrastructure/# 基础设施层 (Config, Security)
    └── blog-common/       # 公共模块 (Result, Exception, Utils)
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
| Spring Boot | 3.0.5 | Java 应用框架 |
| MyBatis Plus | 3.5.3.1 | ORM 框架 |
| JWT | 4.4.0 | JSON Web Token |
| MySQL | 8.0+ | 关系型数据库 |
| Maven | 3.8+ | 项目构建工具 |

##  项目结构详解

### 前端结构

```
blog-frontend/src/
├── api/                    # API 接口
│   ├── article.ts         # 文章接口
│   ├── auth.ts            # 认证接口
│   ├── category.ts        # 分类接口
│   ├── tag.ts             # 标签接口
│   ├── comment.ts         # 评论接口
│   └── user.ts            # 用户接口
├── components/            # 公共组件
│   ├── AppHeader.vue      # 顶部导航
│   ├── AppFooter.vue      # 页脚
│   ├── UserCard.vue       # 用户信息卡片
│   ├── TagCloud.vue       # 标签云
│   ├── CategoryList.vue  # 分类列表
│   ├── CalendarWidget.vue # 日历组件
│   ├── ThemeToggle.vue    # 主题切换
│   ├── ThemeSettings.vue  # 主题设置
│   └── BackToTop.vue      # 回到顶部
├── composables/           # 组合式 API (Hooks)
│   ├── useApi.ts          # API 请求封装
│   ├── useFetch.ts        # 数据获取封装
│   ├── usePagination.ts   # 分页逻辑
│   ├── useTheme.ts        # 主题管理
│   ├── useLoading.ts      # 加载状态
│   ├── useCalendar.ts     # 日历逻辑
│   ├── useEventBus.ts     # 事件总线
│   ├── useProvide.ts      # 依赖注入
│   └── useCommon.ts       # 通用工具
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
├── types/                 # TypeScript 类型
│   └── index.ts           # 类型定义
├── utils/                 # 工具函数
│   ├── request.ts         # HTTP 请求封装
│   ├── markdown.ts        # Markdown 渲染
│   └── url.ts             # URL 处理
└── views/                 # 页面视图
    ├── home/              # 首页相关
    │   ├── HomeView.vue          # 首页
    │   ├── AboutView.vue         # 关于页
    │   ├── SearchView.vue        # 搜索页
    │   ├── CategoriesView.vue   # 分类列表页
    │   ├── CategoryView.vue     # 分类文章页
    │   ├── TagsView.vue          # 标签列表页
    │   └── TagView.vue           # 标签文章页
    ├── article/           # 文章相关
    │   └── ArticleDetail.vue     # 文章详情
    └── admin/             # 后台管理
        ├── LoginView.vue         # 登录页
        ├── DashboardView.vue     # 仪表盘
        ├── ArticleManage.vue     # 文章管理
        ├── ArticleEdit.vue       # 文章编辑
        ├── CategoryManage.vue    # 分类管理
        ├── TagManage.vue         # 标签管理
        └── UserSettings.vue      # 用户设置
```

### 后端结构

```
blog-backend/
├── blog-api/              # API 接口层
│   └── controller/        # REST 控制器
│       ├── ArticleController.java     # 文章接口
│       ├── AuthController.java       # 认证接口
│       ├── CategoryController.java    # 分类接口
│       ├── TagController.java        # 标签接口
│       └── UserController.java       # 用户接口
├── blog-application/      # 应用服务层
│   └── service/           # 业务逻辑
│       ├── ArticleService.java       # 文章服务
│       ├── CategoryService.java      # 分类服务
│       ├── TagService.java           # 标签服务
│       ├── UserService.java          # 用户服务
│       └── FileService.java          # 文件服务
├── blog-domain/           # 领域层
│   ├── entity/            # 实体类
│   │   ├── Article.java          # 文章实体
│   │   ├── ArticleTag.java       # 文章标签关联
│   │   ├── Category.java        # 分类实体
│   │   ├── Tag.java              # 标签实体
│   │   ├── Comment.java          # 评论实体
│   │   └── User.java             # 用户实体
│   ├── dto/               # 数据传输对象
│   │   ├── ArticleDTO.java       # 文章 DTO
│   │   ├── LoginDTO.java         # 登录 DTO
│   │   └── UserDTO.java          # 用户 DTO
│   ├── vo/                # 视图对象
│   │   ├── ArticleListVO.java    # 文章列表 VO
│   │   ├── ArticleVO.java        # 文章详情 VO
│   │   └── UserVO.java           # 用户 VO
│   ├── enums/             # 枚举类
│   │   ├── FileType.java         # 文件类型
│   │   └── UserRole.java         # 用户角色
│   └── repository/        # 数据访问层
│       ├── ArticleRepository.java     # 文章仓储
│       ├── ArticleTagRepository.java  # 文章标签仓储
│       ├── CategoryRepository.java    # 分类仓储
│       ├── TagRepository.java         # 标签仓储
│       └── UserRepository.java        # 用户仓储
├── blog-infrastructure/   # 基础设施层
│   ├── config/            # 配置类
│   │   ├── Knife4jConfig.java      # API 文档配置
│   │   ├── MyBatisPlusConfig.java  # MyBatis Plus 配置
│   │   ├── MyMetaObjectHandler.java # 自动填充配置
│   │   ├── SecurityConfig.java     # 安全配置
│   │   └── WebMvcConfig.java       # Web MVC 配置
│   └── security/          # 安全相关
│       ├── JwtAuthenticationFilter.java # JWT 认证过滤器
│       ├── CustomUserDetails.java      # 自定义用户详情
│       ├── SecurityUtils.java          # 安全工具类
│       └── UserDetailsServiceImpl.java # 用户详情服务
└── blog-common/           # 公共模块
    ├── exception/         # 异常处理
    │   ├── BusinessException.java         # 业务异常
    │   └── GlobalExceptionHandler.java    # 全局异常处理
    ├── result/           # 统一响应
    │   ├── Result.java          # 统一结果
    │   └── ResultCode.java     # 响应码
    └── utils/             # 工具类
        ├── JwtUtil.java        # JWT 工具
        └── PageResult.java    # 分页结果
```