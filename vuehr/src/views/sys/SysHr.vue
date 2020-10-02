<template>
    <div>
        <div style="margin-top: 10px; display: flex;justify-content: center">
            <el-input v-model="keywords"
                      prefix-icon="el-icon-search"
                      @keydown.enter.native="doSearch"
                      placeholder="请输入用户名进行用户搜索 ~ ~"style="width: 500px">



            </el-input>
            <el-button type="primary" icon="el-icon-search" style="margin-left: 10px" @click="doSearch">搜索</el-button>
        </div>

        <div class="hr-container">
            <el-card class="hr-card" v-for="(hr,index) in hrs" :key="index">
                <div slot="header" class="clearfix">
                    <span>{{hr.name}}</span>

                        <!--删除按钮-->
                    <el-button style="float: right;
                               padding: 3px 0;
                               color: #ff0000" type="text"
                               icon="el-icon-delete-solid"
                               @click="deleteHr(hr)" >

                    </el-button>
                </div>
                <!--头像 -->
                <div>
                    <div class="img-container">
                        <img :src="hr.userface" :alt="hr.name" :title="hr.name" class="userface-img">
                    </div>
                    <!--基本信息-->
                    <div class="userinfo-container">
                        <div>用户名: {{hr.name}}</div>
                        <div>手机: {{hr.phone}}</div>
                        <div>电话: {{hr.telephone}}</div>
                        <div>地址: {{hr.address}}</div>
                        <div>用户状态: <el-switch
                                v-model="hr.enabled"
                                active-text="启用"
                                @change="enabledChange(hr)"
                                active-color="#13ce66"
                                inactive-color="#ff4949"
                                inactive-text="禁用">
                        </el-switch>
                        </div>
                        <!--用户角色-->
                        <div>
                            用户角色: <el-tag type="success"
                                          v-for="(role,indexj) in hr.roles"
                                          :key="indexj"
                                          style="margin-right: 5px"  >
                            {{role.nameZh}}</el-tag>
                            <!--编辑-->
                            <el-popover
                                    placement="right"
                                    title="角色列表操作面板"
                                    @show="showPop(hr)"
                                    @hide="hidePop(hr)"
                                    width="200"
                                    trigger="click">
                                <el-select v-model="selectedRoles" multiple placeholder="请选择">
                                    <el-option
                                            v-for="(r,indexk) in allRoles"
                                            :key="indexk"
                                            :label="r.nameZh"
                                            :value="r.id">
                                    </el-option>
                                </el-select>
                            <el-button slot="reference" style="margin-left: 8px;color: #409eff" type="text" icon="el-icon-edit">
                            </el-button>
                            </el-popover>




                        </div>
                        <!--备注-->
                        <div>
                            备注: {{hr.remark}}
                        </div>
                    </div>
                </div>

            </el-card>
        </div>
    </div>
</template>

<script>
    export default {
        name: "SysHr",
        data(){
            return{
                keywords:'',
                hrs:[],
                allRoles:[],
                selectedRoles:[]
            }
        },
        mounted() {
            this.initHrs();
        },
        methods:{

            //删除操作
            deleteHr(hr){
                this.$confirm('此操作将永久删除【'+hr.name+'】用户, 是否继续?', '提示', {
                    confirmButtonText: '确定执行删除操作',
                    cancelButtonText: '取消删除',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/system/hr/"+hr.id).then(resp=>{
                        this.initHrs();
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },

            //搜索
            doSearch(){
               this.initHrs();
            },
            hidePop(hr){
                let roles = [];
                Object.assign(roles, hr.roles);
                let flag = false;
                if (roles.length != this.selectedRoles.length) {
                    flag = true;
                } else {
                    for (let i = 0; i < roles.length; i++) {
                        let role = roles[i];
                        for (let j = 0; j < this.selectedRoles.length; j++) {
                            let sr = this.selectedRoles[j];
                            if (role.id == sr) {
                                roles.splice(i, 1);
                                i--;
                                break;
                            }
                        }
                    }
                    if (roles.length != 0) {
                        flag = true;
                    }
                }
                if (flag) {
                    let url = '/system/hr/role?hrid=' + hr.id;
                    this.selectedRoles.forEach(sr => {
                        url += '&rids=' + sr;
                    });
                    this.putRequest(url).then(resp => {
                        if (resp) {
                            this.initHrs();
                        }
                    });
                }
            },
            showPop(hr){
                this.initAllRoles();
                let roles= hr.roles;
                this.selectedRoles=[];
                roles.forEach(r=>{
                    this.selectedRoles.push(r.id);
                })
            },
            //拿到所有角色
            initAllRoles(){
                this.getRequest("/system/hr/roles").then(resp=>{
                    if(resp){
                        this.allRoles=resp;
                    }
                })
            },
            enabledChange(hr){
                delete hr.roles;
                this.putRequest("/system/hr/",hr).then(resp=>{
                    if (resp){
                        this.initHrs();
                    }
                })
            },
            initHrs(){
                this.getRequest("/system/hr/?keywords="+this.keywords).then(resp=>{
                    if (resp){
                        this.hrs=resp;
                    }
                })
            }
        }
    }
</script>

<style>


    .userinfo-container div{
        font-family: 华文行楷;
        font-size: 20px;
        color: #409eff;
    }

    .userinfo-container{

        margin-top: 20px;
    }

    .img-container{
        width: 100%;
        display: flex;
        justify-content: center;
    }
    .userface-img{
        width: 72px;
        height: 72px;
        border-radius: 10px;
    }

    .hr-container{
        margin-top: 10px;
        display: flex;
        flex-wrap: wrap;
        /*空间匀分布*/
        justify-content: space-around;
    }
    .hr-card{
        width: 400px;
        margin-bottom: 20px;
    }

</style>