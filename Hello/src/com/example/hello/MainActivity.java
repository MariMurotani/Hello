package com.example.hello;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleLayoutGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;
import org.andengine.entity.util.*;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.input.touch.TouchEvent;

import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class MainActivity extends SimpleLayoutGameActivity implements IOnSceneTouchListener{
	// 画面サイズを設定
	public static final int CAMERA_WIDTH = 480;
	public static final int CAMERA_HEIGHT = 800;
	private final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
	private ITextureRegion mBackgroundTextureRegion, mTowerTextureRegion, mRing1, mRing2, mRing3;
	
	int imgTypeCount = 5;
	ArrayList region = new ArrayList();
	
	int numRowX = 4;
	int numRowY = 6;
	private int spWidth = 102;
	private int spXMargin = (CAMERA_WIDTH -spWidth*numRowX)/2;
	private Sprite[][] items = new Sprite[numRowY][numRowX];

	private BitmapTextureAtlas bitmapTextureAtlas;
	private TextureRegion testRegion;
	
	public void MainActivity(){
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// ゲームのエンジンを初期化
		EngineOptions eo = new EngineOptions(true,
				ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), camera);

		return eo;
	}

	@Override
	protected void onCreateResources() {
		 loadImageResources();
	}

	ArrayList<Sprite> SpriteList;
	@Override
	protected Scene onCreateScene() {
		
		// シーンを作る
		final Scene scene = new Scene();
		scene.setBackground(new Background(0, 0, 0));
		
		for(int x = 0 ; x < numRowX ; x++){
			for(int y = 0 ; y < numRowY ; y++){
				int rand = (int) Math.round(Math.random()*(imgTypeCount-1));
				//Log.v("murotani","rand: " + String.valueOf(rand));
				
				Sprite sp = getImageSprite(rand);
				
				if(sp != null)
				{
					items[y][x] = sp;
				}
				
			}
		}
		
		for(int x = 0 ; x < numRowX ; x++){
			for(int y = 0 ; y < numRowY ; y++){
				try{
					Sprite sp = (Sprite)items[y][x];
					sp.setPosition(spXMargin+spWidth*x, spWidth*y);
					scene.registerTouchArea(sp);
					scene.attachChild(sp);
				}catch(Exception e){
					Log.v("murotani","attacch err: " + e.getMessage());
				}
			}
		}
		
		scene.registerUpdateHandler(new TimerHandler(1.5f, true,
				new ITimerCallback() {

					public void onTimePassed(TimerHandler pTimerHandler) {

						/** 処理したい内容を記述 **/
						
					}

				}));

		scene.setOnSceneTouchListener(this);
		
		return scene;
	}

	@Override
	protected int getLayoutID() {
		// ActivityのレイアウトのIDを返す
		return R.layout.activity_main;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		// SceneがセットされるViewのIDを返す
		return R.id.renderview;
	}

	private void loadImageResources(){
		for(int i = 0 ; i < imgTypeCount; i++){
			BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),100,100, TextureOptions.BILINEAR);
			TextureRegion tr = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas,this,"img/neko"+String.valueOf(i)+".png", 0, 0);
			bitmapTextureAtlas.load();
			region.add(tr);
		}

	}
	
	private boolean moving = false;
	private int moveFromX = 0;
	private int moveFromY = 0;
	
	private Sprite getImageSprite(int index){
		Sprite mSprite = null;
        // Spriteに貼り付けるテクスチャーの生成
		if(index <region.size()){
			TextureRegion reg = (TextureRegion)region.get(index);
			
			// Spriteの作成と画面中央に配置
	        mSprite = new Sprite(0, 0, reg,getVertexBufferObjectManager()){
	        	@Override
	    		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
	    			super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	    			
	    			//int xIndex = (int) Math.ceil((pTouchAreaLocalX - spXMargin)/numRowX);
	    			//int yIndex = (int) Math.ceil(pTouchAreaLocalY/numRowY);
	    			
	    			/*this.setScale(1.2f);
	    			this.setAlpha(0.8f);
	    		
	    			Log.v("murotani","index: " + xIndex + ", " + yIndex);
	    			*/
	    			
	    			return false;
	    		}
		    	
	        };
		}else{
			Log.v("murotani","no key: " + String.valueOf(index));
		}
		return mSprite;
	}

	Sprite currentSprite;
	float startX = 0;
	float startY = 0;
	float movingPosX = 0;
	float movingPosY = 0;
	int movingDirectionC =0;
	int movingX =0;
	int movingY =0;
	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		float[] localPos = pScene.convertSceneToLocalCoordinates(pSceneTouchEvent.getX(),pSceneTouchEvent.getY());
		final int xIndex = (int) Math.ceil((localPos[0] - spXMargin)/(spWidth*numRowX))-1;
		final int yIndex = (int) Math.ceil(localPos[1]/(spWidth*numRowY))-1;
		
		/*Log.v("murotani","x,y (global): " + String.valueOf(pSceneTouchEvent.getX()) + " , " + String.valueOf(pSceneTouchEvent.getY()));
		Log.v("murotani","x,y (local): " + String.valueOf(localPos[0]) + " , " + String.valueOf(localPos[1]));
		Log.v("murotani","x,y (local): " + String.valueOf(xIndex) + " , " + String.valueOf(yIndex));
		*/
		
		switch (pSceneTouchEvent.getAction()) {
		    case MotionEvent.ACTION_DOWN:
		    	movingDirectionC =0;
		    	movingX = 0;
		    	movingY = 0;
		    	movingPosX = pSceneTouchEvent.getX();
		    	movingPosY = pSceneTouchEvent.getY();
				this.runOnUiThread(new Runnable() {
		            @Override
		            public void run() {
		                //Toast.makeText(MainActivity.this, "onSceneTouchEvent:ACTION_DOWN", Toast.LENGTH_SHORT).show();
				    	Log.v("murotani","onSceneTouchEvent: ACTION_DOWN");
						
						try{
							Sprite sp = items[yIndex][xIndex];
							if(sp != null){
								startX = sp.getX();
								startY = sp.getY();
								currentSprite = sp;
								sp.setAlpha(0.8f);
								sp.setScale(1.2f);
							}
						}catch(Exception e){
							Log.v("murotani",e.getMessage());
						}
		            }
		        });
		        break;
		    case MotionEvent.ACTION_MOVE:
		    	
		    	//	うごいているほうこうをきめる
		    	if(movingDirectionC < 3){
		    		float diffX = Math.abs(movingPosX - pSceneTouchEvent.getX());
		    		float diffY = Math.abs(movingPosY - pSceneTouchEvent.getY());
		    		Log.v("murotani","move diff X: " + movingPosX + "- "  + pSceneTouchEvent.getX());
		    		Log.v("murotani","move diff Y: " + movingPosY + "- "  + pSceneTouchEvent.getY());
					//Log.v("murotani","move diff: " + diffX + " , " + diffY);

					if(diffY < diffX){
						movingX++;
					}else{
						movingY--;
					}
					movingDirectionC++;
					movingPosX += pSceneTouchEvent.getX();
					movingPosY += pSceneTouchEvent.getY();
					return false;
		    	}
				this.runOnUiThread(new Runnable() {
		            @Override
		            public void run() {
		                //Toast.makeText(MainActivity.this, "onSceneTouchEvent:ACTION_MOVE", Toast.LENGTH_SHORT).show();
				    	Log.v("murotani","onSceneTouchEvent: ACTION_MOVE");
						Log.v("murotani","moving x,y " + String.valueOf(movingX) + " , " + String.valueOf(movingY));
						if(movingY < movingX){
							currentSprite.setPosition(pSceneTouchEvent.getX(), currentSprite.getY());
						}else{
							currentSprite.setPosition(currentSprite.getX(), pSceneTouchEvent.getY()- spWidth/2);
						}
		            }
		        });
		        break;
		    case MotionEvent.ACTION_UP:
				
		        this.runOnUiThread(new Runnable() {
		            @Override
		            public void run() {
		                //Toast.makeText(MainActivity.this, "onSceneTouchEvent:ACTION_UP", Toast.LENGTH_SHORT).show();
				    	Log.v("murotani","onSceneTouchEvent: ACTION_UP");
						Log.v("murotani","x,y: " + String.valueOf(pSceneTouchEvent.getX()) + " , " + String.valueOf(pSceneTouchEvent.getY()));
						currentSprite.setScale(1.0f);
						currentSprite.setAlpha(1.0f);
						currentSprite.setPosition(startX,startY);
		            }
		        });
		        break;
		}
		
		
		movingPosX += pSceneTouchEvent.getX();
		movingPosY += pSceneTouchEvent.getY();

		 return false;
	}

}
