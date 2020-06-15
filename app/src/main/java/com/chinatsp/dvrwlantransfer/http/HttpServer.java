package com.chinatsp.dvrwlantransfer.http;

import android.text.TextUtils;

import com.chinatsp.dvrwlantransfer.file.DvrFileUtils;
import com.chinatsp.dvrwlantransfer.file.FileBean;
import com.chinatsp.dvrwlantransfer.utils.Constants;
import com.chinatsp.dvrwlantransfer.utils.Logcat;
import com.chinatsp.dvrwlantransfer.utils.StringUtils;
import com.koushikdutta.async.http.body.MultipartFormDataBody;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author chenzuohua
 * Created at 2020/5/28 16:12
 */
public class HttpServer implements HttpServerRequestCallback {
    AsyncHttpServer mServer = new AsyncHttpServer();

    public void start(int port) {
        mServer.get("[\\d\\D]*", this);
        mServer.post("[\\d\\D]*", this);
        mServer.listen(port);
    }

    public void stop() {
        mServer.stop();
    }

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        String uri = request.getPath();
        Logcat.i("uri = " + uri);
        Logcat.i("method = " + request.getMethod());
        switch (request.getMethod()) {
            case "GET":
                File file = null;
                if (uri.startsWith(DvrFileUtils.PHOTO_URL_MID + File.separator)) {// 请求图片
                    file = new File(uri.replace(DvrFileUtils.PHOTO_URL_MID, DvrFileUtils.DVR_PHOTO_FOLDER));
                } else if (uri.startsWith(DvrFileUtils.LOOP_VIDEO_MID + File.separator)) {// 请求循环视频
                    file = new File(uri.replace(DvrFileUtils.LOOP_VIDEO_MID, DvrFileUtils.DVR_LOOP_VIDEO_FOLDER));
                } else if (uri.startsWith(DvrFileUtils.EMERGENCY_VIDEO_MID + File.separator)) {// 请求紧急视频
                    file = new File(uri.replace(DvrFileUtils.EMERGENCY_VIDEO_MID, DvrFileUtils.DVR_EMERGENCY_VIDEO_FOLDER));
                }
                if (file != null) {
                    if (file.exists()) {
                        String path = file.getAbsolutePath();
                        Logcat.i("request file:" + path);
                        if (!file.isDirectory()) {
                            response.sendFile(file);
                        } else {
                            Logcat.e(path + " is a folder!");
                        }
                    } else {
                        Logcat.e(uri + " is not exists!");
                        sendResponse(response, HttpCode.INTERNAL_ERROR, -1, "文件不存在！");
                    }
                }
                break;
            case "POST":
                switch (uri) {
                    case "/dvr/getFileList":
                        handleGetFileList(request, response);
                        break;
                    case "/dvr/deleteFile":
                        handleDeleteFile(request, response);
                        break;
                    case "/dvr/saveFile":
                        handleSaveFile(request, response);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取文件列表
     * @param request
     * @param response
     * @return
     */
    private void handleGetFileList(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        final HashMap<String, String> map = new HashMap<>(3);
        MultipartFormDataBody body = (MultipartFormDataBody) request.getBody();
        body.setMultipartCallback(part -> body.setDataCallback((emitter, bb) -> {
            try {
                String partName = part.getName();
                Logcat.i("partName = " + partName);
                if (!TextUtils.isEmpty(partName)) {
                    String value = URLDecoder.decode(new String(bb.getAllByteArray()), "UTF-8");
                    Logcat.i("value = " + value);
                    if (!TextUtils.isEmpty(value)) {
                        if (StringUtils.isPositiveNum(value)) {
                            switch (partName) {
                                case "pageIndex":
                                case "pageSize":
                                case "fileType":
                                    map.put(partName, value);
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            Logcat.e("value is not a positive number!");
                            sendResponse(response, HttpCode.BAD_REQUEST, -1, "请求参数错误！");
                        }
                    } else {
                        Logcat.e("value is empty!");
                        sendResponse(response, HttpCode.BAD_REQUEST, -1, "请求参数错误！");
                    }
                } else {
                    Logcat.e("partName is empty!");
                    sendResponse(response, HttpCode.BAD_REQUEST, -1, "请求参数错误！");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Logcat.e("handleGetFileList, UnsupportedEncodingException");
                sendResponse(response, HttpCode.INTERNAL_ERROR, -1, "请求参数解析失败！");
            }
            bb.recycle();
        }));
        request.setEndCallback(ex -> {
            String pageIndexStr = map.get("pageIndex");
            int pageIndex = -1;
            if (!TextUtils.isEmpty(pageIndexStr)) {
                pageIndex = Integer.valueOf(pageIndexStr);
            }

            String pageSizeStr = map.get("pageSize");
            int pageSize = -1;
            if (!TextUtils.isEmpty(pageSizeStr)) {
                pageSize = Integer.valueOf(pageSizeStr);
            }

            String fileTypeStr = map.get("fileType");// 0：图片 1：视频 2：紧急视频
            int fileType = -1;
            if (!TextUtils.isEmpty(fileTypeStr)) {
                fileType = Integer.valueOf(fileTypeStr);
            }

            Logcat.i("pageIndex = " + pageIndex + ", pageSize = " + pageSize + ", fileType = " + fileType);
            if (pageIndex != -1 && pageSize != -1) {
                switch (fileType) {
                    case Constants.FILE_TYPE_PHOTO:
                    case Constants.FILE_TYPE_LOOP_VIDEO:
                    case Constants.FILE_TYPE_EMERGENCY_VIDEO:
                        LinkedHashMap<String, List<FileBean>> fileMap = DvrFileUtils.getFileListByGroup(
                                fileType, pageIndex, pageSize);
                        if (fileMap != null && !fileMap.isEmpty()) {
                            JSONObject retObject = new JSONObject();

                            JSONArray dataArray = new JSONArray();
                            fileMap.forEach((date, itemList) -> {
                                final JSONArray fileArray = new JSONArray();
                                itemList.forEach((fileBean) -> fileArray.put(fileBean.toJson()));
                                dataArray.put(fileArray);
                            });

                            try {
                                retObject.put("data", dataArray);
                                retObject.put("statue", 0);
                                retObject.put("message", "请求成功！");
                                sendResponse(response, retObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Logcat.e("retObject pack failed!");
                                sendResponse(response, HttpCode.INTERNAL_ERROR, -1, "返回结果封装失败！");
                            }
                        } else {
                            Logcat.e(fileMap == null ? "fileMap = null" : "fileMap is empty");
                            sendResponse(response, HttpCode.INTERNAL_ERROR, -1, "列表无数据！");
                        }
                        break;
                    default:
                        Logcat.e("fileType is invalid!");
                        sendResponse(response, HttpCode.BAD_REQUEST, -1, "文件类型错误！");
                        break;
                }
            } else {
                sendResponse(response, HttpCode.BAD_REQUEST, -1, "请求参数错误！");
            }
        });
    }

    /**
     * 删除文件
     * @param request
     * @param response
     * @return
     */
    private void handleDeleteFile(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        final HashMap<String, String> map = new HashMap<>(2);
        MultipartFormDataBody body = (MultipartFormDataBody) request.getBody();
        body.setMultipartCallback(part -> body.setDataCallback((emitter, bb) -> {
            try {
                String partName = part.getName();
                Logcat.i("partName = " + partName);
                if (!TextUtils.isEmpty(partName)) {
                    String value = URLDecoder.decode(new String(bb.getAllByteArray()), "UTF-8");
                    Logcat.i("value = " + value);
                    if (!TextUtils.isEmpty(value)) {
                        switch (partName) {
                            case "ids":
                                map.put("ids", value);
                                break;
                            case "fileType":
                                if (StringUtils.isPositiveNum(value)) {
                                    map.put("fileType", value);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Logcat.e("handleDeleteFile, UnsupportedEncodingException");
                sendResponse(response, HttpCode.INTERNAL_ERROR, -1, "请求参数解析失败！");
            }
            bb.recycle();
        }));
        request.setEndCallback(ex -> {
            String ids = map.get("ids");// 删除文件支持批量删除，id以“,”分割
            String[] idArr = null;
            if (!TextUtils.isEmpty(ids)) {
                idArr = ids.split(",");
            }

            String fileTypeStr = map.get("fileType");// 0：图片 1：视频 2：紧急视频
            int fileType = -1;
            if (!TextUtils.isEmpty(fileTypeStr)) {
                fileType = Integer.valueOf(fileTypeStr);
            }

            Logcat.i("ids = " + ids + ", fileType = " + fileType);
            if (idArr != null && idArr.length > 0) {
                switch (fileType) {
                    case Constants.FILE_TYPE_PHOTO:
                    case Constants.FILE_TYPE_LOOP_VIDEO:
                    case Constants.FILE_TYPE_EMERGENCY_VIDEO:
                        JSONObject retObject = new JSONObject();
                        try {
                            retObject.put("data", new JSONArray());
                            boolean result = DvrFileUtils.deleteFiles(fileType, idArr);
                            retObject.put("statue", result ? 0 : -1);
                            retObject.put("message", result ? "删除成功！" : "待删除文件不存在！");
                            sendResponse(response, result ? HttpCode.OK : HttpCode.BAD_REQUEST, retObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Logcat.e("retObject pack failed!");
                            sendResponse(response, HttpCode.INTERNAL_ERROR, -1, "返回结果封装失败！");
                        }
                        break;
                    default:
                        Logcat.e("fileType is invalid!");
                        sendResponse(response, HttpCode.BAD_REQUEST, -1, "文件类型错误！");
                        break;
                }
            } else {
                Logcat.e("no ids in request!");
                sendResponse(response, HttpCode.BAD_REQUEST, -1, "未指明待删除文件id！");
            }
        });
    }

    /**
     * 保存文件（只有循环视频有该功能）
     * @param request
     * @param response
     * @return
     */
    private void handleSaveFile(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        final HashMap<String, String> map = new HashMap<>(2);
        MultipartFormDataBody body = (MultipartFormDataBody) request.getBody();
        body.setMultipartCallback(part -> body.setDataCallback((emitter, bb) -> {
            try {
                String partName = part.getName();
                Logcat.i("partName = " + partName);
                if (!TextUtils.isEmpty(partName)) {
                    String value = URLDecoder.decode(new String(bb.getAllByteArray()), "UTF-8");
                    Logcat.i("value = " + value);
                    if (!TextUtils.isEmpty(value)) {
                        switch (partName) {
                            case "id":
                                map.put("id", value);
                                break;
                            case "fileType":
                                if (StringUtils.isPositiveNum(value)) {
                                    map.put("fileType", value);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Logcat.e("handleSaveFile, UnsupportedEncodingException");
                sendResponse(response, HttpCode.INTERNAL_ERROR, -1, "请求参数解析失败！");
            }
            bb.recycle();
        }));
        request.setEndCallback(ex -> {
            String id = map.get("id");

            String fileTypeStr = map.get("fileType");// 0：图片 1：视频 2：紧急视频
            int fileType = -1;
            if (!TextUtils.isEmpty(fileTypeStr)) {
                fileType = Integer.valueOf(fileTypeStr);
            }

            Logcat.i("id = " + id + ", fileType = " + fileType);
            if (!TextUtils.isEmpty(id)) {
                if (DvrFileUtils.isVideo(id)) {// 得是视频文件
                    if (fileType == Constants.FILE_TYPE_LOOP_VIDEO) {// 循环视频
                        if (new File(DvrFileUtils.DVR_LOOP_VIDEO_FOLDER, id).exists()) {
                            JSONObject retObject = new JSONObject();
                            try {
                                retObject.put("data", new JSONArray());
                                boolean result = DvrFileUtils.saveFileToEmergency(id);
                                retObject.put("statue", result ? 0 : -1);
                                retObject.put("message", result ? "保存成功！" : "保存失败！");
                                sendResponse(response, result ? HttpCode.OK : HttpCode.INTERNAL_ERROR, retObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Logcat.e("retObject pack failed!");
                                sendResponse(response, HttpCode.INTERNAL_ERROR, -1, "返回结果封装失败！");
                            }
                        } else {
                            Logcat.e("file is not exists!");
                            sendResponse(response, HttpCode.BAD_REQUEST, -1, "文件不存在！");
                        }
                    } else {
                        Logcat.e("fileType is not loop video!");
                        sendResponse(response, HttpCode.BAD_REQUEST, -1, "文件类型错误！");
                    }
                } else {
                    Logcat.e("file is not a video!");
                    sendResponse(response, HttpCode.BAD_REQUEST, -1, "文件id非视频！");
                }
            } else {
                Logcat.e("no id in request!");
                sendResponse(response, HttpCode.BAD_REQUEST, -1, "未指明待保存文件id！");
            }
        });
    }

    /**
     * 发送响应信息给Client端
     * @param response 反馈对象
     * @param json     反馈数据
     */
    private void sendResponse(AsyncHttpServerResponse response, JSONObject json) {
        sendResponse(response, -1, json);
    }

    /**
     * 发送响应信息给Client端
     * @param response 反馈对象
     * @param code     状态码，-1时为默认200
     * @param json     反馈数据
     */
    private void sendResponse(AsyncHttpServerResponse response, int code, JSONObject json) {
        // *表示允许所有域名的脚本访问该资源，否则允许特定的域名访问
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        if (code != -1) {
            response.code(code);
        }
        response.send(json);
        Logcat.i("response json--->" + json.toString());
    }

    /**
     * 发送响应信息给Client端
     * @param response 反馈对象
     * @param code     状态码，-1时为默认200
     * @param message  反馈字符串
     */
    private void sendResponse(AsyncHttpServerResponse response, int code, int statue, String message) {
        // *表示允许所有域名的脚本访问该资源，否则允许特定的域名访问
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        if (code != -1) {
            response.code(code);
        }
        JSONObject json = new JSONObject();
        try {
            json.put("statue", statue);
            json.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        response.send(json);
        Logcat.i("response json--->" + json.toString());
    }
}
