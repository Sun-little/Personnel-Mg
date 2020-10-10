<template>
    <div class="department">
        <el-input
                prefix-icon="el-icon-search"
                placeholder="请输入部门名称进行搜索操作(●'◡'●)~"
                v-model="filterText">
        </el-input>

        <!--deps: 所有的部门信息-->
        <el-tree
                :data="deps"
                :props="defaultProps"
                :expand-on-click-node="false"
                :filter-node-method="filterNode"
                ref="tree">

            <!--scope-slot实现 树形控件自定义增删按钮-->
            <span class="custom-tree-node" style="display: flex;justify-content: space-between;width: 100%;"
                  slot-scope="{ node, data }">
        <span>{{data.name }}</span>
        <span>
          <el-button
                  type="primary"
                  size="mini"
                  class="depBtn"
                  @click="() => showAddDepView(data)" icon="el-icon-circle-plus">
            部门添加操作
          </el-button>

          <el-button
                  type="danger"
                  size="mini"
                  class="depBtn"
                  @click="() => deleteDep(data)" icon="el-icon-delete">
            部门删除操作
          </el-button>
        </span>
      </span>
        </el-tree>

        <!--添加用的对话框-->
        <el-dialog
                title="部门添加操作面板"
                :visible.sync="dialogVisible"
                width="30%" style="font-family: 华文行楷">
            <div>
                <table>
                    <tr>
                        <td>
                            <el-tag>上级部门</el-tag>
                        </td>
                        <td>{{pname}}</td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag>部门名称</el-tag>
                        </td>
                        <td>
                            <el-input prefix-icon="el-icon-edit-outline" v-model="dep.name" placeholder="请输入新部门所属名称~"></el-input>
                        </td>
                    </tr>
                </table>
            </div>
            <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false" icon="el-icon-circle-close">取消当前添加操作</el-button>
    <el-button type="primary" @click="doAddDep" icon="el-icon-success">确定提交该添加操作</el-button>
  </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "DepMana",
        data() {
            return {
                dialogVisible:false,
                filterText: '',
                //dep 需要添加部门的用的属性
                dep: {
                    name: '',
                    parentId: -1
                },
                pname: '',
                deps: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                }
            }
        },
        watch: {
            //watch负责监控 filterText值的变化
            //val: 是filterText的最新值,值每发生一次变化方法和其中的语句就会被执行一次。
            filterText(val) {
                //获取 tree树形控件并调用 filterNode: 即过滤方法
                this.$refs.tree.filter(val);
            }
        },
        mounted() {
            this.initDeps();
        },
        methods: {

            //添加操作完成后,面板参数清空即初始化变量
            initDep() {
                this.dep = {
                    name: '',
                        parentId: -1
                }
                this.pname= '';
            },

            //递归实现deps数组动态的修改它的元素实现刷新
            addDep2Deps(deps,dep){
                    for (let i = 0; i < deps.length; i++) {
                        let d = deps[i];
                        if (d.id == dep.parentId) {
                            d.children = d.children.concat(dep);
                            if (d.children.length > 0) {
                                d.parent = true;
                            }
                            return;
                        } else {
                            this.addDep2Deps(d.children, dep);
                        }
                    }
            },

            doAddDep(){
              this.postRequest("/system/basic/department/", this.dep).then(resp=>{
                  if(resp){
                      this.addDep2Deps(this.deps, resp.obj);
                      this.dialogVisible = false;
                      //初始化变量
                      this.initDep();
                  }
              })
            },
            //递归实现删除操作后动态刷新节点
            removeDepFromDeps(p,deps, id) {
                for(let i=0;i<deps.length;i++){
                    let d = deps[i];
                    if (d.id == id) {
                        deps.splice(i, 1);
                        if (deps.length == 0) {
                            p.parent = false;
                        }
                        return;
                    }else{
                        this.removeDepFromDeps(d,d.children, id);
                    }
                }
            },

            //添加部门面板实现
            showAddDepView(data){
                this.pname = data.name;
                this.dep.parentId = data.id;
                this.dialogVisible=true;

            },

            //删除部门操作
            deleteDep(data){
                if(data.parent){
                    this.$message.error("该部门为父部门,无法进行删除操作~ ~")
                }else{
                    this.$confirm('此操作将永久删除【'+data.name+'】部门, 是否继续?', '提示->☠', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        //删除成功操作
                        this.deleteRequest("/system/basic/department/"+ data.id).then(resp=>{
                            if(resp){
                                this.removeDepFromDeps(null,this.deps,data.id);
                            }
                        })
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '当前删除操作已成功取消'
                        });
                    });
                }
            },

                initDeps(){
                    this.getRequest("/system/basic/department/").then(resp=>{
                        if(resp){
                            this.deps=resp;
                        }
                    })
                },
                filterNode(value, data) {
                    if (!value) return true;
                    return data.name.indexOf(value) !== -1;
                }
          }
    }

</script>

<style>
    .department{
        width: 474px;
    }
    .depBtn{
        padding: 2px;
    }
</style>