import request from '@/utils/request'

//获取问卷基础数据
export function getQuestionnaire(id) {
    return request({
        url: '/question/' + id,
        method: 'get'
    })
}

//获取问卷详细数据
export function getQuestionnaireDetail(id) {
    return request({
        url: '/question/detail/' + id,
        method: 'get'
    })
}

//获取问卷数据(匿名)
export function getQuestionnaireAnonymous(id) {
    return request({
        url: '/question-anonymous/question/' + id,
        headers: {
            isToken: false
        },
        method: 'get'
    })
}

//新建问卷,至少提供问卷标题，至多提供问卷标题、问卷简介
export function newQuestionnaire({title, introduction}) {
    return request({
        url: '/question/new',
        method: 'post',
        data: {title, introduction}
    })
}

//修改问卷基础属性
export function modifyQuestionnaire(data) {
    return request({
        url: '/question/modify/basic',
        method: 'post',
        data
    })
}

//修改问卷结构
export function modifyQuestionnaireStructure(data) {
    return request({
        url: '/question/modify/structure',
        method: 'post',
        data
    })
}

//列某用户作为**问卷设计者**的某些类型的问卷列表
export function listUserQuestionnaire(userId, type) {
    return request({
        url: '/question/list/' + userId,
        method: 'get',
        params: {type: type}
    })
}

//若问卷不为星标，将问卷设置为星标问卷，否则取消星标
export function starQuestionnaire(id) {
    return request({
        url: '/question/star/' + id,
        method: 'get'
    })
}

//删除问卷，保留问卷结构和收集到的数据
export function deleteQuestionnaire(id) {
    return request({
        url: '/question/delete/' + id,
        method: 'get'
    })
}

//彻底删除问卷，前提是问卷删除标记为1，且没有收集到的数据才能彻底删除
export function deleteQuestionnaireCompletely(id) {
    return request({
        url: '/question/delete/' + id,
        method: 'delete'
    })
}

//删除问卷收集到的数据
export function clearQuestionnaireData(id) {
    return request({
        url: '/question/clear/' + id,
        method: 'get'
    })
}

//恢复问卷
export function recoverQuestionnaire(id) {
    return request({
        url: '/question/recover/' + id,
        method: 'get'
    })
}
