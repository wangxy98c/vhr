//封装axios，使得具有统一的请求返回样式
import axios from 'axios'
import { ElMessage } from 'element-plus'
//单独引用（不在vue文件中，不能直接用）

//响应拦截器
axios.interceptors.response.use(function (response) {
    // 2xx (http的) 范围内的状态码都会触发该函数,对响应数据做点什么
    //但注意，这个2XX不是后端设置的ok，error里的那个码。postman下面的status200 OK
    if(response.status && response.status==200 && response.data.status==500){
        //data.status 是后端的码.500是业务错误
        ElMessage.error(response.data.msg);//把后端错误信息展示出来
        return;
    }
    if(response.data.msg){
        ElMessage.success(response.data.msg);
    }
    //下面也可以写成功的提醒，此处省略
    return response.data;//此处有data，其他地方可能不需要.data了
  }, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数,对响应错误做点什么
    if(error.response.status==504||error.response.status==404){
        ElMessage.error('服务器丢失!');
    }else if(error.response.status==403){
        ElMessage.error('权限不足');
    }else if(error.response.status==401){
        ElMessage.error('尚未登陆，请登陆后操作');
    }else{
        if(error.response.data.msg){
            console.log("msg:");
            ElMessage.error(error.response.data.msg);
        }else{
            ElMessage.error('未知错误');
        }
    }
  });

const baseUrl='/api';
export function postKeyValueRequest(url:string,params:any){
    return axios({
        method: 'post',
        url: url,
        baseURL: baseUrl,
        data: params,
        transformRequest: [function (data, headers) {
            // 对发送的 data 进行任意转换处理,此处是把data转化为keyValue形式
            let ret='';
            for(let i in data){
                ret+=encodeURIComponent(i)+'='+encodeURIComponent(data[i])+'&';
            }
            console.log(ret);
            return ret;
        }],
        headers: {
            'Content-Type':'application/x-www-form-urlencoded'
        }
    })//也可以用别名方式
}

export function postRequest(url:string,params:any){
    return axios({
        method: 'post',
        url: url,
        baseURL: baseUrl,
        data: params
    })
}
export function putRequest(url:string,params:any){
    return axios({
        method: 'put',
        url: url,
        baseURL: baseUrl,
        data: params
    })
}
export function getRequest(url:string,params:any){
    return axios({
        method: 'get',
        url: url,
        baseURL: baseUrl,
        data: params
    })
}
export function deleteRequest(url:string,params:any){
    return axios({
        method: 'delete',
        url: url,
        baseURL: baseUrl,
        data: params
    })
}