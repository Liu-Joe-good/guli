package com.atguigu.vod.service.Impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.oss.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.uploadVideoService;
import com.atguigu.vod.util.ConstantPropertiesUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.atguigu.vod.util.initVodClient.initVodClient;

@Service
public class uploadVideoServiceImpl implements uploadVideoService {
    @Override
    public String uploadFile(MultipartFile file){
        String accessKeyId=ConstantPropertiesUtils.KEY_ID;
        String accessKeySecret=ConstantPropertiesUtils.KEY_SECRET;
        String fileName=file.getOriginalFilename();
        String title= fileName.substring(0,fileName.lastIndexOf(".")); // 知识点
        // 知识点2，把属性先赋值，然后传入方法内
        // 知识点3，这个接口不用把videoid放入数据库中，只需实现上传功能，然后把id值获取出来，其服务器的接口拿过来用就行
        InputStream inputStream= null;
        String videoId = null;
        try {
            inputStream = file.getInputStream();
            // 调用阿里云上传文件的方法实现上传
            videoId = testUploadStream(accessKeyId,accessKeySecret,title,fileName,inputStream);
            return videoId;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 流式上传接口
     *  @param accessKeyId
     * @param accessKeySecret
     * @param title
     * @param fileName
     * @param inputStream
     * @return
     */
    private static String testUploadStream(String accessKeyId, String accessKeySecret, String title, String fileName, InputStream inputStream) {
        UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        String videoId=null;
        if (response.isSuccess()) {
            videoId=response.getVideoId();
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            videoId=response.getVideoId();
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
        return videoId;
    }


    @Override
    public void removeVideo(String videoSourceId) throws Exception {
        DefaultAcsClient client = null;
        try {
            client = initVodClient(ConstantPropertiesUtils.KEY_ID, ConstantPropertiesUtils.KEY_SECRET);
            DeleteVideoResponse response = new DeleteVideoResponse();
            response = deleteVideo(client,videoSourceId);
            System.out.print("RequestId = " + response.getRequestId() + "\n");
        } catch (ClientException e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
            throw new GuliException(20001, "视频删除失败");
        }
    }

    @Override
    public void removeVideoByChapterId(List<String> id) {
        DefaultAcsClient client = null;
        try {
            client = initVodClient(ConstantPropertiesUtils.KEY_ID, ConstantPropertiesUtils.KEY_SECRET);
            DeleteVideoResponse response = new DeleteVideoResponse();
            // 先转数组，然后再以","分隔开  最后输出结果：“a,b,c”
            String join = StringUtils.join(id.toArray(), ",");
            response = deleteVideo(client,join);
            System.out.print("RequestId = " + response.getRequestId() + "\n");
        } catch (ClientException e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
            throw new GuliException(20001, "视频删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "批量删除视频失败");
        }
    }

    @Override
    public String getPlayauthById(String id)  {
        // 初始化
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, ConstantPropertiesUtils.KEY_ID, ConstantPropertiesUtils.KEY_SECRET);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        // 创建resquest
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        // 把所要的视频id传入request对象中
        request.setVideoId(id);
        // 创建response
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try {
            response = client.getAcsResponse(request);
        }catch (ClientException e){
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        // 获取播放凭证
        String playAuth = response.getPlayAuth();
        System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
        return playAuth;
    }

    // 用于测试
//    public static void main(String[] args) {
//        List<String> strings = new ArrayList<>();
//        strings.add("1");
//        strings.add("2");
//        strings.add("3");
//        String x="";
//        for (int i = 0; i < strings.size(); i++) {
//            if(i+1==strings.size()){
//                x +=strings.get(i);
//            }else {
//                x +=strings.get(i)+",";
//            }
//        }
//        System.out.println(x);
//        Object[] objects = strings.toArray();
//        System.out.println(Arrays.toString(objects));
//        String join = StringUtils.join(objects, ",");
//        System.out.println(join);
//    }


    /**
     * 删除视频
     * @param client 发送请求客户端
     * @return DeleteVideoResponse 删除视频响应数据
     * @throws Exception
     */
    public static DeleteVideoResponse deleteVideo(DefaultAcsClient client,String videoSourceId) throws Exception {
        DeleteVideoRequest request = new DeleteVideoRequest();
        //支持传入多个视频ID，多个用逗号分隔
        request.setVideoIds(videoSourceId);
        return client.getAcsResponse(request);
    }
}
