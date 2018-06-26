package com.zkxltech.domain;
/**
 * @author: ZhouWei
 * @date:2018年6月25日 下午4:32:13
 */
public class EquipmentParam {
    private Integer model;
    private String uidStr;
    /**答题接收的参数*/
    private Integer isQuickResponse;
    private String answerStr;
    public Integer getModel() {
        return model;
    }
    public void setModel(Integer model) {
        this.model = model;
    }
    public String getUidStr() {
        return uidStr;
    }
    public void setUidStr(String uidStr) {
        this.uidStr = uidStr;
    }
    public Integer getIsQuickResponse() {
        return isQuickResponse;
    }
    public void setIsQuickResponse(Integer isQuickResponse) {
        this.isQuickResponse = isQuickResponse;
    }
    public String getAnswerStr() {
        return answerStr;
    }
    public void setAnswerStr(String answerStr) {
        this.answerStr = answerStr;
    }
}
