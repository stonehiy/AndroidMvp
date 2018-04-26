package com.ebusbar.core.contract;

import com.ebusbar.ecore.base.BasePresenter;
import com.ebusbar.ecore.base.BaseResponse;
import com.ebusbar.ecore.base.BaseView;
import com.ebusbar.core.entity.Login;

import java.util.HashMap;
import java.util.List;

import io.reactivex.ObservableTransformer;

/**
 * 作者：senon on 2017/12/27 10:30
 * 邮箱：a1083911695@163.com
 * LoginContract  V 、P契约类
 */
public interface LoginContract {

    interface View extends BaseView {

        void result(BaseResponse<List<Login>> data);

        void setMsg(String msg);

        <T> ObservableTransformer<T, T> bindLifecycle();

    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void login(HashMap<String, String> map, boolean isDialog, boolean cancelable);

    }
}
