<template>
    <div class="mainCard">

        <!--个人资料展示面板 started-->
        <el-card class="box-card" style="width: 550px">
            <div slot="header" class="clearfix">
                <span style="color: #409eff">{{hr.name+'の资料面板'}}</span>
                <el-button @click="closeBtn" type="text" icon="el-icon-close"></el-button>
            </div>
            <div>
                <div style="display: flex;justify-content: center">
                    <!--头像上传 Started-->
                    <el-upload
                            :show-file-list="false"
                            :on-success="onSuccess"
                            :data="hr"
                            action="/hr/userface">
                        <img title="点击进行用户图像の修改操作" :src="hr.userface"
                             style="width: 100px;height: 100px;border-radius: 50px" alt="">
                    </el-upload>
                    <!--头像上传 end-->
                </div>
                <div style="display: flex;justify-content: center;margin-top: 10px">电话号码:
                    <el-tag style="margin-left: 6px" type="success">{{hr.telephone}}</el-tag>
                </div>
                <div style="display: flex;justify-content: center;margin-top: 10px">手机号码:
                    <el-tag style="margin-left: 6px" type="success">{{hr.phone}}</el-tag>
                </div>
                <div style="display: flex;justify-content: center;margin-top: 10px">居住地址:
                    <el-tag style="margin-left: 6px" type="success">{{hr.address}}</el-tag>
                </div>
                <div style="display: flex;justify-content: center;margin-top: 10px">用户标签:
                    <el-tag style="margin-left: 6px; margin-right: 5px" type="success" v-for="(r,index) in hr.roles"
                            :key="index">{{r.nameZh}}
                    </el-tag>
                </div>
                <div class="ElBtn">
                    <el-button type="primary" @click="showUpdateHrInfoView">个人信息の修改操作</el-button>
                    <el-button type="warning" @click="showUpdatePasswordView">密码の修改操作</el-button>
                </div>
            </div>
        </el-card>
        <!--个人资料展示面板 end-->

        <!--个人信息修改面板 started-->
        <el-dialog
                title="个人信息の修改操作面板"
                :visible.sync="dialogVisible"
                width="30%">
            <div style="display: flex;justify-content: center">
                <table>
                    <tr>
                        <td>
                            <el-tag>电话号码:</el-tag>
                        </td>
                        <td>
                            <el-input prefix-icon="el-icon-edit-outline" v-model="hr2.telephone"></el-input>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag>手机号码:</el-tag>
                        </td>
                        <td>
                            <el-input prefix-icon="el-icon-edit-outline" v-model="hr2.phone"></el-input>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag>现居住地:</el-tag>
                        </td>
                        <td>
                            <el-input prefix-icon="el-icon-edit-outline" v-model="hr2.address"></el-input>
                        </td>
                    </tr>
                </table>
            </div>
            <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="updateHrInfo">确 定</el-button>
  </span>
        </el-dialog>
        <!--个人信息修改面板 end-->

        <!--密码修改面板 started-->
        <el-dialog
                title="密码の修改操作面板"
                :visible.sync="passWordDialogVisible"
                width="30%">
            <div>
                <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px"
                         class="demo-ruleForm">
                    <el-form-item label="请输入旧密码" prop="pass">
                        <el-input prefix-icon="el-icon-edit-outline" type="password" v-model="ruleForm.oldPass"
                                  autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="请输入新密码" prop="pass">
                        <el-input prefix-icon="el-icon-edit-outline" type="password" v-model="ruleForm.pass"
                                  autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="请确认密码" prop="checkPass">
                        <el-input prefix-icon="el-icon-edit-outline" type="password" v-model="ruleForm.checkPass"
                                  autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
                        <el-button @click="resetForm('ruleForm')">重置</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-dialog>
        <!--密码修改面板 end-->
    </div>
</template>

<script>
    export default {
        name: "HrInfo",
        data() {
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.ruleForm.checkPass !== '') {
                        this.$refs.ruleForm.validateField('checkPass');
                    }
                    callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (value !== this.ruleForm.pass) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                hr: null,
                hr2: null,
                dialogVisible: false,
                passWordDialogVisible: false,
                ruleForm: {
                    oldPass: '',
                    pass: '',
                    checkPass: ''
                },
                rules: {
                    oldPass: [
                        {validator: validatePass, trigger: 'blur'}
                    ], pass: [
                        {validator: validatePass, trigger: 'blur'}
                    ],
                    checkPass: [
                        {validator: validatePass2, trigger: 'blur'}
                    ]
                }
            }
        },
        mounted() {
            this.initHr();
        },
        methods: {

            closeBtn() {
                this.$router.push("/home");
            },

            //头像上传成功的处理
            onSuccess() {
                this.initHr();
            },

            //密码の修改操作 started
            showUpdatePasswordView() {
                this.passWordDialogVisible = true;
            },

            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.ruleForm.hrid = this.hr.id;
                        this.putRequest("/hr/pass", this.ruleForm).then(resp => {
                            if (resp) {
                                this.getRequest("/logout");
                                window.sessionStorage.removeItem("user")
                                this.$store.commit('initRoutes', []);
                                this.$router.replace("/");
                            }
                        })
                    } else {
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },


            //个人信息编辑模块 started
            updateHrInfo() {
                this.putRequest("/hr/info", this.hr2).then(resp => {
                    if (resp) {
                        this.dialogVisible = false;
                        this.initHr();
                        //做数据同步
                        /*window.sessionStorage.setItem("user", JSON.stringify(resp));
                        this.$store.commit('INIT_CURRENTHR',resp);*/
                    }
                })
            },

            showUpdateHrInfoView() {
                this.dialogVisible = true;
            },
            //个人信息编辑模块 end

            initHr() {
                this.getRequest("/hr/info").then(resp => {
                    if (resp) {
                        this.hr = resp;
                        //初始化hr2数据源,通过数据拷贝的形式
                        this.hr2 = Object.assign({}, this.hr);

                    }
                })
            }
        }
    }
</script>

<style>
    .clearfix {
        display: flex;
        justify-content: space-between;
        line-height: 32px;
    }

    .mainCard {
        display: flex;
        justify-content: center;
        font-family: 华文行楷;
    }

    .ElBtn {
        display: flex;
        justify-content: space-between;
        margin-top: 10px;
    }
</style>