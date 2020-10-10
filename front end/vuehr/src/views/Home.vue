<template>
    <div class="homeMian">
        <div class="imgBg">
            <img :src="imgSrc" width="100%" height="100%" alt="">
        </div>
        <el-container>
            <el-header class="homeHeader">
                <div class="title">Hr</div>

                <!--右边个人信息栏Started-->
                <div>
                    <!--消息按钮-->
                    <el-button style="margin-right: 10px;
                               color: #000000"
                               size="normal"
                               type="text"
                               icon="el-icon-chat-dot-square" @click="goChat">

                    </el-button>
                    <el-dropdown class="userInfo" @command="commandHandler">
  <span class="el-dropdown-link">
    {{user.name}}<i><img :src="user.userface" alt=""></i>
  </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="userInfo">个人中心</el-dropdown-item>
                            <el-dropdown-item command="setting">设置</el-dropdown-item>
                            <!--divided:表示一条分隔线-->
                            <el-dropdown-item command="logout" divided>注销登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
                <!--右边个人信息栏end-->
            </el-header>

            <el-container>
                <el-aside width="200px">
                    <!--router添加了该属性 它会使用 index来自动进行菜单页的页面跳转
                    无需使用点击事件
                    unique-opened:点击菜单栏一次只展开一级菜单
                    -->
                    <el-menu router unique-opened>
                        <!--options: 拿到的就是index.js中的 routes数组数据,再进行渲染出来
                        其中有hidden属性的要它为 false的时候才会渲染它
                        -->
                        <el-submenu :index="index+''" v-for="(item,index) in routes" v-if="!item.hidden" :key="index">
                            <template slot="title">
                                <i style="color: blue;margin-right: 5px" :class="item.iconCls"></i>
                                <span>{{item.name}}</span>
                            </template>
                            <el-menu-item :index="child.path" v-for="(child,indexj) in item.children" :key="indexj"><i
                                    style="color: coral;margin-right: 5px" :class="child.iconCls"></i>
                                {{child.name}}
                            </el-menu-item>
                        </el-submenu>
                    </el-menu>

                </el-aside>
                <el-main>
                    <!--主页菜单导航栏-->
                    <el-breadcrumb separator="/" v-if="this.$router.currentRoute.path!='/home'">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <!--获取当前路由对象并拿到他的name属性-->
                        <el-breadcrumb-item>{{this.$router.currentRoute.name}}</el-breadcrumb-item>
                    </el-breadcrumb>
                    <div class="homeWelcome" v-if="this.$router.currentRoute.path=='/home'">
                        欢迎使用本系统φ(゜▽゜*)♪φ(゜▽゜*)♪！
                    </div>
                    <!--展示菜单栏点击后触发的页面-->
                    <router-view class="homeRouterView"/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "Home",
        data() {
            return {
                //将保存在sessionStorage字符串化的用用户信息转成一个用户对象
                user: JSON.parse(window.sessionStorage.getItem("user")),
                imgSrc: require('../assets/images/bg2.jpg')
            }

        },
        computed: {
            routes() {
                return this.$store.state.routes;
            },
            /*user(){
                //计算属性定义数据共享,做数据同步
                return this.$store.state.currentHr;
            }*/
        },
        methods: {

            //聊天goChat
            goChat(){
                //点击后前往聊天页面
              this.$router.push("/chat");
            },
            commandHandler(cmd) {
                if (cmd == 'logout') {
                    this.$confirm('您即将进行用户注销操作, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        //注销登录请求调用服务端接口
                        this.getRequest("/logout");
                        //注销登录后 用户数据清空 再跳转到登录页
                        window.sessionStorage.removeItem("user");
                        //更换不同用户登录时清空之前登录的缓存
                        this.$store.commit('initRoutes', []);
                        this.$router.replace("/")
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消当前操作o(*￣▽￣*)ブ'
                        });
                    });
                } else if (cmd == 'userInfo'){
                    this.$router.push('/hrinfo');
                }

            }
        }
    }
</script>

<style>

    .homeRouterView {
        margin-top: 10px;
    }

    .homeHeader {
        background-color: brown;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0px 15px;
        box-sizing: border-box;
    }

    .homeHeader .title {
        font-size: 30px;
        font-family: 仿宋;
        font-weight: bold;
        color: #fff;
    }

    .homeHeader .userInfo {
        /*鼠标放上去为手指状态*/
        cursor: pointer;
    }

    .el-dropdown-link img {
        width: 48px;
        height: 48px;
        border-radius: 24px;
        margin-left: 8px;
    }

    .el-dropdown-link {
        display: flex;
        align-items: center;
        color: beige;
    }

    /*背景图片*/
    .imgBg {
        width: 100%;
        height: 100%;
        z-index: -1;
        position: absolute;

    }

    .homeMian {
        width: 100%;
        height: 100%;
        z-index: 1;
        position: absolute;
    }

    .homeWelcome {
        text-align: center;
        font-size: 45px;
        font-family: 华文行楷;
        color: white;
    }

</style>