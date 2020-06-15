package com.chinatsp.dvrwlantransfer.file;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author chenzuohua
 * Created at 2020/5/20 18:01
 */
public class FileBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 文件 ID */
    private String id;
    /** 文件名称 */
    private String fileName;
    /** 文件大小 */
    private long fileSize;
    /** 文件缩略图 */
    private String thumbnailPath;
    /** 文件路径 */
    private String filePath;
    /** 文件类型（0、图片；1、视频；2、紧急视频）*/
    private int fileType;
    /** 创建时间 */
    private long createTime;
    /** 创建日期 */
    private String createDate;

    public FileBean(String id, String fileName, long fileSize, String thumbnailPath, String filePath,
                    int fileType, long createTime, String createDate) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.thumbnailPath = thumbnailPath;
        this.filePath = filePath;
        this.fileType = fileType;
        this.createTime = createTime;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
            object.put("fileName", fileName);
            object.put("fileSize", fileSize);
            object.put("thumbnailPath", thumbnailPath);
            object.put("filePath", filePath);
            object.put("fileType", fileType);
            object.put("createTime", createTime);
            // 协议中没有createDate
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
