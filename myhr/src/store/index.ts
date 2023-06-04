import { defineStore } from "pinia";
import { getRequest } from "@/utils/api";
import SockJS from 'sockjs-client/dist/sockjs.min.js';
import Stomp from "stompjs"


export const useStore=defineStore('menuStore',{
    state:()=>({
        menuRoutes:[]
    }),
    actions:{
        initMenuStore(data:any){
            this.menuRoutes=data;
        }
    }
})
const now=new Date();
export const usechatstore=defineStore('chatStore',{
    state:()=>({
        sessions:{},
        hrs:[],
        currentSession: '',//当前选中的对话框
        currentHr:'',
        filterKey:'',
        stomp:null
    }),
    actions:{
        changeCurrentSession (user) {
			this.currentSession = user;
		},
		addMessage (msg) {
            this.currentHr=JSON.parse(window.sessionStorage.getItem("user")||'{}');//更好的办法是在登陆页处直接赋值，以后就不用多次调用了，但懒得改了
            console.log(this.currentHr.username+'#'+msg.to)
            //存储的是， 当前登陆用户 和 聊天用户 之间，且当前用户用户永远在前（不管是发送还是收到，都处理成如此）  即 user.username#currentSessionName
            var s=this.currentHr.username+'#'+msg.to
            var ms=this.sessions[s];//session[s] 未定义，属于基本类型undefined 。故而值引用
            if(ms==null){
                this.sessions[s]=[]
            }
			this.sessions[s].push({
                content:msg.content,
                date:new Date(),
                self:!msg.notSelf
            })
            //console.log('typeof:',typeof(this.sessions[s]))
            console.log("session:",this.sessions)
		},//此处的Date格式： 2023-05-31T04:47:47.358Z。需要在显示的地方进行过滤处理
        initData(){
            //处理浏览器本地的聊天记录
            getRequest("/chat/hrs",'').then(resp=>{
                if(resp){
                    this.initHrs(resp);
                }
            })
            this.connect()
        },
        initHrs(hrs){
            this.hrs=hrs;
        },
        connect(){
            this.stomp=Stomp.over(new SockJS("/ws/ep"));//SockJS是链接
            this.stomp.connect({},success=>{
                this.stomp.subscribe("/user/queue/chat",msg=>{
                    //    /user是to的内容。比如.to=‘libai’.
                    //如果.to=“李白”，则信息更新到了 李白/queue/chat上。李白收不到了
                    console.log("收到了订阅的数据:",msg)
                    let reciveMsg=JSON.parse(msg.body);
                    console.log(reciveMsg)
                    reciveMsg.notSelf=true;//不是自己发的消息
                    reciveMsg.to=reciveMsg.from//因为需要存储，以发送时为准currentHr#to。但此处是收到的。to是自己，会变成我#我 ，故而需要处理 
                    reciveMsg.notSelf=true;
                    console.log(reciveMsg)
                    this.addMessage(reciveMsg);
                    //未完成功能：可以添加功能，别人发消息就小窗提醒。以及list右上角小红点
                })
            },error=>{
                console.log("socket-error",error);
            })

        }
    }
})
