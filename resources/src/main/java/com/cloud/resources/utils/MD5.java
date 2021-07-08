package com.cloud.resources.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class MD5 {
    public static String getMD5(MultipartFile file) {
        try {
            byte[] uploadBytes = file.getBytes();
            //file->byte[],生成md5
            String md5Hex = DigestUtils.md5Hex(uploadBytes);
            //file->InputStream,生成md5
            String md5Hex1 = DigestUtils.md5Hex(file.getInputStream());
            //对字符串生成md5
            String s = DigestUtils.md5Hex("字符串");
            return md5Hex ;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
