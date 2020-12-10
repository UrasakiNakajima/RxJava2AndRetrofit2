package com.mobile.rxjava2andretrofit2.login.bean;

public class LoginResponse {

    /**
     * code : 200
     * message : 登录成功
     * data : {"userId":61,"shopId":null,"housekeeperId":3,"userName":"13513313214","nickName":"13513313214","userRole":1,"email":null,"phonenumber":"13513313214","sex":"2","avatar":"https://hdoss001.oss-cn-beijing.aliyuncs.com/userImg.png","status":"0","password":"JdVa0oOqQAr0ZMdtcTwHrQ==","delFlag":"0","loginIp":null,"loginDate":"2020-10-20 15:20:32","createBy":null,"createTime":"2020-08-19 14:34:06","updateBy":null,"updateTime":"2020-08-19 14:34:06","remark":null,"isBindHd":"1","realname":"Erwin"}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userId : 61
         * shopId : null
         * housekeeperId : 3
         * userName : 13513313214
         * nickName : 13513313214
         * userRole : 1
         * email : null
         * phonenumber : 13513313214
         * sex : 2
         * avatar : https://hdoss001.oss-cn-beijing.aliyuncs.com/userImg.png
         * status : 0
         * password : JdVa0oOqQAr0ZMdtcTwHrQ==
         * delFlag : 0
         * loginIp : null
         * loginDate : 2020-10-20 15:20:32
         * createBy : null
         * createTime : 2020-08-19 14:34:06
         * updateBy : null
         * updateTime : 2020-08-19 14:34:06
         * remark : null
         * isBindHd : 1
         * realname : Erwin
         */

        private int userId;
        private Object shopId;
        private int housekeeperId;
        private String userName;
        private String nickName;
        private int userRole;
        private Object email;
        private String phonenumber;
        private String sex;
        private String avatar;
        private String status;
        private String password;
        private String delFlag;
        private Object loginIp;
        private String loginDate;
        private Object createBy;
        private String createTime;
        private Object updateBy;
        private String updateTime;
        private Object remark;
        private String isBindHd;
        private String realname;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public Object getShopId() {
            return shopId;
        }

        public void setShopId(Object shopId) {
            this.shopId = shopId;
        }

        public int getHousekeeperId() {
            return housekeeperId;
        }

        public void setHousekeeperId(int housekeeperId) {
            this.housekeeperId = housekeeperId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getUserRole() {
            return userRole;
        }

        public void setUserRole(int userRole) {
            this.userRole = userRole;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public String getPhonenumber() {
            return phonenumber;
        }

        public void setPhonenumber(String phonenumber) {
            this.phonenumber = phonenumber;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public Object getLoginIp() {
            return loginIp;
        }

        public void setLoginIp(Object loginIp) {
            this.loginIp = loginIp;
        }

        public String getLoginDate() {
            return loginDate;
        }

        public void setLoginDate(String loginDate) {
            this.loginDate = loginDate;
        }

        public Object getCreateBy() {
            return createBy;
        }

        public void setCreateBy(Object createBy) {
            this.createBy = createBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Object updateBy) {
            this.updateBy = updateBy;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public String getIsBindHd() {
            return isBindHd;
        }

        public void setIsBindHd(String isBindHd) {
            this.isBindHd = isBindHd;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }
    }
}
