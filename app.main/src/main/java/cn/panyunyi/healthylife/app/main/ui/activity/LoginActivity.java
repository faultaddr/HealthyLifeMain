package cn.panyunyi.healthylife.app.main.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.panyunyi.healthylife.app.main.R;
import cn.panyunyi.healthylife.app.main.biz.remote.model.MUserEntity;
import cn.panyunyi.healthylife.app.main.biz.remote.service.LoginImpl;
import cn.panyunyi.healthylife.app.main.event.MessageEvent;
import cn.panyunyi.healthylife.app.main.util.ACache;
import cn.panyunyi.healthylife.app.main.util.JellyInterpolator;

public class LoginActivity extends Activity implements View.OnClickListener {
    private static int TAG = 2;
    private final Lock lock = new ReentrantLock();
    @SuppressLint("HandlerLeak")
    public android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(final Message msg) {

            switch (msg.what) {
                case 1:
                    finish();
                    break;
                case 2:
                    Log.i(">>>progress", "handler");
                    MUserEntity user = new MUserEntity();
                    user.setUserName(nameString);
                    user.setUserPassword(psString);
                    Log.i("LoginActivity>>>", nameString);
                    Log.i("LoginActivity>>>", psString);
                    final LoginImpl login = new LoginImpl(user);
                    new Thread() {
                        public void run() {

                            String result = login.login();
                            Log.i("LoginActivity>>>result", result + "");
                            if (result!=null) {
                                MessageEvent messageEvent = new MessageEvent("loginStatus", result);

                                EventBus.getDefault().post(messageEvent);

                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);

                            } else {
                                Snackbar.make(mainToAll, "登录失败请检查密码", Snackbar.LENGTH_LONG);
                            }
                        }
                    }.start();


                    break;

            }
            super.handleMessage(msg);
        }

    };
    @BindView(R.id.remember_pwd)
    RadioButton radioButton;
    //TODO-LIST: 增加注册页面
    private TextView mBtnLogin;
    private View progress;
    private View mInputLayout;
    private float mWidth, mHeight;
    private LinearLayout mName, mPsw;
    private EditText userId;
    private EditText passWord;
    private TextView signUp;
    private RelativeLayout mainToAll;
    private ImageView backButton;
    private Condition notComplete = lock.newCondition();
    private Condition notEmpty = lock.newCondition();
    private String nameString, psString;
    @BindView(R.id.registerFrame)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        final ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.login_activity_init);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        frameLayout.addView(imageView);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(imageView, "scaleX", 1.0f, 1.3f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(imageView, "scaleY", 1.0f, 1.3f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(3000);
        set.playTogether(animatorX, animatorY);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                frameLayout.removeView(imageView);
                initView();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


//        if(getIntent()!=null) {
//            Intent intent=getIntent();
//            Log.i("intent",intent.toString());
//            userId.setText(intent.getStringExtra("userId"));
//            passWord.setText(intent.getStringExtra("passWord"));
//            nameString=intent.getStringExtra("userId");
//            psString=intent.getStringExtra("passWord");
//            Bundle bundle=new Bundle();
//            bundle.putString("userId",nameString);
//            bundle.putString("passWord",psString);
//            Message msg=new Message();
//            msg.what=2;
//            msg.setData(bundle);
//            Log.i("msgg",msg.toString());
//            handler.sendMessage(msg);
//
//        }
//        ACache mCache=ACache.get(getApplication(),"User");
//        if(mCache.getAsString("username")!=null){
//            Message msg=new Message();
//            msg.what=1;
//            handler.sendMessage(msg);
//        }
    }

    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        signUp = (TextView) findViewById(R.id.signup);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        backButton = (ImageView) findViewById(R.id.back);
        backButton.setVisibility(View.INVISIBLE);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        userId = (EditText) findViewById(R.id.userId);
        passWord = (EditText) findViewById(R.id.passWord);
        mainToAll = (RelativeLayout) findViewById(R.id.mainToAll);
        mBtnLogin.setOnClickListener(this);
        signUp.setOnClickListener(this);

        userId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(">>>>>>", s.toString());
                String pwd = ACache.get(getApplicationContext()).getAsString(s.toString() + "pwd");
                if (pwd != null) {
                    passWord.setText(pwd);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (userId.getText() != null) {
                        ACache aCache = ACache.get(getApplicationContext());
                        aCache.put(userId.getText().toString() + "pwd", passWord.getText().toString());
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_login:
                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();
                nameString = userId.getText().toString();
                psString = passWord.getText().toString();
                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);
                //inputAnimator(mInputLayout, mWidth, mHeight);
                mInputLayout.setVisibility(View.INVISIBLE);
                mBtnLogin.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                Log.i(">>>progress", "initiate");
                try {
                    progressAnimator(progress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
                break;
            case R.id.signup:
/*                Intent intent=new Intent(this,RegisterActivity.class);
                startActivity(intent);*/
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        Log.i("daole", "fsdafs");
        nameString = data.getStringExtra("userName");
        psString = data.getStringExtra("passWord");


    }

    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mInputLayout.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);

                try {
                    progressAnimator(progress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void progressAnimator(final View view) throws InterruptedException {
        Log.i(">>>progress", "start");
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();


    }


}
