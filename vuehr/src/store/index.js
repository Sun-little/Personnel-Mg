import Vue from 'vue'
import Vuex from 'vuex'
import {getRequest} from "../utils/api";
import { Notification } from 'element-ui';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

Vue.use(Vuex)

const now = new Date();

const store = new Vuex.Store({
    //这里定义的内容可以视为变量
    state: {
        routes: [],
        sessions: {},
        hrs: [],
        currentHr: JSON.parse(window.sessionStorage.getItem("user")),
        CurrentSession: null,
        stomp: null,
        isDot: {}
    },
    mutations: {
        //初始化Hr
        INIT_CURRENTHR(state, hr) {
            state.currentHr = hr;
        },
        //初始化routes
        initRoutes(state, data) {
            state.routes = data;
        },
        changeCurrentSession(state, CurrentSession) {
            //消息红点设置默认为false
            Vue.set(state.isDot, state.currentHr.username + '#' + CurrentSession.username, false);
            state.CurrentSession = CurrentSession;
        },
        //接收到的消息在这里保存
        addMessage(state, msg) {
            let mssg = state.sessions[state.currentHr.username + '#' + msg.to];
            if (!mssg) {
                //让Vue能够检测到state.sessions属性(将对象属性添加到Vue),从而达到聊天消息实时更新
                //第一个参数是要添加属性的对象; 第二个参数是添加的属性名; 第三个参数是属性值
                Vue.set(state.sessions, state.currentHr.username + '#' + msg.to, []);
            }
            state.sessions[state.currentHr.username + '#' + msg.to].push({
                content: msg.content,
                date: new Date(),
                self: !msg.noSelf
            })
        },
        INIT_DATA(state) {
            //浏览器的历史聊天记录可以在此处完成
            let data = localStorage.getItem('vue-chat-session');
            if (data) {
                state.sessions = JSON.parse(data);
            }
        },
        //拿到数据后在此处初始化数据
        INIT_Hr(state, data) {
            state.hrs = data;
        }
    },
    //actions：用于做异步操作,拿到后端数据
    actions: {
        //connect： 用于做前后端 WebSocket 消息连接
        connect(context) {
            context.state.stomp = Stomp.over(new SockJS('/ws/ep'));
            //开始创建前后端消息连接,以及连接成功,连接失败的回调
            context.state.stomp.connect({}, success => {
                //前后端消息互通, 此处需手动加上/user 前缀, 服务端底层定义必须要
                context.state.stomp.subscribe('/user/queue/chat', msg => {
                    //消息接收处理
                    let receiveMsg = JSON.parse(msg.body);//消息本身
                    //消息提示处理
                    if (!context.state.CurrentSession || receiveMsg.from != context.state.CurrentSession.username) {
                        Notification.info({
                            title: '【'+ receiveMsg.fromNickname+'】给您发来了一条消息,请及时查看😊',
                            message: receiveMsg.content.length>10? receiveMsg.content.substr(0,10):receiveMsg.content,
                            position: 'bottom-right'
                        })
                        //当发来消息时红点就会显示否则就不显示
                        Vue.set(context.state.isDot, context.state.currentHr.username + '#' + receiveMsg.from, true);
                    }
                    receiveMsg.noSelf = true;
                    receiveMsg.to = receiveMsg.from;
                    //消息提交(第一参数为提交到哪里去  第二参数为消息本身)
                    context.commit('addMessage', receiveMsg);
                })
            }, error => {

            })
        },

        initData(context) {
            context.commit('INIT_DATA')
            getRequest("/chat/hrs").then(resp => {
                if (resp) {
                    //请求成功,拿到数据进行初始化
                    console.log(resp);
                    context.commit('INIT_Hr', resp);

                }
            })
        }
    }
})
store.watch(function (state) {
    return state.sessions
}, function (val) {
    console.log('CHANGE: ', val);
    localStorage.setItem('vue-chat-session', JSON.stringify(val));
}, {
    deep: true/*这个貌似是开启watch监测的判断,官方说明也比较模糊*/
})

export default store;