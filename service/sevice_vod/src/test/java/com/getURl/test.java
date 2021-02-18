package com.getURl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.init.initVod;

public class test {
    /*获取播放凭证函数*/
    public static GetVideoPlayAuthResponse getVideoPlayAuth(DefaultAcsClient client) throws Exception {
        // 创建resquest
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        // 把所要的视频id传入request对象中
        request.setVideoId("1b8213e8b41646789945057ca5e9b35e");
        // 调用初始化里面的方法，传递request
        return client.getAcsResponse(request);
    }
    /*以下为调用示例*/
    public static void main(String[] argv) throws ClientException {
        // 初始化
        DefaultAcsClient client = initVod.initVodClient("LTAI4GCirUTAXerwExiYpJuN","2gd5WWLGpnbaWVWQFpUhUbQo4XhbC8");
        // 创建response
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try {
            response = getVideoPlayAuth(client);
            //播放凭证
            System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
            //VideoMeta信息
            System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }
}
