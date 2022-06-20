package com.phone.sts_server;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

public class StsServer {


    /**
     * 模拟服务器获取STS
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("main*****");

        //你的阿里云对象存储上的accessKeyId
        String accessKeyId = "LTAI5tN3cQRzuw1N2KF79fXE";
        //你的阿里云对象存储上的accessKeySecret
        String accessKeySecret = "V4mRjv8OS7ieAeqqoi3PNLOGhSsDfw";

        //构建一个阿里云客户端，用于发起请求。
        //设置调用者（RAM用户或RAM角色）的AccessKey ID和AccessKey Secret。
        DefaultProfile profile = DefaultProfile.getProfile("cn-shenzhen", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        //构造请求，设置参数。关于参数含义和设置方法，请参见《API参考》。
        AssumeRoleRequest request = new AssumeRoleRequest();
        request.setRegionId("cn-shenzhen");
        request.setRoleArn("acs:ram::1864855472139195:role/aliyunosstokengeneratorrole");
        request.setRoleSessionName("AliyunOSSTokenGeneratorRole");

        //发起请求，并得到响应。
        try {
            AssumeRoleResponse response = null;
            response = client.getAcsResponse(request);
            System.out.println("response*****" + new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (com.aliyuncs.exceptions.ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

    }
}