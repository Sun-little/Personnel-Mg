<template>
    <div>
        <div>
            <el-input
                    size="small"
                    class="addPosInput"
                    v-loading="loading"
                    element-loading-text="正在执行添加操作,请稍后~ ~"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    placeholder="新职位信息添加(●'◡'●)"
                    prefix-icon="el-icon-plus"
                    @keydown.enter.native="addPosition"
                    v-model="pos.name">
            </el-input>
            <el-button icon="el-icon-plus" size="small" type="primary" @click="addPosition">添加</el-button>
        </div>
        <div class="posManaMain">
            <el-table
                    :data="positions"
                    border
                    @selection-change="handleSelectionChange"
                    stripe
                    size="small"
                    style="width: 60%">
                <!--checkbox多选按钮-->
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>

                <el-table-column
                        prop="id"
                        label="编号"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="职位名称"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        label="创建时间">
                </el-table-column>

                <el-table-column
                prop="enabled"
                label="是否启用">
                <templates slot-scope="scope">
                    <el-tag type="success" v-if="scope.row.enabled">已启用</el-tag>
                    <el-tag type="danger" v-else>未启用</el-tag>
                </templates>
                </el-table-column>

                <el-table-column label="其他操作">
                    <!--scope 相当于占位符 用于编辑等按钮的填充-->
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                @click="showEditView(scope.$index, scope.row)">编辑</el-button>
                        <el-button
                                size="mini"
                                type="danger"
                                @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-button @click="deleteMany" type="danger" size="small" style="margin-top: 6px" :disabled="multipleSelection.length==0">批量删除</el-button>
        </div>
        <el-dialog
                title="职位信息修改操作"
                :visible.sync="dialogVisible"
                width="30%">
                <div>
                     <el-tag>职位名称</el-tag>
                    <el-input class="updatePosInput"
                              size="small"
                              v-model="updatePos.name"
                              @keydown.enter.native="doUpdata">
                    </el-input>
                </div>
            <div>
                <el-tag>是否启用</el-tag>
                <el-switch
                        v-model="updatePos.enabled"
                        active-text="启用"
                        inactive-text="禁用">
                </el-switch>
            </div>

            <span slot="footer" class="dialog-footer">
    <el-button size="small" @click="dialogVisible = false">取 消</el-button>
    <el-button size="small" type="primary" @click="doUpdata">确 定</el-button>
  </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "PosMana",
        data(){
            return{
                //服务端的对象
                pos:{
                    name:''
                },
                updatePos:{
                  name:'',
                  enabled:false
                },
                dialogVisible:false,
                loading:false,
                multipleSelection:[],
                //服务端数据
                positions: []
            }
        },
        //数据初始化放于mounted中
        mounted() {
            //当mounted组件触发时 自动调用initPositions方法并在前端展示出来
            this.initPositions();
        },
        methods:{
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
                    this.deleteRequest("/system/basic/pos/"+ids).then(resp=>{
                        if(resp){
                            this.initPositions();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消数据的批量删除o(*￣▽￣*)ブ'
                    });
                });
            },

            //添加
            addPosition(){
                //如果字段不为空的话发送请求,将输入的对象数据上传到数据库
                if(this.pos.name){
                    this.postRequest("/system/basic/pos/",this.pos).then(resp=>{
                        //如果请求成功,需要将前端数据进行实时刷新,且添加完成后输入框数据清空
                        if(resp){
                            this.initPositions();
                            this.pos.name='';
                        }
                    })
                }else {
                    this.$message.error('职位名称字段不能为空(；′⌒`)');
                }
            },
            //编辑
            showEditView(index,data){
                Object.assign(this.updatePos,data);
                this.dialogVisible=true;
            },
            doUpdata(){
                this.putRequest("/system/basic/pos/",this.updatePos).then(resp=>{
                    //如果请求成功 数据实时刷新,并将输入框数据清空
                    if(resp){
                        this.initPositions();
                        this.updatePos.name='';
                        this.dialogVisible=false;
                    }
                })
            },
            //删除
            handleDelete(index,data){
                this.$confirm('您正在进行的操作将永久删除'+data.name+'职位, 是否继续?', '提示', {
                    confirmButtonText: '确定执行删除操作->?',
                    cancelButtonText: '取消当前操作',
                    type: 'warning'
                }).then(() => {
                   //如果点击确定按钮就发送请求处理
                    this.deleteRequest("/system/basic/pos/"+data.id).then(resp=>{
                        //如果删除成功刷新前端展示的数据
                        if (resp){
                            this.initPositions();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消即将要进行的删除操作'
                    });
                });
            },
            //查询
            initPositions(){
                this.loading = true;
                //向服务端发送get请求获取数据
                this.getRequest("/system/basic/pos/").then(resp=>{
                    this.loading = false;
                    //如果请求成功,就将请求结果赋值给前端要展示的属性
                    if(resp){
                    this.positions=resp;
                    }
                })
            }
        }

    }
</script>

<style>
    .addPosInput{
        width: 388px;
        margin-right: 11px;
    }
    .posManaMain{
        margin-top: 10px;
    }

    .updatePosInput{
        width: 200px;
        margin-left: 6px;
    }

</style>