package cn.mrcode.foodiedev.fs.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author mrcode
 * @date 2021/9/7 22:33
 */
public interface FdfsService {
    String upload(MultipartFile file, String fileExtName) throws IOException;
}
