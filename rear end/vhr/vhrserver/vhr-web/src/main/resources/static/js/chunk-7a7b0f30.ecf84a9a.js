(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-7a7b0f30"],{"0aaf":function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[e._v(" 综合信息统计 ")])},n=[],i={name:"StaAll"},r=i,s=a("2877"),c=Object(s["a"])(r,l,n,!1,null,"71464d5f",null);t["default"]=c.exports},1937:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[e._v(" 工资表查询 ")])},n=[],i={name:"SalSearch"},r=i,s=a("2877"),c=Object(s["a"])(r,l,n,!1,null,"564a95f9",null);t["default"]=c.exports},"21c7":function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[e._v(" 工资表管理 ")])},n=[],i={name:"SalTable"},r=i,s=a("2877"),c=Object(s["a"])(r,l,n,!1,null,"bca3269e",null);t["default"]=c.exports},"2bc5":function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",{staticStyle:{display:"flex","justify-content":"space-between"}},[a("el-button",{attrs:{type:"primary",icon:"el-icon-circle-plus"},on:{click:e.showAddSalaryView}},[e._v("添加工资账套")]),a("el-button",{attrs:{type:"success",icon:"el-icon-refresh"},on:{click:e.initsalaries}})],1),a("div",{staticStyle:{"margin-top":"10px"}},[a("el-table",{staticClass:"salTab",attrs:{data:e.salaries,border:"",stripe:""}},[a("el-table-column",{attrs:{align:"center",type:"selection",width:"55"}}),a("el-table-column",{attrs:{align:"center",width:"120",prop:"name",label:"账套名称"}}),a("el-table-column",{attrs:{align:"center",width:"75",prop:"basicSalary",label:"基本工资"}}),a("el-table-column",{attrs:{align:"center",width:"75",prop:"trafficSalary",label:"交通补助"}}),a("el-table-column",{attrs:{align:"center",width:"75",prop:"lunchSalary",label:"午餐补助"}}),a("el-table-column",{attrs:{align:"center",width:"70",prop:"bonus",label:"奖金"}}),a("el-table-column",{attrs:{align:"center",width:"100",prop:"createDate",label:"启用时间"}}),e._v(" //多级表头 "),a("el-table-column",{attrs:{label:"养老金",align:"center"}},[a("el-table-column",{attrs:{align:"center",width:"70",prop:"pensionPer",label:"比率"}}),a("el-table-column",{attrs:{align:"center",width:"70",prop:"pensionBase",label:"基数"}})],1),e._v(" //多级表头 "),a("el-table-column",{attrs:{label:"医疗保险",align:"center"}},[a("el-table-column",{attrs:{align:"center",width:"70",prop:"medicalPer",label:"比率"}}),a("el-table-column",{attrs:{align:"center",width:"70",prop:"medicalBase",label:"基数"}})],1),e._v(" //多级表头 "),a("el-table-column",{attrs:{label:"公积金",align:"center"}},[a("el-table-column",{attrs:{align:"center",width:"70",prop:"accumulationFundPer",label:"比率"}}),a("el-table-column",{attrs:{align:"center",width:"70",prop:"accumulationFundBase",label:"基数"}})],1),a("el-table-column",{attrs:{align:"center",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{on:{click:function(a){return e.showEditSalView(t.row)}}},[e._v("编辑")]),a("el-button",{attrs:{type:"danger"},on:{click:function(a){return e.deleteSal(t.row)}}},[e._v("删除")])]}}])})],1)],1),a("el-dialog",{staticStyle:{"font-family":"华文行楷"},attrs:{title:e.dialogVisibleTitle,visible:e.dialogVisible,width:"50%"},on:{"update:visible":function(t){e.dialogVisible=t}}},[a("div",{staticStyle:{display:"flex","justify-content":"space-around","align-items":"center"}},[a("el-steps",{attrs:{direction:"vertical",active:e.activeItemIndex}},e._l(e.salaryItemName,(function(e,t){return a("el-step",{key:t,attrs:{title:e}})})),1),e._l(e.salary,(function(t,l,n){return a("el-input",{directives:[{name:"show",rawName:"v-show",value:e.activeItemIndex==n,expression:"activeItemIndex==index"}],key:n,staticStyle:{width:"250px"},attrs:{"prefix-icon":"el-icon-edit",placeholder:"请输入账套の"+e.salaryItemName[n]+"~ ~"},nativeOn:{keydown:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.nextStep(t)}},model:{value:e.salary[l],callback:function(t){e.$set(e.salary,l,t)},expression:"salary[title]"}})}))],2),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:e.perStep}},[e._v(e._s(10==e.activeItemIndex?"取消":"上一步"))]),a("el-button",{attrs:{type:"primary"},on:{click:e.nextStep}},[e._v(e._s(10==e.activeItemIndex?"完成":"下一步"))])],1)])],1)},n=[],i=(a("b0c0"),{name:"SalSob",data:function(){return{salaries:[],dialogVisibleTitle:"工资账套添加の操作面板",dialogVisible:!1,activeItemIndex:0,salaryItemName:["基本工资","交通补助","午餐补助","奖金","养老金比率","养老金基数","医疗保险比率","医疗保险基数","公积金比率","公积金基数","账套名称"],salary:{basicSalary:0,trafficSalary:0,lunchSalary:0,bonus:0,pensionPer:0,pensionBase:0,medicalPer:0,medicalBase:0,accumulationFundPer:0,accumulationFundBase:0,name:""}}},mounted:function(){this.initsalaries()},methods:{showEditSalView:function(e){this.dialogVisibleTitle="工资账套编辑の操作面板",this.dialogVisible=!0,this.salary.basicSalary=e.basicSalary,this.salary.trafficSalary=e.trafficSalary,this.salary.lunchSalary=e.lunchSalary,this.salary.bonus=e.bonus,this.salary.pensionPer=e.pensionPer,this.salary.pensionBase=e.pensionBase,this.salary.medicalPer=e.medicalPer,this.salary.medicalBase=e.medicalBase,this.salary.accumulationFundPer=e.accumulationFundPer,this.salary.accumulationFundBase=e.accumulationFundBase,this.salary.name=e.name,this.salary.id=e.id,this.activeItemIndex=0},deleteSal:function(e){var t=this;this.$confirm("此操作将永久删除名称为【"+e.name+"】の套账, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){t.deleteRequest("/salary/sob/"+e.id).then((function(e){e&&t.initsalaries()}))})).catch((function(){t.$message({type:"info",message:"删除操作已取消"})}))},perStep:function(){0!=this.activeItemIndex&&(10!=this.activeItemIndex?this.activeItemIndex--:this.dialogVisible=!1)},nextStep:function(){var e=this;10!=this.activeItemIndex?this.activeItemIndex++:this.salary.id?this.putRequest("/salary/sob/",this.salary).then((function(t){t&&(e.initsalaries(),e.dialogVisible=!1)})):this.postRequest("/salary/sob/",this.salary).then((function(t){t&&(e.initsalaries(),e.dialogVisible=!1)}))},showAddSalaryView:function(){this.salary={basicSalary:0,trafficSalary:0,lunchSalary:0,bonus:0,pensionPer:0,pensionBase:0,medicalPer:0,medicalBase:0,accumulationFundPer:0,accumulationFundBase:0,name:""},this.dialogVisibleTitle="工资账套添加の操作面板",this.activeItemIndex=0,this.dialogVisible=!0},initsalaries:function(){var e=this;this.getRequest("/salary/sob/").then((function(t){t&&(e.salaries=t)}))}}}),r=i,s=(a("6c55"),a("2877")),c=Object(s["a"])(r,l,n,!1,null,null,null);t["default"]=c.exports},"6ac98":function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",[a("el-table",{staticStyle:{"font-family":"华文行楷"},attrs:{data:e.emps,border:"",stripe:"",size:"mini"}},[a("el-table-column",{attrs:{type:"selection",align:"left",width:"55"}}),a("el-table-column",{attrs:{prop:"name",label:"姓名",fixed:"",width:"120",align:"left"}}),a("el-table-column",{attrs:{prop:"workID",label:"工号",width:"120",align:"left"}}),a("el-table-column",{attrs:{prop:"email",label:"电子邮件",width:"200",align:"left"}}),a("el-table-column",{attrs:{prop:"phone",label:"电话号码",width:"120",align:"left"}}),a("el-table-column",{attrs:{prop:"department.name",label:"所属部门",width:"120",align:"left"}}),a("el-table-column",{attrs:{label:"工资账套",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[t.row.salary?a("el-tooltip",{attrs:{placement:"right"}},[a("div",{attrs:{slot:"content"},slot:"content"},[a("table",[a("tr",[a("td",[e._v("基本工资")]),a("td",[e._v(e._s(t.row.salary.basicSalary))])]),a("tr",[a("td",[e._v("交通补助")]),a("td",[e._v(e._s(t.row.salary.trafficSalary))])]),a("tr",[a("td",[e._v("午餐补助")]),a("td",[e._v(e._s(t.row.salary.lunchSalary))])]),a("tr",[a("td",[e._v("奖金")]),a("td",[e._v(e._s(t.row.salary.bonus))])]),a("tr",[a("td",[e._v("养老金比率")]),a("td",[e._v(e._s(t.row.salary.pensionPer))])]),a("tr",[a("td",[e._v("养老金基数")]),a("td",[e._v(e._s(t.row.salary.pensionBase))])]),a("tr",[a("td",[e._v("医疗保险比率")]),a("td",[e._v(e._s(t.row.salary.medicalPer))])]),a("tr",[a("td",[e._v("医疗保险基数")]),a("td",[e._v(e._s(t.row.salary.medicalBase))])]),a("tr",[a("td",[e._v("公积金比率")]),a("td",[e._v(e._s(t.row.salary.accumulationFundPer))])]),a("tr",[a("td",[e._v("公积金基数")]),a("td",[e._v(e._s(t.row.salary.accumulationFundBase))])]),a("tr",[a("td",[e._v("启用时间")]),a("td",[e._v(e._s(t.row.salary.createDate))])])])]),a("el-tag",[e._v(e._s(t.row.salary.name))])],1):a("el-tag",[e._v("暂未设置")])]}}])}),a("el-table-column",{attrs:{label:"操作",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-popover",{staticStyle:{"font-family":"华文行楷"},attrs:{placement:"left",title:"工资账套の修改操作面板",width:"200",trigger:"click"},on:{show:function(a){return e.showPop(t.row.salary)}}},[a("div",[a("el-select",{attrs:{placeholder:"请选择",size:"mini"},model:{value:e.currentSalary,callback:function(t){e.currentSalary=t},expression:"currentSalary"}},e._l(e.salaries,(function(e){return a("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})})),1),a("div",{staticStyle:{"margin-top":"10px",display:"flex","justify-content":"center"}},[a("el-button",{attrs:{size:"mini",type:"primary"},on:{click:function(a){return e.updateSal(t.row)}}},[e._v("确定数据更新")])],1)],1),a("el-button",{attrs:{slot:"reference",type:"danger"},slot:"reference"},[e._v("工资账套の修改")])],1)]}}])})],1),a("div",{staticStyle:{display:"flex","justify-content":"flex-end"}},[a("el-pagination",{attrs:{background:"",layout:"sizes, prev, pager, next, jumper, ->, total, slot",total:e.total},on:{"size-change":e.sizeChange,"current-change":e.currentChange}})],1)],1)])},n=[],i={name:"SalSobCfg",data:function(){return{emps:[],total:0,currentPage:1,currentSize:10,currentSalary:null,salaries:[]}},mounted:function(){this.initEmps(),this.initSalaries()},methods:{updateSal:function(e){var t=this;this.currentSalary&&this.putRequest("/salary/sobcfg/?eid="+e.id+"&sid="+this.currentSalary).then((function(e){e&&t.initEmps()}))},sizeChange:function(e){this.currentSize=e,this.initEmps()},currentChange:function(e){this.currentPage=e,this.initEmps()},showPop:function(e){this.currentSalary=e?e.id:null},initSalaries:function(){var e=this;this.getRequest("/salary/sobcfg/salies").then((function(t){t&&(e.salaries=t)}))},initEmps:function(){var e=this;this.getRequest("/salary/sobcfg/?page="+this.currentPage+"&size="+this.currentSize).then((function(t){t&&(e.emps=t.data,e.total=t.total)}))}}},r=i,s=a("2877"),c=Object(s["a"])(r,l,n,!1,null,"3f6031e6",null);t["default"]=c.exports},"6c55":function(e,t,a){"use strict";var l=a("c045"),n=a.n(l);n.a},a49a:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[e._v(" 人事记录统计 ")])},n=[],i={name:"StaRecord"},r=i,s=a("2877"),c=Object(s["a"])(r,l,n,!1,null,"0f16d65e",null);t["default"]=c.exports},b4192:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[e._v(" 人事信息统计 ")])},n=[],i={name:"StaPers"},r=i,s=a("2877"),c=Object(s["a"])(r,l,n,!1,null,"5b97df8c",null);t["default"]=c.exports},c045:function(e,t,a){},c1d3:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[e._v(" 员工积分统计 ")])},n=[],i={name:"StaScore"},r=i,s=a("2877"),c=Object(s["a"])(r,l,n,!1,null,"14a28673",null);t["default"]=c.exports},fbac:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[e._v(" 月末处理 ")])},n=[],i={name:"SalMonth"},r=i,s=a("2877"),c=Object(s["a"])(r,l,n,!1,null,"51ab19b2",null);t["default"]=c.exports},feca:function(e,t,a){var l={"./Home.vue":"bb51","./HrInfo.vue":"92d8","./Login.vue":"a55b","./chat/FriendChat.vue":"8a18","./emp/EmpAdv.vue":"8759","./emp/EmpBasic.vue":"58da","./per/PerEc.vue":"d9ac","./per/PerEmp.vue":"5865","./per/PerMv.vue":"cfbb","./per/PerSalary.vue":"5a4a","./per/PerTrain.vue":"83ed","./sal/SalMonth.vue":"fbac","./sal/SalSearch.vue":"1937","./sal/SalSob.vue":"2bc5","./sal/SalSobCfg.vue":"6ac98","./sal/SalTable.vue":"21c7","./sta/StaAll.vue":"0aaf","./sta/StaPers.vue":"b4192","./sta/StaRecord.vue":"a49a","./sta/StaScore.vue":"c1d3","./sys/SysBasic.vue":"8d67","./sys/SysCfg.vue":"1fe7","./sys/SysData.vue":"d1e3","./sys/SysHr.vue":"418a","./sys/SysInit.vue":"8608","./sys/SysLog.vue":"864e"};function n(e){var t=i(e);return a(t)}function i(e){if(!a.o(l,e)){var t=new Error("Cannot find module '"+e+"'");throw t.code="MODULE_NOT_FOUND",t}return l[e]}n.keys=function(){return Object.keys(l)},n.resolve=i,e.exports=n,n.id="feca"}}]);
//# sourceMappingURL=chunk-7a7b0f30.ecf84a9a.js.map