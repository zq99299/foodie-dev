package cn.mrcode.foodiedev.fs.service;

import cn.mrcode.foodiedev.fs.config.AliossProperties;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author mrcode
 * @date 2021/9/7 22:33
 */
@Service
public class FdfsServiceImpl implements FdfsService {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private AliossProperties aliossProperties;

    @Override
    public String upload(MultipartFile file, String fileExtName) throws IOException {
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileExtName, null);
        return storePath.getFullPath();
    }

    @Override
    public String uploadOSS(MultipartFile file, String fileExtName, String userId) throws IOException {
        // yourEndpoint 填写 Bucket 所在地域对应的 Endpoint。以华东1（杭州）为例，Endpoint 填写为 https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = aliossProperties.getEndpoint();
        // 阿里云账号 AccessKey 拥有所有 API 的访问权限，风险很高。强烈建议您创建并使用 RAM 用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = aliossProperties.getAccessKeyId();
        String accessKeySecret = aliossProperties.getAccessKeySecret();

        // 创建 OSSClient 实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 填写网络流地址。
        InputStream inputStream = file.getInputStream();
        // 依次填写 Bucket 名称（例如 examplebucket ）和 Object 完整路径（例如exampledir/exampleobject.txt）。Object 完整路径中不能包含 Bucket 名称。
        // 构建图片路径：test/<userid>/<userid>.jpg
        String url = "test/" + userId + "/" + userId + "." + fileExtName;
        ossClient.putObject(aliossProperties.getBucket(), url, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        return aliossProperties.getHost() + url;
    }
}
