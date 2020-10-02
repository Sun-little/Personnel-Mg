<template>
    <div>
        <div style="display: flex; justify-content: space-between">
            <el-button type="primary" icon="el-icon-circle-plus" @click="showAddSalaryView">添加工资账套</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="initsalaries"/>
        </div>
        <!--表格数据展示Started-->
        <div style="margin-top: 10px">
            <el-table class="salTab" :data="salaries" border stripe>
                <el-table-column align="center" type="selection" width="55"></el-table-column>
                <el-table-column align="center" width="120" prop="name" label="账套名称"></el-table-column>
                <el-table-column align="center" width="75" prop="basicSalary" label="基本工资"></el-table-column>
                <el-table-column align="center" width="75" prop="trafficSalary" label="交通补助"></el-table-column>
                <el-table-column align="center" width="75" prop="lunchSalary" label="午餐补助"></el-table-column>
                <el-table-column align="center" width="70" prop="bonus" label="奖金"></el-table-column>
                <el-table-column align="center" width="100" prop="createDate" label="启用时间"></el-table-column>
                //多级表头
                <el-table-column label="养老金" align="center">
                    <el-table-column align="center" width="70" prop="pensionPer" label="比率"></el-table-column>
                    <el-table-column align="center" width="70" prop="pensionBase" label="基数"></el-table-column>
                </el-table-column>
                //多级表头
                <el-table-column label="医疗保险" align="center">
                    <el-table-column align="center" width="70" prop="medicalPer" label="比率"></el-table-column>
                    <el-table-column align="center" width="70" prop="medicalBase" label="基数"></el-table-column>
                </el-table-column>
                //多级表头
                <el-table-column label="公积金" align="center">
                    <el-table-column align="center" width="70" prop="accumulationFundPer" label="比率"></el-table-column>
                    <el-table-column align="center" width="70" prop="accumulationFundBase" label="基数"></el-table-column>
                </el-table-column>

                <el-table-column align="center" label="操作">
                    <template slot-scope="scope">
                        <el-button @click="showEditSalView(scope.row)">编辑</el-button>
                        <el-button type="danger" @click="deleteSal(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--表格END-->
        </div>
        <!--添加操作 Started-->
        <el-dialog
                style="font-family: 华文行楷"
                :title="dialogVisibleTitle"
                :visible.sync="dialogVisible"
                width="50%">
            <!--左边步骤条-->
            <div style="display: flex; justify-content: space-around; align-items: center">
                <!--添加操作の步骤条-->
                <el-steps direction="vertical" :active="activeItemIndex">
                    <el-step :title="itemName" v-for="(itemName,index) in salaryItemName" :key="index"></el-step>
                </el-steps>
                <!--右边输入框Started-->
                <el-input

                        v-model="salary[title]" prefix-icon="el-icon-edit"
                        @keydown.enter.native="nextStep"
                        :placeholder="'请输入账套の'+salaryItemName[index]+'~ ~'"
                        v-for="(value,title,index) in salary"
                        :key="index"
                        v-show="activeItemIndex==index"
                        style="width: 250px">
                </el-input>
                <!--右边输入框end-->
            </div>
            <span slot="footer" class="dialog-footer">
    <el-button @click="perStep">{{activeItemIndex==10?'取消':'上一步'}}</el-button>
    <el-button type="primary" @click="nextStep">{{activeItemIndex==10?'完成':'下一步'}}</el-button>
  </span>
        </el-dialog>
        <!--添加操作 END-->

    </div>
</template>

<script>
    export default {
        name: "SalSob",
        data() {
            return {
                salaries: [],
                dialogVisibleTitle: '工资账套添加の操作面板',
                dialogVisible: false,
                activeItemIndex: 0,
                salaryItemName: [
                    '基本工资',
                    '交通补助',
                    '午餐补助',
                    '奖金',
                    '养老金比率',
                    '养老金基数',
                    '医疗保险比率',
                    '医疗保险基数',
                    '公积金比率',
                    '公积金基数',
                    '账套名称'
                ],
                salary: {
                    basicSalary: 0,
                    trafficSalary: 0,
                    lunchSalary: 0,
                    bonus: 0,
                    pensionPer: 0,
                    pensionBase: 0,
                    medicalPer: 0,
                    medicalBase: 0,
                    accumulationFundPer: 0,
                    accumulationFundBase: 0,
                    name: ''
                },
            }
        },
        mounted() {
            this.initsalaries();
        },
        methods: {

            //编辑操作视图展示
            showEditSalView(data) {
                this.dialogVisibleTitle = '工资账套编辑の操作面板';
                this.dialogVisible = true;
                //数据赋值
                this.salary.basicSalary = data.basicSalary;
                this.salary.trafficSalary = data.trafficSalary;
                this.salary.lunchSalary = data.lunchSalary;
                this.salary.bonus = data.bonus;
                this.salary.pensionPer = data.pensionPer;
                this.salary.pensionBase = data.pensionBase;
                this.salary.medicalPer = data.medicalPer;
                this.salary.medicalBase = data.medicalBase;
                this.salary.accumulationFundPer = data.accumulationFundPer;
                this.salary.accumulationFundBase = data.accumulationFundBase;
                this.salary.name = data.name;
                this.salary.id = data.id;
                this.activeItemIndex = 0;
            },

            //删除操作
            deleteSal(data) {
                this.$confirm('此操作将永久删除名称为【' + data.name + '】の套账, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/salary/sob/" + data.id).then(resp => {
                        if (resp) {
                            this.initsalaries();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '删除操作已取消'
                    });
                });
            },

            perStep() {
                if (this.activeItemIndex == 0) {
                    return;
                } else if (this.activeItemIndex == 10) {
                    //关闭dialog
                    this.dialogVisible = false;
                    return;
                }
                this.activeItemIndex--;
            },
            nextStep() {
                if (this.activeItemIndex == 10) {
                    //判断请求地址如果有id,则执行编辑操作
                    if (this.salary.id) {
                        this.putRequest("/salary/sob/",this.salary).then(resp=>{
                            if(resp){
                                this.initsalaries();
                                this.dialogVisible=false;
                            }
                        })
                    } else {
                        //没有id就执行添加操作
                        this.postRequest("/salary/sob/", this.salary).then(resp => {
                            if (resp) {
                                this.initsalaries();
                                this.dialogVisible = false;
                            }
                        })
                    }
                    return;
                }
                this.activeItemIndex++;
            },
            showAddSalaryView() {
                //数据初始化
                this.salary = {
                    basicSalary: 0,
                    trafficSalary: 0,
                    lunchSalary: 0,
                    bonus: 0,
                    pensionPer: 0,
                    pensionBase: 0,
                    medicalPer: 0,
                    medicalBase: 0,
                    accumulationFundPer: 0,
                    accumulationFundBase: 0,
                    name: ''
                }
                this.dialogVisibleTitle = '工资账套添加の操作面板';
                this.activeItemIndex = 0;
                this.dialogVisible = true;
            },

            //初始化数据
            initsalaries() {
                this.getRequest("/salary/sob/").then(resp => {
                    if (resp) {
                        this.salaries = resp;
                    }
                })
            }
        }
    }
</script>

<style>
    .salTab {
        font-family: 华文行楷;
        text-align: center;
    }
</style>