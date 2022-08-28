import request from '@/utils/request'

//（设计者）发布任务
export function create(data) {
    return request({
        url: '/help-center/task',
        method: 'post',
        data
    })
}

//（填写者）完成任务
export function finishByTaskId(taskId) {
    return request({
        url: '/help-center/task/finish',
        method: 'get',
        params: {taskId}
    })
}

//（填写者）完成任务
export function finishByQuestionId(questionnaireId) {
    return request({
        url: '/help-center/task/finish/question',
        method: 'get',
        params: {questionnaireId}
    })
}

//(管理员) 调整可用状态: 若任务为可用，则设置为禁用，否则还原可用状态
export function modifyAvailable(taskId) {
    return request({
        url: '/help-center/task/available',
        method: 'get',
        params: {taskId}
    })
}

//（设计者）删除任务
export function deleteTask(taskId) {
    return request({
        url: '/help-center/task',
        method: 'delete',
        params: {taskId}
    })
}

//发送邀请，向data指定的用户推送问卷邀请,
// data为 type: desc 其中type可以是email、username、phone和studentId desc为查找内容
export function invite(taskId, data) {
    return request({
        url: '/help-center/invite/' + taskId,
        method: 'post',
        data
    })
}

//列出所有互助任务
export function list() {
    return request({
        url: '/help-center/list',
        method: 'get'
    })
}

//根据taskId查找问卷Id
export function getQuestionId(taskId) {
    return request({
        url: '/help-center/task/' + taskId,
        method: 'get'
    })
}

export function existTask(question) {
    return request({
        url: '/help-center/exist/' + question,
        method: 'get'
    })
}
