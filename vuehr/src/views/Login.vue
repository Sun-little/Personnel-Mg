<template>

    <div>

      <div class="logo-img">
        <img :src="logoImg" width="100%" height="100%" alt="">
      </div>

        <div class="background-img">
            <img :src="imgSrc" width="100%" height="100%" alt="">
        </div>
        <div class="doFrom">

            <!--:model:存放着用户登录的身份信息-->
            <!--loginFrom: 等于是表单的名字 和 id是一样的意思-->
            <el-form
                    :rules="rules"
                    ref="loginFrom"
                    v-loading="loading"
                    element-loading-text="正在登录中~ ~"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    :model="loginForm"
                    class="loginContainer">
              <span class="logo"><img :src="logoSrc" width="100%" height="100%" alt=""></span>
                <!--<h3 class="loginTitle">Hr登录</h3>-->
                <el-form-item prop="username">
                    <el-input prefix-icon="el-icon-user"
                              class="input-One" size="normal"
                              type="text" v-model="loginForm.username"
                              auto-complete="off" placeholder="请输入用户名~~"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input class="input-Two"
                              prefix-icon="el-icon-edit-outline"
                              show-password
                              size="normal" type="password"
                              v-model="loginForm.password"
                              auto-complete="off" placeholder="请输入用户密码~~">

                    </el-input>
                </el-form-item>

                <el-form-item prop="code">
                    <el-input class="input-Three"
                              prefix-icon="el-icon-document-checked"
                              size="normal" type="text"
                              v-model="loginForm.code"
                              auto-complete="off" placeholder="看不清? 请点击校验码刷新~~"
                              @keydown.enter.native="submitLogin"
                              style="width: 250px;margin-right: 3px">
                    </el-input>
                    <img :src="vcUrl" @click="updateVerifyCode">
                </el-form-item>
                <el-checkbox size="normal" class="loginRemember" v-model="checked">记住我！</el-checkbox>
                <el-button size="normal" type="primary" style="width: 100%;" @click="submitLogin">登录</el-button>
            </el-form>
        </div>
    </div>
</template>


<script>
    export default {
        name: "Login",
        data() {
            return {
                //登录页背景图片
                imgSrc: require('../assets/images/login-bg.jpg'),
                logoSrc: require('../assets/images/login-logo.png'),
                logoImg: require('../assets/images/logo.png'),

                loading: false,
                vcUrl: '/verifyCode?time='+ new Date(),
                //默认用户名密码
                loginForm: {
                    username: 'admin',
                    password: '666',
                    code:''
                },
                checked: true,
                //下面配置的是rules登录的校验规则
                rules: {
                    //trigger: 触发条件
                    username: [{required: true, message: '请先完成用户名の输入(；′⌒`)', trigger: 'blur'}],
                    password: [{required: true, message: '请先完成密码の输入(；′⌒`)', trigger: 'blur'}],
                    code: [{required: true, message: '请先完成校验码の输入(；′⌒`)', trigger: 'blur'}]
                }
            }

        },
        methods: {
            //校验码刷新
            updateVerifyCode(){
                this.vcUrl = '/verifyCode?time='+ new Date();
            },
            submitLogin() {
                this.$refs.loginFrom.validate((valid) => {
                    if (valid) {
                        this.loading = true;
                        //resp为服务端返回的参数
                        this.postKeyValueRequest('/doLogin', this.loginForm).then(resp => {
                            this.loading = false;
                            //如果登录成功
                            if (resp) {
                                //这句语句用于初始化 store下index.js中的currentHr;
                                this.$store.commit('INIT_CURRENTHR', resp.obj);
                                //登陆成功后的用户信息以字符串的形式保存在sessionStorage中。
                                window.sessionStorage.setItem("user", JSON.stringify(resp.obj));
                                let path = this.$route.query.redirect;
                                //页面跳转(如果path= / 或者 未定义路径就去home页, 否则就去对应的path下)
                                this.$router.replace((path == '/' || path == undefined) ? '/home' : path);
                            }else {
                                //如诺登陆失败, 校验码自动刷新一次
                                this.vcUrl = '/verifyCode?time='+ new Date();
                            }
                        })
                    } else {
                        this.$message.error('请先完成用户名密码或校验码的输入🤭');
                        return false;
                    }
                });
            }
        }
    }
</script>

<style>

    .input-One /deep/ .el-input__inner {
        background-color: transparent;
        color: black;
    }

    .input-Two /deep/ .el-input__inner {
        background-color: transparent;
        color: black;
    }

    /*背景图片*/
    .background-img {
        width: 100%;
        height: 100%;
        z-index: -2;
        position: absolute;

    }

    .doFrom {
        z-index: -1;
        position: absolute;
        margin: 0px 550px 0px 550px;
    }

    .loginContainer {
        border-radius: 18px;
        background-clip: padding-box;
        margin: 180px auto;
        width: 350px;
        padding: 15px 35px 15px 35px;
        border: 0px solid #eaeaea;
        box-shadow: 0 0 25px #5793cc;
    }

    .logo-img{
      background-color: #8cc5ff;
      z-index: 1;
      position: absolute;
      margin: 90px 550px 0px 702px;
      width: 120px;
      height: 120px;
      border-radius: 60px;
    }
    /*.loginTitle {
        margin: 15px auto 20px auto;
        text-align: center;
        color: white;
    }*/

    .loginRemember {
        text-align: left;
        margin: 0 0 15px 0;
    }

    .el-form-item__content{
        display: flex;
        align-items: center;
    }

</style>