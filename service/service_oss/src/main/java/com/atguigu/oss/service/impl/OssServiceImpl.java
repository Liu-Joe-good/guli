package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.util.ConstantPropertiesUtils;
import lombok.val;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtils.END_POINT;
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String  bucketName= ConstantPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 上传文件流。
            InputStream inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();
            System.out.println(filename);

            // 防止上传文件名相同会覆盖之前文件
            // 解决：UUID
            String uuid= UUID.randomUUID().toString().replace("-",""); // .randomUUID()自动生成主键replace（）把“-”替换为“”
            System.out.println(uuid);
            filename=uuid+filename;

            // 文件过多为了更好区分:
            // 解决：用日期工具栏依赖
            val dateTime = new DateTime().toString("yyyy/MM/dd");
            filename=dateTime+"/"+filename;


            ossClient.putObject(bucketName, filename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();
            //https://edu-2020-7-5.oss-cn-shenzhen.aliyuncs.com/coach2.jpg
            String url="https://"+bucketName+"."+endpoint+"/"+filename;
            return url;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
