package com.getDocument;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;

public class test {
    /*获取播放地址函数*/
    public static GetPlayInfoResponse getPlayInfo(DefaultAcsClient client) throws Exception {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(
                "1b8213e8b41646789945057ca5e9b35e");
        return client.getAcsResponse(request);
    }

    /**
     * 本地文件上传接口
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param title
     * @param fileName
     */
    private static void testUploadVideo(String accessKeyId, String accessKeySecret, String title, String fileName) {
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);
        // 分片存储：把一个文件分为多个部分进行存储，合在一块就是一个文件
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }

    public static void main(String[] args) throws ClientException {
        // DefaultAcsClient client = initVod.initVodClient("LTAI4GCirUTAXerwExiYpJuN","2gd5WWLGpnbaWVWQFpUhUbQo4XhbC8");
//        GetPlayInfoResponse response=new GetPlayInfoResponse(); // 此处写string时一定要加“”
//        try {
//            response = getPlayInfo(client);
//            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
//            //播放地址
//            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
//                System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
//            }
//            //Base信息
//            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
//        } catch (Exception e) {
//            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
//        }
        testUploadVideo("LTAI4GCirUTAXerwExiYpJuN","2gd5WWLGpnbaWVWQFpUhUbQo4XhbC8","测试上传","D:\\视频下载\\0001.哔哩哔哩-【短片】试制 10秒悬疑短片微电影（索尼a7m3 4k slog2）-4k版本.flv");
    }
}
