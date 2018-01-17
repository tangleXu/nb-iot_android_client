package com.example.nb_iotutility;

import java.io.InputStream;
import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.view.View;

public class MyImageView extends ImageView implements View.OnClickListener {
    /**
     * ����GIF�����Ĺؼ���
     */
    private Movie mMovie;
    /**
     * ���Ʋ��ŵİ�ť
     */
    //private Bitmap mStartButton;

    /**
     * ��¼������ʼ��ʱ��
     */
    private long mMovieStart;

    /**
     * GIFͼƬ�Ŀ��
     */
    private int mImageWidth;

    /**
     * GIFͼƬ�ĸ߶�
     */
    private int mImageHeight;

    /**
     * ͼƬ�Ƿ����ڲ���
     */
    private boolean isPlaying;

    /**
     * �Ƿ������Զ�����
     */
    private boolean isAutoPlay;
    
	public MyImageView(Context context) {
		super(context);
		Log.e("ARIC","Init 1");
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs,0);
		//this(context, attrs, 0);
		Log.e("ARIC","Init 2");
		setImageResLoad(context, attrs, 0);
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		Log.e("ARIC","Init 3");
		 setImageResLoad(context, attrs, defStyleAttr);
	}
    private void setImageResLoad(Context context, AttributeSet attrs, int defStyleAttr)
    {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyImageView );
        Log.e("ARIC","INFO: 000");
        isAutoPlay = typedArray.getBoolean(R.styleable.MyImageView_auto_play, false);
        int resourceID = getResourcesID(typedArray);//R.drawable.cjx;//
        Log.e("ARIC","resourceID��"+resourceID);
        
       
        
        Log.e("ARIC","INFO: 111");
        if (resourceID != 0) {

        	Log.e("ARIC","INFO: 222");
            InputStream inputStream = getResources().openRawResource(resourceID);
        	//InputStream inputStream = getResources().openRawResource(R.drawable.radio_logo);
            //InputStream inputStream = getResources().openRawResource(R.drawable.cjx);
            //��ͼƬ���н���
            mMovie = Movie.decodeStream(inputStream);
            if (mMovie != null) {
                // �������ֵ������null����˵������һ��GIFͼƬ�������ȡ�Ƿ��Զ����ŵ�����
                
                //isAutoPlay = true;
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                mImageWidth = bitmap.getWidth();
                mImageHeight = bitmap.getHeight();
                Log.e("ARIC","LENGTH: "+ mImageWidth);
                bitmap.recycle();
                if (!isAutoPlay) {
                    // ���������Զ����ŵ�ʱ�򣬵õ���ʼ���Ű�ť��ͼƬ����ע�����¼�
                    //mStartButton = BitmapFactory.decodeResource(getResources(),
                     //       R.drawable.ic_launcher);
                	
                    setOnClickListener(this);
                }
            }

        }
        try{
        	typedArray.recycle();
        }catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    /**
     * ͨ��Java���䣬��ȡsrcָ��ͼƬ��Դ����Ӧ��id��
     */
    public int getResourcesID(TypedArray typedArray)
    {
    	Log.e("ARIC","enter getResourceID");
        try {
            Field field = TypedArray.class.getDeclaredField("mValue");
            field.setAccessible(true);
            Log.e("ARIC","setAccessible");
            TypedValue typedValueObject = (TypedValue) field.get(typedArray);
            Log.e("ARIC","field.get(typedArray);");
            return typedValueObject.resourceId;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
        Log.e("ARIC","leave getResourceID");
        return 0;
    }

/*
    @Override
    public void onClick(View v)
    {
        if (v.getId() == getId()) {
            //���ͼƬ��ʼ����
            isPlaying = true;
            invalidate();
        }
    }*/

    @Override
    protected void onDraw(Canvas canvas)
    {
    	Log.e("ARIC","ON DRAW 1");
        if (mMovie == null) {
            // mMovie����null��˵��������ͨ��ͼƬ����ֱ�ӵ��ø����onDraw()����
            super.onDraw(canvas);
            Log.e("ARIC","ON DRAW:IT's a common picture.");
        } else {
        	Log.e("ARIC","IT's gif.");
            //���mMovie������null���Ǿ�˵����gifͼƬ
            if (isAutoPlay) {
                //���������,����palyMovie();
                playMovie(canvas);
                invalidate();
                Log.e("ARIC","Auto play");
            } else {
                //�������Զ����ţ��ж��Ƿ�Ҫ����
                if (isPlaying) {
                    if (playMovie(canvas)) {
                        isPlaying = false;
                    }
                    invalidate();
                } else {
                    // ��û��ʼ���ž�ֻ����GIFͼƬ�ĵ�һ֡��������һ����ʼ��ť
                    mMovie.setTime(0);
                    mMovie.draw(canvas, 0, 0);
                    //int imageX = (mImageWidth - mStartButton.getWidth()) / 2;
                    //int imageY = (mImageHeight - mStartButton.getHeight()) / 2;
                    //canvas.drawBitmap(mStartButton, imageX, imageY, null);
                    Log.e("ARIC","ON DRAW canvas.draw");
                }
            }
        }
    } 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMovie != null) {
            // �����GIFͼƬ����д�趨myImageView�Ĵ�С
            setMeasuredDimension(mImageWidth, mImageHeight);
        }
    }
    public boolean playMovie(Canvas canvas)
    {
    	Log.e("ARIC","playMovie enter");
        long now = android.os.SystemClock.uptimeMillis();
        if (mMovieStart == 0) {
            mMovieStart = now;
        }
        int duration = mMovie.duration();
        if (duration == 0) {
            duration = 1000;
        }
        Log.e("ARIC","#####1111");
        int loadTime = (int) ((now - mMovieStart) % duration);
        Log.e("ARIC","#####2222");
        mMovie.setTime(loadTime);
        Log.e("ARIC","#####3333");
        if(canvas !=null && mMovie != null)
        	mMovie.draw(canvas,0,0);
        Log.e("ARIC","#####4444");
        if ((now - mMovieStart) >= duration) {
            mMovieStart = 0;
            return true;
        }
        Log.e("ARIC","playMovie leave");
        return false;
    }
    public void play(){
    	isPlaying = true;
    	invalidate();
    }
    
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
        //if (arg0.getId() == getId()) 
        {
            //���ͼƬ��ʼ����
            isPlaying = true;
            invalidate();
            Log.e("ARIC","On clicked!");
        }		
	}
    
}
