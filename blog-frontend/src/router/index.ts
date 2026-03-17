import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Layout',
      component: () => import('@/layouts/DefaultLayout.vue'),
      children: [
        {
          path: '',
          name: 'Home',
          component: () => import('@/views/home/HomeView.vue')
        },
        {
          path: 'categories',
          name: 'Categories',
          component: () => import('@/views/home/CategoriesView.vue')
        },
        {
          path: 'category/:id',
          name: 'CategoryArticles',
          component: () => import('@/views/home/CategoryView.vue')
        },
        {
          path: 'tags',
          name: 'Tags',
          component: () => import('@/views/home/TagsView.vue')
        },
        {
          path: 'tag/:id',
          name: 'TagArticles',
          component: () => import('@/views/home/TagView.vue')
        },
        {
          path: 'search',
          name: 'Search',
          component: () => import('@/views/home/SearchView.vue')
        },
        {
          path: 'about',
          name: 'About',
          component: () => import('@/views/home/AboutView.vue')
        }
      ]
    },
    {
      path: '/article/:id',
      name: 'ArticleDetail',
      component: () => import('@/layouts/ArticleLayout.vue'),
      children: [
        {
          path: '',
          component: () => import('@/views/article/ArticleDetail.vue')
        }
      ]
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/admin/LoginView.vue'),
      meta: { guest: true }
    },
    {
      path: '/admin',
      name: 'Admin',
      component: () => import('@/layouts/AdminLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'AdminDashboard',
          component: () => import('@/views/admin/DashboardView.vue')
        },
        {
          path: 'articles',
          name: 'AdminArticles',
          component: () => import('@/views/admin/ArticleManage.vue')
        },
        {
          path: 'articles/create',
          name: 'AdminArticleCreate',
          component: () => import('@/views/admin/ArticleEdit.vue')
        },
        {
          path: 'articles/edit/:id',
          name: 'AdminArticleEdit',
          component: () => import('@/views/admin/ArticleEdit.vue')
        },
        {
          path: 'categories',
          name: 'AdminCategories',
          component: () => import('@/views/admin/CategoryManage.vue')
        },
        {
          path: 'tags',
          name: 'AdminTags',
          component: () => import('@/views/admin/TagManage.vue')
        },
        {
          path: 'settings',
          name: 'AdminSettings',
          component: () => import('@/views/admin/UserSettings.vue')
        }
      ]
    }
  ]
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  const userStore = useUserStore()

  // 需要登录的页面
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/login')
    return
  }

  // 游客专属页面（如登录页）
  if (to.meta.guest && userStore.isLoggedIn) {
    next('/')
    return
  }

  next()
})

export default router
