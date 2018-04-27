package com.ebusbar.core.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebusbar.core.R;
import com.ebusbar.core.contract.LoginContract;
import com.ebusbar.core.entity.Login;
import com.ebusbar.core.fragment.LoginFragment;
import com.ebusbar.core.presenter.LoginPresenter;
import com.ebusbar.ecore.base.BaseActivity;
import com.ebusbar.ecore.base.BaseResponse;
import com.ebusbar.ecore.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.ObservableTransformer;

public class LoginActivity extends BaseActivity<LoginContract.View, LoginContract.Presenter> implements LoginContract.View {


    @BindView(R.id.main_check_btn)
    Button mMainCheckBtn;
    @BindView(R.id.main_msg_tv)
    TextView mMainMsgTv;
    @BindView(R.id.frame_lay)
    FrameLayout mFrameLay;
    @BindView(R.id.activity_main)
    LinearLayout mActivityMain;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public LoginContract.Presenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public LoginContract.View createView() {
        return this;
    }

    @Override
    public void init() {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.frame_lay, new LoginFragment()).
                commitAllowingStateLoss();

//        findViewById(R.id.main_check_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((TextView) findViewById(R.id.main_msg_tv)).setText("");
//                HashMap<String, String> map = new HashMap<>();
//                map.put("type", "yuantong");
//                map.put("postid", "11111111111");
//                getPresenter().login(map, true, true);
//            }
//        });

    }

    @Override
    public void result(BaseResponse<List<Login>> data) {
        mMainMsgTv.setText(data.getData().toString());
    }

    @Override
    public void setMsg(String msg) {
        ToastUtil.showShortToast(msg);
    }

    @Override
    public <T> ObservableTransformer<T, T> bindLifecycle() {
//        return this.bindUntilEvent(ActivityEvent.PAUSE);//绑定到Activity的pause生命周期（在pause销毁请求）
        return this.bindToLifecycle();//绑定activity，与activity生命周期一样
    }


    @OnClick({R.id.main_msg_tv, R.id.main_check_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_msg_tv:
                break;
            case R.id.main_check_btn:
                mMainMsgTv.setText("");
                HashMap<String, String> map = new HashMap<>();
                map.put("type", "yuantong");
                map.put("postid", "11111111111");
//                map.put("mobile","18328008870");
//                map.put("secret","34ba01d602c88790bbe81a7aca8d3a9f");
//                KLog.e("mobile:  "+"18328008870"+"  secret:   "+"34ba01d602c88790bbe81a7aca8d3a9f");
                getPresenter().login(map, true, true);
                break;
        }
        ToastUtil.showLongToast("234123412341234123412341234");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
