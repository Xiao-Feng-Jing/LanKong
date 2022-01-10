package com.zengkan.lankong.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/11/27/16:21
 * @Description :
 * @modified By :
 **/
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ExceptionEnum{
    /**
     * 错误的枚举
     * */
    PRICE_CANNOT_BE_NULL(400,"价格不能为空!"),
    CATEGORY_NOT_FOUND(404,"商品分类不存在！"),
    CATEGORY_RECOMMEDN_NOT_FOUND(404, "不存在推荐分类"),
    CONTENT_TYPE_ERROR(400,"文件类型无效！" ),
    SIGNATURE_FILE_ERROR(500, "OSS文件签名失败"),
    UPLOAD_OSS_ERROR(500,"OSS文件上传失败"),
    DOWNLOAD_FILE_ERROR(500,"文件下载失败"),
    INVALID_IMAGE_ERROR(400,"文件内容无效！" ),
    UPLOAD_FAILED_ERROR(500, "文件上传失败！"),
    FILE_NOT_FOUND(400, "文件不存在"),
    CAROUSEL_NOT_FOUND(404, "轮播图删除失败或不存在"),
    SKU_NOT_FOUND(404,"SKU商品不存在！"),
    SPU_NOT_FOUND(404,"商品不存在！" ),
    SAVE_GOOD_ERROR(500,"商品保存失败！"),
    GOODS_UPDATE_ERROR(400,"商品更新失败！" ),
    SPECIFICATION_NOT_FOUND(404, "规格参数模板不存在"),
    SPU_DETAIL_NOT_FOUND(404, "商品详细信息不存在"),
    INVALID_PARAM(400,"无效的参数！" ),
    GOODS_SAVE_ERROR(400,"商品保存失败！" ),
    GOODS_DELETE_ERROR(400, "商品不存在"),
    INVALID_USER_DATA_TYPE(400,"用户数据类型无效！" ),
    INVALID_VERIFY_CODE(400,"验证码校验失败！" ),
    INVALID_USERNAME_PASSWORD(400,"用户名或密码错误！" ),
    INVALID_PASSWORD(400, "密码错误"),
    CREATE_TOKEN_ERROR(500,"用户凭证生成失败！" ),
    INVALID_TOKEN(400, "token 无效！"),
    NO_AUTHORIZED(401,"没有访问权限！" ),
    CREATE_ORDER_ERROR(500,"创建订单失败！" ),
    STOCK_NOT_ENOUGH(400,"库存不足！" ),
    ORDER_NOT_FOUND(404,"订单不存在！" ),
    NOT_FOUND_ORDER(404, "不存在订单！"),
    ORDER_DETAIL_NOT_FOUNT(404,"订单详情不存在！" ),
    ORDER_STATUS_NOT_FOUND(404,"订单状态不存在！" ),
    ALIPAY_ORDER_FAIL(500,"支付二维码生成失败！" ),
    ORDER_STATUS_ERROE(500,"订单状态异常！" ),
    INVALID_ORDER_PARAM(400,"订单参数异常！" ),
    UPDATE_ORDER_STATUS_ERROR(400,"订单状态更新失败！" ),
    ERROR(500, "服务器内部错误");
    private final int code;
    private final String msg;

    ExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ExceptionEnum{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
