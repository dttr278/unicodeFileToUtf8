package com.dttr.convert.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class BaseController {
    @GetMapping(path = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/convert", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<InputStreamResource> handleFileUpload(MultipartHttpServletRequest request) throws Exception {
        Iterator<String> itrator = request.getFileNames();
        MultipartFile multiFile = request.getFile(itrator.next());
        HttpHeaders responseHeader = new HttpHeaders();
        InputStreamResource inputStreamResource;
        String content = "";
        try {
            // just to show that we have actually received the file
            System.out.println("File Length:" + multiFile.getBytes().length);
            System.out.println("File Type:" + multiFile.getContentType());
            String fileName = multiFile.getOriginalFilename();
            System.out.println("File Name:" + fileName);
            // making directories for our required path.
            byte[] bytes = multiFile.getBytes();
            content = new String(bytes);

            String utf8 = StringEscapeUtils.unescapeJava(content);

            // File file = new File(context.getRealPath("/file/demo.txt"));
            byte[] data = utf8.getBytes("UTF-8");
            // Set mimeType trả về
            responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // Thiết lập thông tin trả về
            responseHeader.set("Content-disposition", "attachment; filename=" + fileName);
            responseHeader.setContentLength(data.length);
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            inputStreamResource = new InputStreamResource(inputStream);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Error while loading the file");
        }
        return new ResponseEntity<InputStreamResource>(inputStreamResource, responseHeader, HttpStatus.OK);
    }
}