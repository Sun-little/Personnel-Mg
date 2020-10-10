<template>
    <div>
        <div>
            <el-input size="small" v-model="jl.name" style="width: 300px"
            prefix-icon="el-icon-plus" placeholder="请输入需要添加的职称o(*￣▽￣*)ブ"
            ></el-input>

            <el-select v-model="jl.titleLevel" placeholder="请选择对应的职称等级~" size="small"
            style="margin-left: 5px">
                <el-option
                        v-for="item in titleLevels"
                        :key="item"
                        :label="item"
                        :value="item">
                </el-option>
            </el-select>

            <el-button icon="el-icon-plus" type="primary" size="small" style="margin-left: 8px" @click="addJobLevel">新添加职称~</el-button>

        </div>
        <div style="margin-top: 8px">
            <!--数据展示-->
            <el-table
                    :data="Jls"
                    border
                    @selection-change="handleSelectionChange"
                    v-loading="loading"
                    element-loading-text="正在执行添加操作,请稍后~ ~"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    stripe
                    size="small"
                    style="width: 80%">
                <!--批量删除checkbox多选按钮-->
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>

                <el-table-column
                        prop="id"
                        label="编号"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="职称名称"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="titleLevel"
                        label="职称级别"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        label="创建时间"
                        width="210">
                </el-table-column>
                <el-table-column
                        prop="enabled"
                        label="是否启用">
                    <templates slot-scope="scope">
                         <el-tag type="success" v-if="scope.row.enabled">已启用</el-tag>
                         <el-tag type="danger" v-else>未启用</el-tag>
                    </templates>

                </el-table-column>
                <el-table-column
                        label="操作">
                    <templates slot-scope="scope">
                        <el-button size="small" @click="showEditView(scope.row)">编辑</el-button>
                        <el-button size="small" type="danger" @click="deleteHandler(scope.row)">删除</el-button>
                    </templates>
                </el-table-column>
            </el-table>
            <!--批量删除-->
            <el-button @click="deleteMany" type="danger" size="small" style="margin-top: 6px" :disabled="multipleSelection.length==0">批量删除</el-button>
        </div>
        <!--编辑信息框-->
        <el-dialog
                title="修改职称"
                :visible.sync="dialogVisible"
                width="30%">
            <div>
                <table>
                    <tr>
                        <td>
                            <el-tag>职称名</el-tag>
                        </td>
                        <td>
                            <el-input size="small" v-model="updateJl.name"></el-input>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag>职称级别</el-tag>
                        </td>
                        <td>
                            <el-select v-model="updateJl.titleLevel" placeholder="职称等级" size="small"
                                       style="margin-left: 5px;margin-right: 5px">
                                <el-option
                                        v-for="item in titleLevels"
                                        :key="item"
                                        :label="item"
                                        :value="item">
                                </el-option>
                            </el-select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag>是否启用</el-tag>
                        </td>
                        <td>
                            <el-switch
                                    v-model="updateJl.enabled"
                                    active-text="启用"
                                    inactive-text="禁用">
                            </el-switch>
                        </td>
                    </tr>
                </table>
            </div>
            <span slot="footer" class="dialog-footer">
    <el-button size="small" @click="dialogVisible = false">取 消</el-button>
    <el-button size="small" type="primary" @click="doUpdate">确 定</el-button>
  </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "JobLevelMana",
        data(){
            return{
                dialogVisible:false,
                loading:false,
                multipleSelection:[],
                updateJl:{
                    name:'',
                    titleLevel:'',
                    enabled:false
                },
                jl:{
                    name:'',
                    titleLevel:'',

                },
                Jls:[],
                titleLevels:[
                    '正高级',
                    '副高级',
                    '中级',
                    '初级',
                    '员级'
                ]
            }
        },
        //mounted 是一个方法
        mounted() {
          //调用 initJls
          this.initJls();
        },
        methods:{
            /*methods: 它是一个属性*/
            //批量删除
            handleSelectionChange(val){
                this.multipleSelection=val;
            },
            deleteMany(){
                this.$confirm('该操作将永久删除【'+this.multipleSelection.length+'】条数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let ids='?';
                    this.multipleSelection.forEach(item=>{
                        ids+='ids='+item.id+'&';
                    })
                    this.deleteRequest("/system/basic/joblevel/"+ids).then(resp=>{
                        if(resp){
                            this.initJls();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消数据的批量删除o(*￣▽￣*)ブ'
                    });
                });
            },

            //编辑操作
            showEditView(data){
                //将对应的数据提前显示在输入框中
                Object.assign(this.updateJl,data);
                this.dialogVisible= true;
            },
            doUpdate(){
                this.putRequest("/system/basic/joblevel/",this.updateJl).then(resp=>{
                    if(resp){
                        this.initJls();
                        this.dialogVisible= false;
                    }
                })
            },
            //删除操作
            deleteHandler(data) {
            this.$confirm('此操作将永久删除【' + data.name + '】职称, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteRequest("/system/basic/joblevel/" + data.id).then(resp => {
                    if (resp) {
                        this.initJls();
                    }
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                })
            })
        },

            //添加
            addJobLevel(){
                if(this.jl.name && this.jl.titleLevel){
                    this.postRequest("/system/basic/joblevel/",this.jl).then(resp=>{
                        if(resp){
                            this.initJls();
                        }
                    })
                }else {
                    this.$message("该操作对应的字段不能为空~请输入字段后重试~")
                }

            },
            initJls(){
                this.loading= true;
                this.getRequest("/system/basic/joblevel/").then(resp=>{
                    this.loading= false;
                    if(resp){
                       this.Jls=resp;
                       //数据添加完成后 输入框置空
                       this.jl={
                           name:'',
                           titleLevel:''
                        }
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>