package kong.qingwei.opencv320;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.kongqw.ObjectDetectingView;
import com.kongqw.ObjectDetector;
import com.kongqw.listener.OnOpenCVLoadListener;
import com.yl.fecedetectdemo.R;

import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ObjectDetectingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private ObjectDetectingView objectDetectingView;
    private ObjectDetector mFaceDetector;
    private ObjectDetector mEyeDetector;
    private ObjectDetector mUpperBodyDetector;
    private ObjectDetector mLowerBodyDetector;
    private ObjectDetector mFullBodyDetector;
    private ObjectDetector mSmileDetector;
    private CascadeClassifier classifier;
    static {
        System.loadLibrary("opencv_java3");
    }

    private String TAG=ObjectDetectingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"---------------------init");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_object_detecting);

        ((RadioButton) findViewById(R.id.rb_face)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rb_eye)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rb_upper_body)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rb_lower_body)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rb_full_body)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rb_smile)).setOnCheckedChangeListener(this);

        objectDetectingView = findViewById(R.id.photograph_view);
// 初始化人脸级联分类器，必须先初始化
        objectDetectingView.onOpenCVLoadSuccess();
        mFaceDetector = new ObjectDetector(getApplicationContext(), R.raw.lbpcascade_frontalface, 6, 0.2F, 0.2F, new Scalar(255, 0, 0, 255));
//        mEyeDetector = new ObjectDetector(getApplicationContext(), R.raw.haarcascade_eye, 6, 0.1F, 0.1F, new Scalar(0, 255, 0, 255));
//        mUpperBodyDetector = new ObjectDetector(getApplicationContext(), R.raw.haarcascade_upperbody, 3, 0.3F, 0.4F, new Scalar(0, 0, 255, 255));
//        mLowerBodyDetector = new ObjectDetector(getApplicationContext(), R.raw.haarcascade_lowerbody, 3, 0.3F, 0.4F, new Scalar(255, 255, 0, 255));
//        mFullBodyDetector = new ObjectDetector(getApplicationContext(), R.raw.haarcascade_fullbody, 3, 0.3F, 0.5F, new Scalar(255, 0, 255, 255));
//        mSmileDetector = new ObjectDetector(getApplicationContext(), R.raw.haarcascade_smile, 10, 0.2F, 0.2F, new Scalar(0, 255, 255, 255));
        findViewById(R.id.radio_group).setVisibility(View.VISIBLE);
        objectDetectingView.enableView();
    }
    private void initClassifier() {
        try {
            InputStream is = getResources()
                    .openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File cascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(cascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            classifier = new CascadeClassifier(cascadeFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 切换摄像头
     *
     * @param view view
     */
    public void swapCamera(View view) {
        objectDetectingView.swapCamera();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_face:
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "人脸检测", Toast.LENGTH_SHORT).show();
                    objectDetectingView.addDetector(mFaceDetector);
                } else {
                    objectDetectingView.removeDetector(mFaceDetector);
                }
                break;
            case R.id.rb_eye:
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "眼睛检测", Toast.LENGTH_SHORT).show();
                    objectDetectingView.addDetector(mEyeDetector);
                } else {
                    objectDetectingView.removeDetector(mEyeDetector);
                }
                break;
            case R.id.rb_upper_body:
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "上半身检测", Toast.LENGTH_SHORT).show();
                    objectDetectingView.addDetector(mUpperBodyDetector);
                } else {
                    objectDetectingView.removeDetector(mUpperBodyDetector);
                }
                break;
            case R.id.rb_lower_body:
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "下半身检测", Toast.LENGTH_SHORT).show();
                    objectDetectingView.addDetector(mLowerBodyDetector);
                } else {
                    objectDetectingView.removeDetector(mLowerBodyDetector);
                }
                break;
            case R.id.rb_full_body:
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "全身检测", Toast.LENGTH_SHORT).show();
                    objectDetectingView.addDetector(mFullBodyDetector);
                } else {
                    objectDetectingView.removeDetector(mFullBodyDetector);
                }
                break;
            case R.id.rb_smile:
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "微笑检测", Toast.LENGTH_SHORT).show();
                    objectDetectingView.addDetector(mSmileDetector);
                } else {
                    objectDetectingView.removeDetector(mSmileDetector);
                }
                break;
            default:
                break;
        }
    }
}
