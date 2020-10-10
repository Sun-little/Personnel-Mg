<template>
  <div>
    <div>

      <div class="three_button">
        <div>
          <el-input
              prefix-icon="el-icon-search"
              placeholder="请输入员工の姓名或关键字进行检索~ ~"
              clearable
              @clear="initEmps"
              :disabled="showAdvanceSearchView"
              style="width: 300px" v-model="keyword" @keydown.enter.native="initEmps"></el-input>
          <el-button
              icon="el-icon-search"
              type="primary"
              :disabled="showAdvanceSearchView"
              style="margin-left: 10px" @click="initEmps">搜索
          </el-button>
          <el-button
              @click="showAdvanceSearchView= !showAdvanceSearchView"
              type="primary"
              style="margin-left: 10px">
            <i :class="showAdvanceSearchView?'fa fa-angle-double-up':'fa fa-angle-double-down'" aria-hidden="true"></i>
            高级搜索
          </el-button>
        </div>
        <!--右边按钮-->
        <div>
          <!--数据导入第一步: 文件上传-->
          <el-upload
              :show-file-list="false"
              :before-upload="BeforeUpload"
              :on-success="onSuccess"
              :on-error="onUpError"
              :disabled="importDataDisabled"
              style="display: inline-flex;margin-right: 8px"
              action="/employee/basic/import">
            <el-button  :disabled="importDataDisabled" type="success" :icon="importDataBtnIcon">
              {{importDataBtnText}}
            </el-button>
          </el-upload>

          <el-button type="success" @click="exportEmp" icon="el-icon-download">
            员工数据の导出
          </el-button>

          <el-button type="primary" @click="showAddEmpView">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
            新员工信息添加
          </el-button>
        </div>
      </div>
      <transition name="slide-fade">
        <div class="seachDiv" v-show="showAdvanceSearchView">
          <!--第一行 started-->
          <el-row >
            <el-col :span="5">
              <el-tag style="margin-left: 4px">政治面貌-></el-tag>
              <el-select v-model="searchValue.politicId" placeholder="请选择对应の政治面貌~ ~" size="mini" style="width: 130px; margin-left: 4px">
                <el-option
                    v-for="item in politicsstatus"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>

            <el-col :span="4">
              <el-tag >民族-></el-tag>
              <el-select v-model="searchValue.nationId" placeholder="请选择对应の民族~ ~" size="mini" style="width: 100px; margin-left: 4px">
                <el-option
                    v-for="item in nations"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-tag >职位-></el-tag>
              <el-select v-model="searchValue.posId" placeholder="请选择对应の职位~ ~" size="mini" style="width: 130px; margin-left: 4px">
                <el-option
                    v-for="item in positions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-tag style="margin-left: 30px">职称-></el-tag>
              <el-select v-model="searchValue.jobLevelId" placeholder="请选择对应の职称~ ~" size="mini" style="width: 120px; margin-left: 4px">
                <el-option
                    v-for="item in jobLevels"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <el-tag style="margin-left: 30px">聘用形式-></el-tag>
              <el-radio-group v-model="searchValue.engageForm" style="margin-left: 10px">
                <el-radio label="劳动合同">劳动合同</el-radio>
                <el-radio label="劳务合同">劳务合同</el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
          <!--第一行 end-->

          <!--第二行 started-->
          <el-row style="margin-top: 10px">
            <el-col :span="6">
              <el-tag style="margin-left: 4px;">所属部门-></el-tag>
              <el-popover
                  placement="right"
                  title="所属部门选择操作面板"
                  width="220"
                  trigger="manual"
                  v-model="popVisible2">
                <el-tree default-expand-all :data="DepData" :props="defaultProps" @node-click="searchViewHandleNodeClick"></el-tree>

                <div  slot="reference"
                      class="department_div2"
                      @click="showDepView2">
                  {{inputDepName}}
                </div>
              </el-popover>
            </el-col>
            <el-col :span="9">
              <el-tag style="margin-left: 4px">入职日期-></el-tag>
              <el-date-picker
                  style="margin-left: 4px;
                                cursor: pointer"
                  size="mini"
                  v-model="searchValue.beginDateScope"
                  type="daterange"
                  unlink-panels
                  value-format="yyyy-MM-dd"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="5" :offset="4">
              <el-button size="mini">取消</el-button>
              <el-button size="mini" icon="el-icon-search" type="primary" @click="initEmps('advanced')">搜索</el-button>
            </el-col>
          </el-row>
          <!--第二行 end-->
        </div>
      </transition>
    </div>
    <!--Table-->
    <div style="margin-top: 10px">
      <el-table
          :data="emps"
          stripe
          border
          v-loading="loading"
          element-loading-text="正在加载数据,请稍后~ ~"
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)"
          style="width: 100%; font-family: 华文行楷">
        <!--选择框-->
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>

        <el-table-column
            prop="name"
            fixed
            align="center"
            label="姓名"
            width="90">
        </el-table-column>
        <el-table-column
            prop="workID"
            label="工号"
            align="center"
            width="85">
        </el-table-column>
        <el-table-column
            prop="gender"
            label="性别"
            align="center"
            width="85">
        </el-table-column>
        <el-table-column
            prop="birthday"
            width="95"
            align="center"
            label="出生日期">
        </el-table-column>
        <el-table-column
            prop="idCard"
            width="150"
            align="center"
            label="身份证号码">
        </el-table-column>
        <el-table-column
            prop="wedlock"
            width="75"
            align="center"
            label="婚姻状况">
        </el-table-column>
        <el-table-column
            prop="nation.name"
            width="50"
            align="center"
            label="民族">
        </el-table-column>
        <el-table-column
            prop="nativePlace"
            width="85"
            align="center"
            label="籍贯">
        </el-table-column>
        <el-table-column
            prop="politicsstatus.name"
            align="center"
            label="政治面貌">
        </el-table-column>
        <el-table-column
            prop="email"
            width="180"
            align="center"
            label="电子邮件">
        </el-table-column>
        <el-table-column
            prop="phone"
            width="100"
            align="center"
            label="电话号码">
        </el-table-column>
        <el-table-column
            prop="address"
            width="220"
            align="center"
            label="联系地址">
        </el-table-column>
        <el-table-column
            prop="department.name"
            width="100"
            align="center"
            label="所属部门">
        </el-table-column>
        <el-table-column
            prop="position.name"
            width="120"
            align="center"
            label="职位">
        </el-table-column>
        <el-table-column
            prop="jobLevel.name"
            width="100"
            align="center"
            label="职称">
        </el-table-column>
        <el-table-column
            prop="engageForm"
            width="100"
            align="center"
            label="聘用形式">
        </el-table-column>
        <el-table-column
            prop="tiptopDegree"
            width="80"
            align="center"
            label="最高学历">
        </el-table-column>
        <el-table-column
            prop="specialty"
            width="150"
            align="center"
            label="专业">
        </el-table-column>
        <el-table-column
            prop="school"
            width="150"
            align="center"
            label="毕业院校">
        </el-table-column>
        <el-table-column
            prop="beginDate"
            width="95"
            align="center"
            label="入职日期">
        </el-table-column>
        <el-table-column
            prop="conversionTime"
            width="95"
            align="center"
            label="转正日期">
        </el-table-column>
        <el-table-column
            prop="beginContract"
            width="100"
            align="center"
            label="合同起始日期">
        </el-table-column>
        <el-table-column
            prop="endContract"
            width="100"
            align="center"
            label="合同截止日期">
        </el-table-column>
        <el-table-column
            width="100"
            align="center"
            label="合同期限">
          <template slot-scope="scope">
            <el-tag>{{scope.row.contractTerm}}</el-tag>
            年
          </template>
        </el-table-column>
        <el-table-column
            fixed="right"
            width="200"
            align="center"
            label="操作">
          <template slot-scope="scope">
            <el-button style="padding: 3px" size="mini" @click="showEditEmpView(scope.row)">编辑</el-button>
            <el-button style="padding: 3px" size="mini" type="primary">查看高级资料</el-button>
            <el-button style="padding: 3px" size="mini" type="danger" @click="deleteEmp(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--分页条-->
      <div style="display: flex; justify-content: flex-end">
        <el-pagination
            background
            @current-change="currentChange"
            @size-change="sizeChange"
            layout="sizes, prev, pager, next, jumper, ->, total, slot"
            :total="total">
        </el-pagination>
      </div>
    </div>

    <!--添加操作面板-->
    <el-dialog style="font-family: 华文行楷"
               :title="title"
               :visible.sync="dialogVisible"
               width="80%">
      <!--主面板-->
      <div style="font-family: 华文行楷">
        <el-form :model="emp" :rules="rules" ref="EmpForm">
          <!--第一行start-->
          <el-row>
            <el-col :span="6">
              <el-form-item label="姓名:" prop="name">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit" v-model="emp.name"
                          placeholder="请输入新员工の姓名~ ~"></el-input>
              </el-form-item>
            </el-col>
            <!--性别-->
            <el-col :span="5">
              <el-form-item label="性别:" prop="gender">
                <el-radio-group v-model="emp.gender">
                  <el-radio label="男">男</el-radio>
                  <el-radio label="女">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <!--出生日期-->
            <el-col :span="6">
              <el-form-item size="mini" label="出生日期:" prop="birthday">
                <el-date-picker
                    v-model="emp.birthday"
                    value-format="yyyy-MM-dd"
                    style="width: 150px"
                    placeholder="请选择出生日期~ ~">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <!--政治面貌-->
            <el-col :span="7">
              <el-form-item label="政治面貌:" prop="politicId">
                <el-select v-model="emp.politicId" placeholder="请选择对应の政治面貌~ ~" size="mini" style="width: 200px">
                  <el-option
                      v-for="item in politicsstatus"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <!--第一行end-->

          <!--第二行start-->
          <el-row>
            <el-col :span="6">
              <el-form-item label="民族:" prop="nationId">
                <el-select v-model="emp.nationId" placeholder="请选择对应の民族~ ~" size="mini" style="width: 150px">
                  <el-option
                      v-for="item in nations"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="籍贯:" prop="nativePlace">
                <el-input size="mini" style="width: 120px" prefix-icon="el-icon-edit" v-model="emp.nativePlace"
                          placeholder="请输入新员工の籍贯~ ~"></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="电子邮箱:" prop="email">
                <el-input size="mini" style="width: 155px" prefix-icon="el-icon-message" v-model="emp.email"
                          placeholder="请输入新员工の电子邮箱~ ~"></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="7">
              <el-form-item label="联系地址:" prop="address">
                <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit" v-model="emp.address"
                          placeholder="请输入新员工の联系地址~ ~"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <!--第二行end-->

          <!--第三行start-->
          <el-row>
            <el-col :span="6">
              <el-form-item label="职位:" prop="posId">
                <el-select v-model="emp.posId" placeholder="请选择对应の职位~ ~" size="mini" style="width: 150px">
                  <el-option
                      v-for="item in positions"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="5">
              <el-form-item label="职称:" prop="jobLevelId">
                <el-select v-model="emp.jobLevelId" placeholder="请选择对应の职称~ ~" size="mini" style="width: 120px">
                  <el-option
                      v-for="item in jobLevels"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="所属部门:" prop="departmentId">
                <el-popover
                    placement="right"
                    title="所属部门选择操作面板"
                    width="220"
                    trigger="manual"
                    v-model="PopVisible">
                  <el-tree default-expand-all :data="DepData" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
                  <div  slot="reference"   class="department_div" @click="showDepView">
                    {{inputDepName}}
                  </div>
                </el-popover>
              </el-form-item>
            </el-col>

            <el-col :span="7">
              <el-form-item label="电话号码:" prop="phone">
                <el-input prefix-icon="el-icon-phone" size="mini" style="width: 200px"  v-model="emp.phone"
                          placeholder="请输入新员工の电话号码~ ~"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <!--第三行end-->

          <!--第四行start-->
          <el-row>
            <el-col :span="6">
              <el-form-item label="工号:" prop="workID">
                <el-input size="mini" style="width: 150px"  v-model="emp.workID"
                          placeholder="请输入新员工の工号~ ~" disabled></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="学历:" prop="tiptopDegree">
                <el-select v-model="emp.tiptopDegree" placeholder="请选择对应の学历~ ~" size="mini" style="width: 120px">
                  <el-option
                      v-for="item in tiptopDegrees"
                      :key="item"
                      :label="item"
                      :value="item">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="毕业院校:" prop="school">
                <el-input size="mini" style="width: 150px"  v-model="emp.school"
                          placeholder="请输入新员工の毕业院校~ ~"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="所学专业:" prop="specialty">
                <el-input size="mini" style="width: 200px"  v-model="emp.specialty"
                          placeholder="请输入新员工の所学专业~ ~"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <!--第四行end-->

          <!--第五行start-->
          <el-row>
            <el-col :span="6">
              <el-form-item label="入职日期:" prop="beginDate">
                <el-date-picker
                    v-model="emp.beginDate"
                    size="mini"
                    type="date"
                    value-format="yyyy-MM-dd"
                    style="width: 130px;"
                    placeholder="入职日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="转正日期:" prop="conversionTime">
                <el-date-picker
                    v-model="emp.conversionTime"
                    size="mini"
                    type="date"
                    value-format="yyyy-MM-dd"
                    style="width: 130px;"
                    placeholder="转正日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="合同起始日期:" prop="beginContract">
                <el-date-picker
                    v-model="emp.beginContract"
                    size="mini"
                    type="date"
                    value-format="yyyy-MM-dd"
                    style="width: 130px;"
                    placeholder="合同起始日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="合同终止日期:" prop="endContract">
                <el-date-picker
                    v-model="emp.endContract"
                    size="mini"
                    type="date"
                    value-format="yyyy-MM-dd"
                    style="width: 150px;"
                    placeholder="合同终止日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <!--第五行end-->

          <!--第六行start-->
          <el-row>
            <el-col :span="8">
              <el-form-item label="身份证号码:" prop="idCard">
                <el-input size="mini" style="width: 180px" prefix-icon="el-icon-edit"
                          v-model="emp.idCard" placeholder="请输入新员工の身份证号~ ~"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="聘用形式:" prop="engageForm">
                <el-radio-group v-model="emp.engageForm">
                  <el-radio label="劳动合同">劳动合同</el-radio>
                  <el-radio label="劳务合同">劳务合同</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="婚姻状况:" prop="wedlock">
                <el-radio-group v-model="emp.wedlock">
                  <el-radio label="已婚">已婚</el-radio>
                  <el-radio label="未婚">未婚</el-radio>
                  <el-radio label="离异">离异</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <!--第六行end-->
        </el-form>
      </div>

      <span slot="footer" class="dialog-footer">
    <el-button icon="el-icon-circle-close" @click="dialogVisible = false">取 消</el-button>
    <el-button icon="el-icon-circle-check" type="primary" @click="doAddEmp(emp)">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "EmpBasic",
  data() {
    return {
      PopVisible: false,
      popVisible2: false,
      DepData:[],
      inputDepName: '',
      title:'',
      importDataBtnText: '员工数据の导入',
      importDataBtnIcon: 'el-icon-upload2',
      importDataDisabled: false,
      showAdvanceSearchView: false,
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      rules:{
        name: [{required: true, message: '请先完成用户名的输入~ ~', trigger: 'blur'}],
        gender: [{required: true, message: '请先完成性别的输入~ ~', trigger: 'blur'}],
        birthday: [{required: true, message: '请先完成出生日期的输入~ ~', trigger: 'blur'}],
        idCard: [{required: true, message: '请先完成身份证号码的输入~ ~', trigger: 'blur'}, {
          pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
          message: '身份证号码格式输入不正确,请重试',
          trigger: 'blur'
        }],
        wedlock: [{required: true, message: '请先完成婚姻状况的输入~ ~', trigger: 'blur'}],
        nationId: [{required: true, message: '请先完成您组的输入~ ~', trigger: 'blur'}],
        nativePlace: [{required: true, message: '请先完成籍贯V', trigger: 'blur'}],
        politicId: [{required: true, message: '请先完成政治面貌的输入~ ~', trigger: 'blur'}],
        email: [{required: true, message: '请先完成邮箱地址的输入~ ~', trigger: 'blur'}, {
          type: 'email',
          message: '邮箱输入的格式不正确,请重试',
          trigger: 'blur'
        }],
        phone: [{required: true, message: '请先完成电话号码的输入~ ~', trigger: 'blur'}],
        address: [{required: true, message: '请先完成员工地址的输入~ ~', trigger: 'blur'}],
        departmentId: [{required: true, message: '请先完成部门名称的输入~ ~', trigger: 'blur'}],
        jobLevelId: [{required: true, message: '请先完成职称的输入~ ~', trigger: 'blur'}],
        posId: [{required: true, message: '请先完成职位的输入~ ~', trigger: 'blur'}],
        engageForm: [{required: true, message: '请先完成聘用形式的输入~ ~', trigger: 'blur'}],
        tiptopDegree: [{required: true, message: '请先完成学历的输入~ ~', trigger: 'blur'}],
        specialty: [{required: true, message: '请先完成专业的输入~ ~', trigger: 'blur'}],
        school: [{required: true, message: '请先完成毕业院校的输入~ ~', trigger: 'blur'}],
        beginDate: [{required: true, message: '请先完成入职日期的输入~ ~', trigger: 'blur'}],
        workState: [{required: true, message: '请先完成工作状态的输入~ ~', trigger: 'blur'}],
        workID: [{required: true, message: '请先完成工号的输入~ ~', trigger: 'blur'}],
        contractTerm: [{required: true, message: '请先完成合同期限的输入~ ~', trigger: 'blur'}],
        conversionTime: [{required: true, message: '请先完成转正日期的输入~ ~', trigger: 'blur'}],
        notworkDate: [{required: true, message: '请先完成离职日期的输入~ ~', trigger: 'blur'}],
        beginContract: [{required: true, message: '请先完成合同起始日期的输入~ ~', trigger: 'blur'}],
        endContract: [{required: true, message: '请先完成合同结束日期的输入~ ~', trigger: 'blur'}],
        workAge: [{required: true, message: '请先完成工龄的输入~ ~', trigger: 'blur'}],
      },
      searchValue: {
        politicId: null,
        nationId: null,
        jobLevelId: null,
        posId: null,
        engageForm: null,
        departmentId: null,
        beginDateScope: null
      },
      emps: [],
      loading: false,
      jobLevels: [],
      nations: [],
      politicsstatus: [],
      positions: [],
      total: 0,
      page: 1,
      keyword: '',
      size: 10,
      dialogVisible: false,
      tiptopDegrees: ['博士','研究生','本科','大专','高中','初中','小学','中专','博士后'],
      emp: {
        name: "高文",
        gender: "男",
        birthday: "1684-12-31",
        idCard: "610122199001011256",
        wedlock: "已婚",
        nationId: 1,
        nativePlace: "圆桌骑士堡",
        politicId: 13,
        email: "gaowen@gmail.com",
        phone: "18565558897",
        address: "大不列颠",
        departmentId: null,
        jobLevelId: 9,
        posId: 29,
        engageForm: "劳务合同",
        tiptopDegree: "本科",
        specialty: "信息管理与信息系统",
        school: "伦敦大学",
        beginDate: "2017-12-31",
        workState: "在职",
        workID: "00000057",
        contractTerm: 2,
        conversionTime: "2018-03-31",
        notworkDate: null,
        beginContract: "2017-12-31",
        endContract: "2019-12-31",
        workAge: null
      },

    }
  },
  mounted() {
    this.initEmps();
    this.initPositions();
    this.initData();
  },
  methods: {

    //数据导入时文件上传之前处理
    BeforeUpload(){
      this.importDataBtnText = '正在进行数据导入,请稍后~ ~';
      this.importDataBtnIcon= 'el-icon-loading';
      this.importDataDisabled= true;
    },
    //数据导入时文件上传成功处理
    onSuccess(response, file, fileList){
      this.importDataBtnText = '员工数据の导入';
      this.importDataBtnIcon= 'el-icon-upload2';
      this.importDataDisabled= false;
      this.initEmps();
    },
    //数据导入时文件上传失败处理
    onUpError(err, file, fileList){
      this.importDataBtnText = '员工数据の导入';
      this.importDataBtnIcon= 'el-icon-upload2'
      this.importDataDisabled= false;
    },

    //数据的导出,_parent： 表示在当前窗口进行操作
    exportEmp(){
      window.open("/employee/basic/export",'_parent');
    },

    //编辑后emp置空
    emptyEmp(){
      this.emp = {

        name: "javaboy",
        gender: "男",
        birthday: "1989-12-31",
        idCard: "610122199001011256",
        wedlock: "已婚",
        nationId: 1,
        nativePlace: "陕西",
        politicId: 13,
        email: "laowang@qq.com",
        phone: "18565558897",
        address: "深圳市南山区",
        departmentId: null,
        jobLevelId: 9,
        posId: 29,
        engageForm: "劳务合同",
        tiptopDegree: "本科",
        specialty: "信息管理与信息系统",
        school: "深圳大学",
        beginDate: "2017-12-31",
        workState: "在职",
        workID: "00000057",
        contractTerm: 2,
        conversionTime: "2018-03-31",
        notworkDate: null,
        beginContract: "2017-12-31",
        endContract: "2019-12-31",
        workAge: null

        /*name: "",
        gender: "",
        birthday: "",
        idCard: "",
        wedlock: "",
        nationId: 1,
        nativePlace: "",
        politicId: 13,
        email: "",
        phone: "",
        address: "",
        departmentId: null,
        jobLevelId: 9,
        posId: 29,
        engageForm: "",
        tiptopDegree: "",
        specialty: "",
        school: "",
        beginDate: "",
        workID: "",
        contractTerm: 2,
        conversionTime: "",
        notworkDate: null,
        beginContract: "",
        endContract: "",
        workAge: null*/
      }
      this.inputDepName= '';
    },

    //编辑模块操作
    showEditEmpView(data){
      this.title= '员工编辑操作面板';
      this.emp = data;
      this.inputDepName = data.department.name
      this.initPositions();
      this.dialogVisible = true;
    },

    //删除操作
    deleteEmp(data){
      this.$confirm('此操作将永久删除名叫【'+data.name+'】员工资料, 是否继续?', '提示', {
        confirmButtonText: '确定执行删除操作',
        cancelButtonText: '取消该操作',
        type: 'warning'
      }).then(() => {
        this.deleteRequest("/employee/basic/"+data.id).then(resp=>{
          if(resp){
            this.initEmps();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '删除操作已成功取消'
        });
      });
    },

    //根据id进行判断,有id就进行更新操作,没有就进行员工添加操作
    doAddEmp(){
      if(this.emp.id){
        this.$refs['EmpForm'].validate(valid=>{
          if(valid){
            this.putRequest("/employee/basic/",this.emp).then(resp=>{
              if (resp){
                this.dialogVisible = false;
                this.initEmps();
              }
            })
          }
        })
      }else {
        this.$refs['EmpForm'].validate(valid=>{
          if(valid){

            this.postRequest("/employee/basic/",this.emp).then(resp=>{
              if (resp){
                this.dialogVisible = false;
                this.initEmps();
              }
            })
          }
        })
      }
    },

    searchViewHandleNodeClick(data){
      this.inputDepName = data.name;
      this.searchValue.departmentId = data.id
      this.popVisible2 = !this.popVisible2;
    },

    handleNodeClick(data){
      this.inputDepName = data.name;
      this.emp.departmentId = data.id;
      this.PopVisible = !this.PopVisible;
    },

    //拿到所有的部门
    initDeps(){
      this.getRequest("/employee/basic/allDeps").then(resp=>{
        if(resp){
          this.DepData = resp;
        }
      })
    },

    //所属部门事件
    showDepView(){
      this.PopVisible = !this.PopVisible;
      this.initDeps();
    },
    showDepView2(){
      this.popVisible2 = !this.popVisible2;
      this.initDeps();
    },

    getMaxWorkID(){
      this.getRequest("/employee/basic/maxworkid").then(resp=>{
        this.emp.workID = resp.obj;
      })
    },

    initPositions(){
      this.getRequest("/employee/basic/positions").then(resp=>{
        if (resp){
          this.positions = resp;
        }
      })
    },

    //初始化 民族 职称 政治面貌数据
    initData(){
      if(!window.sessionStorage.getItem("nations")){
        this.getRequest("/employee/basic/nations").then(resp=>{
          if (resp){
            this.nations = resp;
            window.sessionStorage.setItem("nations",JSON.stringify(resp));
          }
        })
      }else {
        this.nations = JSON.parse(window.sessionStorage.getItem("nations"));
      }
      if(!window.sessionStorage.getItem("jobLevels")){
        this.getRequest("/employee/basic/jobLevels").then(resp=>{
          if (resp){
            this.jobLevels = resp;
            window.sessionStorage.setItem("jobLevels",JSON.stringify(resp));
          }
        })
      }else {
        this.jobLevels = JSON.parse(window.sessionStorage.getItem("jobLevels"));
      }
      if(!window.sessionStorage.getItem("politicsstatus")){
        this.getRequest("/employee/basic/politicsstatus").then(resp=>{
          if (resp){
            this.politicsstatus = resp;
            window.sessionStorage.setItem("politicsstatus",JSON.stringify(resp));
          }
        })
      }else {
        this.politicsstatus = JSON.parse(window.sessionStorage.getItem("politicsstatus"));
      }
    },
    //添加操作
    showAddEmpView() {
      this.emptyEmp();
      this.title='员工信息添加操作所属面板';
      this.getMaxWorkID();
      this.dialogVisible = true;
    },
    //分页条数
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initEmps();
    },

    //分页
    currentChange(currentChange) {
      this.page = currentChange;
      this.initEmps('advanced');
    },
    //emps数据初始化
    initEmps(type) {
      this.loading = true;
      let url = '/employee/basic/?page=' + this.page + '&size=' + this.size;
      if (type && type == 'advanced') {
        if (this.searchValue.politicId) {
          url += '&politicId=' + this.searchValue.politicId;
        }
        if (this.searchValue.nationId) {
          url += '&nationId=' + this.searchValue.nationId;
        }
        if (this.searchValue.jobLevelId) {
          url += '&jobLevelId=' + this.searchValue.jobLevelId;
        }
        if (this.searchValue.posId) {
          url += '&posId=' + this.searchValue.posId;
        }
        if (this.searchValue.engageForm) {
          url += '&engageForm=' + this.searchValue.engageForm;
        }
        if (this.searchValue.departmentId) {
          url += '&departmentId=' + this.searchValue.departmentId;
        }
        if (this.searchValue.beginDateScope) {
          url += '&beginDateScope=' + this.searchValue.beginDateScope;
        }
      } else {
        url += "&name=" + this.keyword;
      }
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
          this.total = resp.total;
        }
      })
    }
  }
}
</script>

<style>

/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all .8s ease;
}
.slide-fade-leave-active {
  transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}
.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */ {
  transform: translateX(10px);
  opacity: 0;
}

.seachDiv{
  border: 1px solid #409eff;
  border-radius: 5px;
  box-sizing: border-box;
  padding: 5px 0px;
  margin: 10px 0px;

}

.three_button {
  display: flex;
  justify-content: space-between;
}
.department_div{
  width: 130px;
  height: 27px;
  display: inline-flex;
  font-size: 13px;
  border: 1px solid #dedede;
  font-family: 华文行楷;
  border-radius: 4px;
  cursor: pointer;
  align-items: center;
  padding-left: 8px;
  box-sizing: border-box;
  margin-left: 4px;
}

.department_div2{
  width: 130px;
  height: 27px;
  display: inline-flex;
  font-size: 13px;
  border: 1px solid #dedede;
  font-family: 华文行楷;
  border-radius: 4px;
  cursor: pointer;
  align-items: center;
  padding-left: 8px;
  box-sizing: border-box;
  margin-left: 4px;
}
</style>