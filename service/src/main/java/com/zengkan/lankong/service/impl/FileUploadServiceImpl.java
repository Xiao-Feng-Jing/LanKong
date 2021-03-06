package com.zengkan.lankong.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.zengkan.lankong.config.OssConfig;
import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.service.FileUploadService;
import com.zengkan.lankong.service.OSSUploadService;
import com.zengkan.lankong.utils.RedisUtil;
import com.zengkan.lankong.vo.FileUploadResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/04/14/13:53
 * @Description:
 **/
@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private OssConfig ossConfig;

    @Autowired
    private OSS ossClient;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private OSSUploadService ossUploadService;

    /**
     * 上传文件到阿里云oss
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FileUploadResult> upload(MultipartFile[] files, boolean isImage) {
        //文件新路径
        List<FileUploadResult> list = new CopyOnWriteArrayList<>();
        for (MultipartFile file : files) {
            try {
                list.add(ossUploadService.upload(file.getName(), getFilePath(file.getOriginalFilename(),isImage),file).get());
            } catch (Exception e) {
                throw new MyException(ExceptionEnum.ERROR, list);
            }
        }

        return list;
    }

    /**
     * 删除图片
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String fileName) {
        ossUploadService.delete(modifyFileName(fileName));
    }

    /**
     * 下载文件
     * */
    @Override
    public void download(String fileName, HttpServletResponse response){
        try {
            fileName = modifyFileName(fileName);
            OSSObject ossObject = ossClient.getObject(ossConfig.getALIYUN_OSS_BUCKET_NAME(), fileName);
            //读取文件内容
            InputStream inputStream = ossObject.getObjectContent();
            BufferedInputStream in = new BufferedInputStream(inputStream);
            OutputStream outputStream = response.getOutputStream();
            BufferedOutputStream out = new BufferedOutputStream(outputStream);
            byte[] buffers = new byte[in.available()];
            int len = 0;
            while ((len = in.read(buffers)) != -1) {
                out.write(buffers, 0, len);
            }

            out.close();
            in.close();
        } catch (Exception e) {
            log.error("{}: 下载失败",fileName);
            throw new MyException(ExceptionEnum.DOWNLOAD_FILE_ERROR);
        }
    }


    /**
     * 追加上传文件
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileUploadResult append(MultipartFile file, String fileName){
        FileUploadResult fileUploadResult = new FileUploadResult();

        try {
            //判断是否是第一次上传
            if (fileName == null || fileName.length() == 0){
                fileName = getFilePath(file.getOriginalFilename(),false);
            }else {
                fileName = modifyFileName(fileName);
            }
            InputStream inputStream = create(file);

            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType("text/plain;charset=utf-8");
            AppendObjectRequest appendObjectRequest = new AppendObjectRequest(
                    ossConfig.getALIYUN_OSS_BUCKET_NAME(),
                    fileName,
                    inputStream,meta);
            boolean found = ossClient.doesObjectExist(ossConfig.getALIYUN_OSS_BUCKET_NAME(), fileName);
            //System.out.println(found);
            if (found) {
                // 获取文件的部分元信息。
                SimplifiedObjectMeta objectMeta = ossClient.getSimplifiedObjectMeta(ossConfig.getALIYUN_OSS_BUCKET_NAME()
                        , fileName);
                //System.out.println(objectMeta.getSize());
                appendObjectRequest.setPosition(objectMeta.getSize());
                fileUploadResult.setFilePosition(objectMeta.getSize());
            }else {
                appendObjectRequest.setPosition(0L);
                fileUploadResult.setFilePosition(0L);
            }

            AppendObjectResult appendObjectResult = ossClient.appendObject(appendObjectRequest);
            fileUploadResult.setFileSize(appendObjectResult.getNextPosition());
            fileUploadResult.setName(ossConfig.getALIYUN_OSS_URLPREFIX()+fileName);
            fileUploadResult.setUid(appendObjectRequest.getMetadata().getRequestId());
            fileUploadResult.setStatus("done");
            fileUploadResult.setResponse("SUCCESS");
            redisUtil.del(fileName);
        } catch (IOException e) {
            log.error("{}: 追加上传失败",fileName);
            throw new MyException(ExceptionEnum.UPLOAD_OSS_ERROR);
        }
        return fileUploadResult;
    }

    /**
     * 在一定范围内下载文件
     * */
    @Override
    public String range(String fileName, Long minSize, Long maxSize){
        String str = null;
        str = (String) redisUtil.getString(fileName);
        //使用分布式锁，因为考虑会部署到多台服务器
        if (str == null) {

            String uuid = UUID.randomUUID().toString();
            if (Boolean.FALSE.equals(redisUtil.lock("lock",uuid))){
                //如果加锁失败  返回默认值
                return str;
            }
            fileName = modifyFileName(fileName);

            OSSObject ossObject = null;
            try {
                GetObjectRequest getRequest = new GetObjectRequest(ossConfig.getALIYUN_OSS_BUCKET_NAME(),
                        fileName);
                getRequest.setRange(minSize, maxSize);
                ossObject = ossClient.getObject(getRequest);
            } catch (Exception e) {
                throw new MyException(ExceptionEnum.FILE_NOT_FOUND);
            }finally {
                redisUtil.unlock("lock",uuid);
            }
            //读取文件内容
            try (BufferedReader in = new BufferedReader(new InputStreamReader(ossObject.getObjectContent(), StandardCharsets.UTF_8));){
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                }
                redisUtil.setString(fileName, sb.toString());
                return sb.toString();
            } catch (Exception e) {
                log.error("{}: 文件读取失败", fileName);
                throw new MyException(ExceptionEnum.DOWNLOAD_FILE_ERROR);
            }finally {
                redisUtil.unlock("lock",uuid);
            }
        }
        return str;
    }



    /**
     * 添加文件路径
     * */
    private String getFilePath(String sourceFileName,boolean isImage) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String prefixPath = ossConfig.getALIYUN_OSS_DIR_PREFIX_ONE();
        if (!isImage){
            prefixPath = ossConfig.getALIYUN_OSS_DIR_PREFIX_TWO();
        }
        String dir = prefixPath + sdf.format(new Date())+"/";
        return  dir + System.currentTimeMillis() +
                RandomUtils.nextInt(new Random(),9999) + "." +
                StringUtils.substringAfterLast(sourceFileName, ".");
    }

    /**
     * 解析文件相对路径
     * */
    private String modifyFileName(String fileName){
         String str = StringUtils.substringAfterLast(fileName,ossConfig.getALIYUN_OSS_URLPREFIX());
         if ("".equals(str)){
             return fileName;
         }
         return str;
    }

    /**
     * 修改文件编码为UTF-8
     * */
    private InputStream create(MultipartFile file) throws IOException {
        if (!file.getOriginalFilename().endsWith("txt")) {
            return file.getInputStream();
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String code = getCode(file.getInputStream());
        String code2 = getCode2(file.getInputStream());
        if ("UTF-8".equals(code)) {
            return file.getInputStream();
        } else {
            String str = IOUtils.toString(file.getInputStream(), code2);
            byte[] head = new byte[3];
            head[0] = -17;
            head[1] = -69;
            head[2] = -65;
            outputStream.write(head, 0, 3);
            outputStream.write(str.getBytes(), 0, str.getBytes().length);
            return new ByteArrayInputStream(outputStream.toByteArray());
            /*BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(),code));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream,StandardCharsets.UTF_8));
            String line = null;
            while ((line = br.readLine()) != null){
                bw.write(line+"\r\n");
            }
            bw.close();
            br.close();
            return new ByteArrayInputStream(outputStream.toByteArray());*/
        }
    }

    /*private byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len=0;
        while (-1 != inputStream.read(buffer)){
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }*/

    /**
     * 获取文件详细编码
     * */
    private static String getCode2(InputStream inputStream) throws IOException {
        UniversalDetector detector = new UniversalDetector(null);
        byte[] buf = new byte[4096];
        // (2)
        int nread;
        while ((nread = inputStream.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        // (3)
        detector.dataEnd();
        // (4)

        return detector.getDetectedCharset();
    }

    /**
     * 获取文件简单编码
     * */
    private String getCode(InputStream inputStream) {
        String charsetName = "gbk";
        byte[] head = new byte[3];
        try {
            inputStream.read(head);
            inputStream.close();
            if (head[0] == -1 && head[1] == -2 ) // 0xFFFE
            {
                charsetName = "UTF-16";
            } else if (head[0] == -2 && head[1] == -1 ) // 0xFEFF
            {
                charsetName = "Unicode";// 包含两种编码格式：UCS2-Big-Endian和UCS2-Little-Endian
            } else if(head[0]==-27 && head[1]==-101 && head[2] ==-98) {
                charsetName = "UTF-8"; // UTF-8(不含BOM)
            } else if(head[0]==-17 && head[1]==-69 && head[2] ==-65) {
                charsetName = "UTF-8"; // UTF-8-BOM
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charsetName;
    }
}

