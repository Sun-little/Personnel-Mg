<template>
    <div
            v-loading="globalloading"
            element-loading-text="正在执行添加操作,请稍后~ ~"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
    >

        <div class="perMissManaTool">
            <!--添加操作的vue界面-->
            <el-input size="small" placeholder="请输入角色的英文名称(●'◡'●)" v-model="role.name">
            <template slot="prepend">ROLE_</template>
            </el-input>
            <el-input size="small" placeholder="请输入角色的中文名称(●'◡'●)" v-model="role.nameZh"  @keydown.enter.native="doAddRole">

            </el-input>
            <el-button type="primary" size="small" icon="el-icon-plus" style="margin-left: 6px" @click="doAddRole">添加角色信息φ(゜▽゜*)♪</el-button>
        </div>


        <!--折叠面板-->
        <div class="perMissManaColl">

            <!--@change:折叠面板点击事件-->
            <el-collapse
                    v-model="activeName"
                    v-loading="loading"
                    element-loading-text="数据正在刷新,请稍后~ ~"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    accordion
                    @change="change">
                <el-collapse-item :title="role.nameZh" :name="role.id" v-for="(role,index) in roles" :key="index" >
                    <el-card class="box-card">
                        <div slot="header" class="clearfix">
                            <span>该角色可访问的资源</span>
                            <el-button style="float: right; padding: 3px 0; color: #ff0000;"icon="el-icon-delete" type="text" @click="deleteRole(role)"></el-button>
                        </div>

                        <div>
                            <!--菜单树型展示-->
                            <!--添加多选框 show-checkbox属性-->
                            <!--selectedMenus：每个角色可以操作的菜单权限-->
                            <el-tree
                                    show-checkbox
                                    node-key="id"
                                    ref="tree"
                                    :key="index"
                                    :default-checked-keys="selectedMenus"
                                    :data="allmenus" :props="defaultProps"></el-tree>
                            <div style="display: flex;justify-content: flex-end">
                                <el-button style="color: #409eff" type="primary" @click="canceUpdate">取消权限修改</el-button>
                                <el-button style="color: #ff0000" type="primary" @click="doUpdate(role.id,index)">确认权限修改->☠</el-button>
                            </div>
                        </div>
                    </el-card>
                </el-collapse-item>
            </el-collapse>
        </div>

    </div>
</template>

<script>
    export default {
        name: "PermissMana",
        data(){
            return{
                loading: false,
                globalloading: false,
                //角色名称参数变量
                role:{
                    name:'',
                    nameZh:''
                },
                activeName:-1,
                //选中的菜单
                selectedMenus:[],
                allmenus:[],
                roles:[],
                //菜单树的Props
                defaultProps: {
                    children: 'children',
                    label: 'name'
                }
            }
        },
        mounted() {
            this.initRoles();
        },
        methods:{

            //删除操作
            deleteRole(role){
                    this.$confirm('此操作将永久删除【'+role.nameZh+'】角色, 是否继续?', '提示->☠', {
                        confirmButtonText: '确定->☠',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        //删除成功操作
                        this.deleteRequest("/system/basic/permiss/del/"+role.id).then(resp=>{
                            if(resp){
                                this.initRoles();
                            }
                        })

                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消当前的删除操作φ(゜▽゜*)♪'
                        });
                    });
            },

            //添加角色操作
            doAddRole(){
                if(this.role.name&&this.role.nameZh){
                    this.globalloading=true;
                    this.postRequest("/system/basic/permiss/",this.role).then(resp=>{
                    this.globalloading=false;
                        if(resp){
                            this.role.name='';
                            this.role.nameZh='';
                            this.initRoles();
                        }
                    })
                }else {
                    this.$message.error("请完成角色名称的输入,此项不能为空(●ˇ∀ˇ●)");
                }
            },

            //取消权限修改
            canceUpdate(){
              this.activeName=-1;
            },

            //权限的修改操作
            doUpdate(rid,index){
                //拿到菜单树中选中权限的index(即下标)
               let tree = this.$refs.tree[index];
               //参数为true 只返回子节点的下标
               let selectedKeys= tree.getCheckedKeys(true);
               //拼接url请求地址
               let url='/system/basic/permiss/?rid='+rid;
               selectedKeys.forEach(key=>{
                   url+='&mids=' + key;
               })
                this.putRequest(url).then(resp=>{
                    if (resp){
                        this.initRoles();
                        this.activeName=-1;
                    }
                })
            },

            //折叠面板的回调参数为 role.name的name
            //每次打开菜单树就执行对应角色权限选中的方法initSelectedMenus
            change(rid){
                if(rid){
                    this.initAllMenus();
                    this.initSelectedMenus(rid);
                }
            },
            initSelectedMenus(rid){
               this.getRequest("/system/basic/permiss/mids/"+rid).then(resp=>{
                   if(resp){
                       this.selectedMenus=resp;
                   }
               })
            },
            initAllMenus(){
              this.getRequest("/system/basic/permiss/menus").then(resp=>{
                  if(resp){
                      this.allmenus=resp;
                  }
              })
            },
            initRoles(){
                this.loading= true;
                this.getRequest("/system/basic/permiss/").then(resp=>{
                this.loading= false;
                    //请求响应成功判断
                    if(resp){
                        this.roles=resp;
                    }
                })
            }
        }
    }
</script>

<style>
    .perMissManaTool{
        display: flex;
        justify-content: flex-start;
    }
    /*调整整个div中的 el-input标签整体大小*/
    .perMissManaTool .el-input{
        width: 300px;
        margin-right: 6px;
    }
    .perMissManaColl{
        margin-top: 10px;
        width: 796px;
    }

</style>