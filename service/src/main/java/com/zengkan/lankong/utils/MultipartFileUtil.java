package com.zengkan.lankong.utils;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nonnull;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/04/14/18:12
 * @Description:
 **/
@Data
public class MultipartFileUtil implements MultipartFile {

    private String fileName;
    private String file;
    private byte[] bytes;

    @Override
    @Nonnull
    public String getName() {
        //直接使用了原文件的属性
        return "file";
    }

    @Override
    public String getOriginalFilename() {
        //直接使用了原文件的属性
        return fileName+".txt";
    }

    @Override
    public String getContentType() {
        //直接使用了原文件的属性
        return "text/plain";
    }

    @Override
    public boolean isEmpty() {
        return StringUtils.isEmpty(file);
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    @Nonnull
    public byte[] getBytes() {
        return bytes;
    }
    @Override
    @Nonnull
    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }
    @Override
    public void transferTo(@Nonnull File file) {
        try(FileOutputStream out = new FileOutputStream(file)) {
            out.write(bytes);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
