package com.example.project.service;

import com.example.project.entity.model.CommonResult;
import com.example.project.entity.model.ListResult;
import com.example.project.entity.model.SingleResult;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    @Getter
    public enum  CommonResponse {
        SUCCESS(0, "성공하였습니다."),
        FAIL(-1, "실패하였습니다.");

        final int code;
        final String msg;

        CommonResponse(int code, String msg) {

            this.code = code;
            this.msg = msg;
        }

    }

    // 단일건 결과를 처리하는 메소드
    public <T> SingleResult<T> getSingleResult(T data) {

        SingleResult<T> result = new SingleResult<>();

        result.setData(data);
        setSuccessResult(result);

        return result;
    }

    // 다중 건 결과를 처리하는 메소드
    public <T> ListResult<T> getListResult(List<T> list) {

        ListResult<T> result = new ListResult<>();

        result.setList(list);
        setSuccessResult(result);

        return result;
    }

    // 성공 결과만 처리하는 메소드
    public CommonResult getSuccessResult() {

        CommonResult result = new CommonResult();

        setSuccessResult(result);

        return result;
    }

    // 실패 결과만 처리하는 메소드
    public CommonResult getFailResult() {

        CommonResult result = new CommonResult();

        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());

        return result;
    }

    public CommonResult getFailResult(int code, String msg) {

        CommonResult result = new CommonResult();

        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);

        return result;
    }

    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메서드
    private void setSuccessResult(CommonResult result) {

        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());

    }

}
