package com.coollime.coollime;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CardActivity extends Activity {
	/** Called when the activity is first created. */

	LinearLayout mainLinear;
	LinearLayout[] linearRows;

	ImageButton[] buttons;
	BitmapDrawable[] bitmapsDrawables;
	Bitmap[] bitmaps;
	
	BitmapDrawable bitmapDrawableBack;
	Bitmap bitmapBack;
	
	int[] indexes;
	
	int[] tempArr = null;
	int maximum = 4;
	int start, end;
	
	int[] correctIndexArray;
	int[] selectedIndex;
	int selectedCardIndex;
	
	Resources res;
	boolean bPassible = true;
	
	TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		int temp = 0;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);

		tv = (TextView)findViewById( R.id.text );
		mainLinear = (LinearLayout) findViewById(R.id.main_linear);
		
		tv.setText( R.string.choice1 );
		tv.setTextColor( Color.BLUE );
		tv.setGravity( Gravity.CENTER_HORIZONTAL );

		res = getResources();
		indexes = new int[ maximum * maximum ];

		start = 0x7f020002;
		end = 0x7f02002f;
 
		bitmapDrawableBack = (BitmapDrawable)res.getDrawable( R.drawable.back );
		bitmapBack = bitmapDrawableBack.getBitmap();
		
		getRandomImages();
		mixIndexes();
		setViews();
		recoverAllCards();
		init();
		
	}
	
	private void mixIndexes(){
		int temp;
		
		tempArr = getRandomNumbers(0, maximum * maximum - 1, maximum * maximum);
		temp = 0 ;	
		for( int i = 0 ; i < indexes.length ; i++ ){
			indexes[ tempArr[ i ] ] = temp;
			temp++;
	
			if( temp >= maximum * maximum / 2 )
				temp = 0;
		}
	}
	
	private void getRandomImages(){
		
		int temp;
		bitmapsDrawables = null;
		tempArr = null;
		
		tempArr = getRandomNumbers(start, end, maximum * maximum / 2 );
		bitmapsDrawables = new BitmapDrawable[ maximum * maximum / 2 ];

		temp = 0;
		for (int i = 0; i < bitmapsDrawables.length; i++) {
			bitmapsDrawables[ i ] = (BitmapDrawable) res.getDrawable( tempArr[ temp++ ] 	);
		}

		bitmaps = new Bitmap[ maximum * maximum / 2 ];
		for (int i = 0; i < bitmaps.length; i++) {
			bitmaps[ i ] = bitmapsDrawables[i].getBitmap();
		}
	}
	
	private void recoverAllCards(){
		for (int i = 0; i < buttons.length; i++){
			buttons[ i ].setImageBitmap( bitmapBack );
		}		
	}
	
	private void recoverCard(){
		
		buttons[ selectedIndex[ 0 ] ].setImageBitmap( bitmapBack );
		buttons[ selectedIndex[ 1 ] ].setImageBitmap( bitmapBack );
		
	}
	
	private void setViews(){		
		int temp;
		
		temp = 0;
		
		mainLinear.removeAllViews();
		
		buttons = new ImageButton[maximum * maximum];
		for (int i = 0; i < buttons.length; i++)
			buttons[ i ] = new ImageButton(this);
		
		linearRows = new LinearLayout[maximum];
		
		for (int i = 0; i < linearRows.length; i++) {

			linearRows[i] = new LinearLayout(this);
			linearRows[i].setGravity( Gravity.CENTER_HORIZONTAL ); // ��� ����
			linearRows[i].setOrientation( LinearLayout.HORIZONTAL );			
			
			for (int j = 0; j < maximum; j++) {
				linearRows[i].addView(buttons[temp++]);
			}
			mainLinear.addView(linearRows[i]);
		}
	}
	
	private void init(){

		for( int i = 0 ; i < buttons.length ; i++ ){
			buttons[ i ].setOnClickListener( mClickListener );
		}
		
		
		selectedIndex =  new int[ 2 ];
		for( int i = 0 ; i < selectedIndex.length ; i++ ){
			selectedIndex[ i ] = -1;
		}
		
		correctIndexArray = new int[ maximum * maximum ];
		for( int i = 0 ; i < correctIndexArray.length ; i++ ){
			correctIndexArray[ i ] = -1;
		}
		
		selectedCardIndex = -1;
		
		bPassible = true;
		
	}
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage( Message msg ){
			
			switch( msg.what )
			{
			case 0:
				recoverCard();				
			case 1:
				selectedIndex[ 0 ] = -1;
				selectedIndex[ 1 ] = -1;
				bPassible = true;
				tv.setTextColor( Color.BLUE );
				tv.setText( R.string.choice1 );
				break;
			}
			
		}
		
	};
	
	public boolean onCreateOptionsMenu( Menu menu ){
		super.onCreateOptionsMenu( menu );
		
		MenuItem item = menu.add( 0, 1, 0, "�ٽ� ����");
		item.setIcon( R.drawable.icon );
		
		return true;
	}
	
	public boolean onOptionsItemSelected( MenuItem item ){
		switch( item.getItemId() )
		{
			case 1:
				
				getRandomImages();
				mixIndexes();					
				setViews();
				recoverAllCards();
				init();
				return true;
		}
		return false;
	}
	
	Button.OnClickListener mClickListener = new View.OnClickListener() {
		// ��ư Ŭ���� ȣ��ȴ�.
		@Override		
		public void onClick(View v) {
			if( bPassible == false )
				return ;
			
			for( int i = 0 ; i < buttons.length ; i++ ){
				if( v.equals( buttons[i] ) ){
					
					 if( selectedIndex[ 0 ] == i ){
						tv.setTextColor( Color.RED );
						tv.setText( R.string.already_selected );
						return ;
					}
						
					if( correctIndexArray[ i ] != -1 ){
						tv.setTextColor( Color.RED );
						tv.setText( R.string.already_correct );
						return ;
					}
					
					buttons[ i ].setImageBitmap( bitmaps[ indexes[ i ] ] );

					if( selectedIndex[ 0 ] == -1 ){
						selectedCardIndex = indexes[ i ];
						tv.setTextColor( Color.YELLOW );
						tv.setText( R.string.choice2 );
						selectedIndex[ 0 ] = i;
					}												
					else{
						selectedIndex[ 1 ] = i;
						
						if( selectedCardIndex == indexes[ i ] ){
							
							tv.setTextColor( Color.BLUE );
							tv.setText( R.string.correct );
							correctIndexArray[ selectedIndex[ 0 ] ] = 1;
							correctIndexArray[ selectedIndex[ 1 ] ] = 1;
							for( int j = 0 ; j < correctIndexArray.length ; j++ ){
								
								if( correctIndexArray[ j ] == -1 ){
									mHandler.sendEmptyMessageDelayed( 1 , 1000 );
									return ;									
								}								
							}
							
							tv.setTextColor( Color.BLUE );
							tv.setText( R.string.clear );
							
							bPassible = false;
							
							return ;
														
							
						}
						else{
							
							tv.setTextColor( Color.RED );
							tv.setText( R.string.wrong );
							mHandler.sendEmptyMessageDelayed( 0 , 1000 );
							bPassible = false;
						}
					}
					break;
				}
			}
		}
	};
	
	

	public static int[] getRandomNumbers(int start, int end, int num) {
		int size;
		int temp;

		if (start > end || start == end)
			return null;

		size = end - start + 1;

		if (size < num)
			return null;

		int[] numbers;
		double[] dRandom;
		int[] result = null;
		
		try {

			numbers = new int[size];
			dRandom = new double[size];
			result = new int[num];

			for (int i = 0; i < dRandom.length; i++) {
				dRandom[i] = Math.random();
			}
			temp = start;
			for (int i = 0; i < numbers.length; i++) {
				numbers[i] = temp++;
			}

			for (int i = 0; i < dRandom.length; i++) {
				for (int j = 0; j < dRandom.length - 1; j++) {
					double tmp = dRandom[j];
					temp = numbers[j];

					if (dRandom[j] > dRandom[j + 1]) {
						dRandom[j] = dRandom[j + 1];
						dRandom[j + 1] = tmp;
						numbers[j] = numbers[j + 1];
						numbers[j + 1] = temp;
					}
				}
			}
			for (int i = 0; i < result.length; i++) {
				result[i] = numbers[i];
			}
		} catch (Exception e) {
		}

		return result;
	}
}
