import request from '@/utils/request'

//（问卷填写者）提交答卷
export function submitAnswer(id, data) {
    return request({
        url: '/answer/' + id,
        method: 'post',
        data
    })
}

//（问卷填写者）提交答卷(匿名)
export function submitAnswerAnonymous(id, data) {
    return request({
        url: '/question-anonymous/answer/' + id,
        headers: {
            isToken: false
        },
        method: 'post',
        data
    })
}

//（所有人）获取答卷数据详情
export function answerDetail(answerId) {
    return request({
        url: '/answer/detail/' + answerId,
        method: 'get'
    })
}

//（问卷设计者）获取答卷数据详情
export function answerDetailDoc(docId) {
    return request({
        url: '/answer/detail/doc/' + docId,
        method: 'get'
    })
}

//（所有人）修改答卷数据详情
export function modifyAnswer(answerId, data) {
    return request({
        url: '/answer/modify/' + answerId,
        method: 'post',
        data
    })
}


//（问卷填写/设计者）若答卷不为星标，将答卷设置为星标问卷，否则取消星标
export function starAnswer(answerId) {
    return request({
        url: '/answer/star/' + answerId,
        method: 'get'
    })
}

//（问卷设计者）将答卷设置为无效答卷，不可撤销
export function disableAnswer(answerId) {
    return request({
        url: '/answer/' + answerId,
        method: 'delete'
    })
}

//（问卷设计者）列出问卷的所有有效答卷
export function listQuestionAnswers(questionnaireId) {
    return request({
        url: '/answer/question/' + questionnaireId,
        method: 'get'
    })
}

//（问卷填写者）列出用户填写的所有答卷
export function listUserAnswers(userId, type) {
    return request({
        url: '/answer/user/' + userId,
        method: 'get',
        params: {type}
    })
}

//（问卷设计者）对问卷数据进行查询筛选
export function queryAnswers(questionnaireId, query, type) {
    return request({
        url: '/answer/query/' + questionnaireId,
        method: 'post',
        data: {...query},
        params: {type}
    })
}

//统计问卷的所有有效答卷数量
export function countQuestionAnswers(questionnaireId) {
    return request({
        url: '/answer/count/' + questionnaireId,
        method: 'get'
    })
}

//查找本人受邀填写（answer_status=2）的问卷列表
export function todo() {
    return request({
        url: '/answer/todo/',
        method: 'get'
    })
}
