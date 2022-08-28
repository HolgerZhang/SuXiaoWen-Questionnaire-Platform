import request from "@/utils/request";

// TODO updateUniAccountInfo
// 角色数据权限
export function updateUniAccountInfo(data) {
    return request({
        url: '/system/user/uniAccount',
        method: 'put',
        data: data
    })
}

// TODO getUniAccountInfo
export function getUniAccountInfo() {
    return request({
        url: '/system/user/uniAccount',
        method: 'get'
    })
}
