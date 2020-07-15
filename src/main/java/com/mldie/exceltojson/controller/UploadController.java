package com.mldie.exceltojson.controller;

import com.alibaba.fastjson.JSONObject;
import com.mldie.exceltojson.utils.ExcelUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class UploadController {

    private int i;

    @PostMapping("/uploads")
    public String uploads(MultipartFile[] uploadFiles, HttpServletRequest request) {

        if (uploadFiles == null || uploadFiles.length < 1) {
            return "文件不能为空";
        }


        //2，定义文件的存储路径,
        UUID uuid = UUID.randomUUID();
        String realPath = request.getSession().getServletContext().getRealPath("/uploadFile/" + uuid + "/");
        File dir = new File(realPath);
        if (!dir.isDirectory()) {//文件目录不存在，就创建一个
            dir.mkdirs();
        }
            try {
                String filePathS = "";
                //3，遍历文件数组，一个个上传
                for (int i = 0; i < uploadFiles.length; i++) {
                    MultipartFile file = uploadFiles[i];


                    String name = file.getOriginalFilename();

                    if (name.length() < 6 || !name.substring(name.length() - 5).equals(".xlsx")) {
                        return "文件格式错误";
                    }

//                List<ExcelBean> list = null;
                    InputStream in = file.getInputStream();
                    ArrayList<JSONObject> jsonList = new ArrayList<>();
                    jsonList = ExcelUtils.excelToShopIdList(in);
                    if (jsonList == null || jsonList.size() <= 0) {
                        return "导入数据为空";
                    }


                    //创建一个文件输出流
//                  FileOutputStream out = new FileOutputStream(savePath + "\\" + filename);
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(realPath + name + ".json")), "utf-8"));
                    out.write(jsonList.toString());

                    System.out.println("success");

                    in.close();
                    out.close();

//                try {
//                    for (ExcelBean excel : list) {
//                        System.out.println(excel.toString());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


                }
                //4，返回可供访问的网络路径
                System.out.println(realPath);
                return realPath;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "上传失败";
        }
    }

