import request from "@/utils/request";

export function getCredit() {
    return request({
        url: '/system/user/credit',
        method: 'get'
    })
}

export function rechargeCredit(credit) {
    const param = {
        credit
    };
    return request({
        url: '/system/user/credit/recharge',
        method: 'put',
        params: param
    })
}
