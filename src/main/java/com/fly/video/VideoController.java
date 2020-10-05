package com.fly.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("video")
@Slf4j
public class VideoController {

    @Value("${file1.root}")
    private String fileRootOne;

    @Value("${file2.root}")
    private String fileRootTwo;

    @GetMapping("/{type}")
    public String redirectToVideo(@PathVariable Integer type, HttpSession session) {
        log.info("type = {}", type);

        String root = type.equals(0) ? fileRootOne : fileRootTwo;
        session.setAttribute("rootPath", root);
        return root;
    }


    @GetMapping("/list")
    public DirectoryInfo listFiles(@RequestParam String path, HttpSession session) throws IOException {
        log.info("path = {}", path);
        path = path.replaceAll("\\\\", "/");

        String rootPath = (String) session.getAttribute("rootPath");
        if (!path.contains(rootPath)) {
            log.error("路径不正确！返回跟目录。。。");
            path = rootPath;
        }

        List<FileInfo> fileInfoList = Files.list(Paths.get(path))
                .map(this::convertToFileInfo)
                .sorted()
                .collect(Collectors.toList());

        return new DirectoryInfo(path, fileInfoList);
    }


    @GetMapping("/back")
    public DirectoryInfo back(@RequestParam String currentPath, HttpSession session) throws IOException {
        log.info("path = {}", currentPath);
        currentPath = currentPath.replaceAll("\\\\", "/");

        Path path = Paths.get(currentPath);
        Path parent = path.getParent();

        return listFiles(parent.toString(), session);
    }

    private FileInfo convertToFileInfo(Path path) {

        File file = path.toFile();
        String name = file.getName();
        Boolean isDir = file.isDirectory();
        String pathString = path.toString().replaceAll("\\\\", "/");

        return new FileInfo(name, pathString, isDir);
    }

    @Data
    @AllArgsConstructor
    public static class FileInfo implements Comparable<FileInfo> {
        String name;
        String path;
        Boolean isDirectory;

        @Override
        public int compareTo(FileInfo that) {
            if (this.isDirectory && !that.isDirectory) {
                return -1;
            }

            if (!this.isDirectory && that.isDirectory) {
                return 1;
            }

            return this.name.compareTo(that.name);

        }
    }

    @Data
    public static class DirectoryInfo {
        String path;
        List<FileInfo> files;

        public DirectoryInfo(String path, List<FileInfo> files) {
            this.path = path.replaceAll("\\\\", "/");
            this.files = files;
        }
    }
}
