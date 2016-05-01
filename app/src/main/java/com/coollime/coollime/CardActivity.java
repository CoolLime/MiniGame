package com.coollime.coollime;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class CardActivity extends Activity {
	/** Called when the activity is first created. */

	LinearLayout mainLinear; // ��ü ���̾ƿ�
	LinearLayout[] linearRows; // ���� ��

	ImageButton[] buttons; // �̹��� ��ư �迭
	BitmapDrawable[] bitmapsDrawables; // drawable �迭
	Bitmap[] bitmaps; // ��Ʈ�� �迭
	
	BitmapDrawable bitmapDrawableBack;
	Bitmap bitmapBack;
	
	int[] indexes; // ī���� ������ȣ ���� �迭
	
	int[] tempArr = null; // �ӽ� ���� �迭	
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
		setContentView(R.layout.activity_main);

		tv = (TextView)findViewById( R.id.text );
		mainLinear = (LinearLayout) findViewById(R.id.main_linear); // ��ü ���Ͼ�
		
		tv.setText( R.string.choice1 ); // �޽����� ��µǴ� �ؽ�Ʈ ��
		tv.setTextColor( Color.BLUE );
		tv.setGravity( Gravity.CENTER_HORIZONTAL );

		res = getResources(); // ���ҽ� ���
		indexes = new int[ maximum * maximum ]; // �̹����� ������ȣ�� ����Ѵ�.

		start = 0x7f020002; // ���ҽ� ��ȣ ó��
		end = 0x7f02002f; // ���ҽ� ��ȣ ������
 
		// ���ۺ��� ������ ī���� 8���� ī�带 �̾Ƴ���.
		bitmapDrawableBack = (BitmapDrawable)res.getDrawable( R.drawable.back );
		bitmapBack = bitmapDrawableBack.getBitmap();
		
		// �������� �̹����� �о�´�.
		getRandomImages();
		// ������ ���´�.
		mixIndexes();	
		// �並 ��ĥ�Ѵ�
		setViews();
		// ī�� �޸��� �����ش�.
		recoverAllCards();
		// ������ �ʱ�ȭ
		init();		
		
	}
	
	private void mixIndexes(){
		int temp;
		
		// �������� �ߺ����� ���� �̴´�.
		tempArr = getRandomNumbers(0, maximum * maximum - 1, maximum * maximum);
		temp = 0 ;	
		//indexes �迭�� �������� ������ ����
		for( int i = 0 ; i < indexes.length ; i++ ){
			indexes[ tempArr[ i ] ] = temp;
			temp++;
	
			// ���� ī�尡 �� ���� ������ �Ѵ�.
			if( temp >= maximum * maximum / 2 )
				temp = 0;
		}
	}
	
	private void getRandomImages(){
		
		int temp;
		bitmapsDrawables = null;
		tempArr = null;
		
		// �������� �ߺ����� �̴´�. �� 50������ ī���� maximum * maximum / 2 ���� ī�带 �̴´�.
		tempArr = getRandomNumbers(start, end, maximum * maximum / 2 );
		bitmapsDrawables = new BitmapDrawable[ maximum * maximum / 2 ];

		// ������ �̹����� �ε��Ѵ�.
		temp = 0;
		for (int i = 0; i < bitmapsDrawables.length; i++) {
			bitmapsDrawables[ i ] = (BitmapDrawable) res.getDrawable( tempArr[ temp++ ] 	);
		}

		// ��Ʈ���� ��´�.
		bitmaps = new Bitmap[ maximum * maximum / 2 ];
		for (int i = 0; i < bitmaps.length; i++) {
			bitmaps[ i ] = bitmapsDrawables[i].getBitmap();
		}
	}
	
	private void recoverAllCards(){
		// ī�带 ��� ������ ���·� ���´�.
		for (int i = 0; i < buttons.length; i++){
			buttons[ i ].setImageBitmap( bitmapBack );
		}		
	}
	
	private void recoverCard(){
		
		// ����� ī�带 �ٽ� �������´�.		
		buttons[ selectedIndex[ 0 ] ].setImageBitmap( bitmapBack );
		buttons[ selectedIndex[ 1 ] ].setImageBitmap( bitmapBack );
		
	}
	
	private void setViews(){		
		int temp;
		
		temp = 0;
		
		mainLinear.removeAllViews();
		
		// ��ư ����
		buttons = new ImageButton[maximum * maximum];
		for (int i = 0; i < buttons.length; i++)
			buttons[ i ] = new ImageButton(this);
		
		// ���� �����Ѵ� ���� ���Ͼ��̴�.
		linearRows = new LinearLayout[maximum];
		
		for (int i = 0; i < linearRows.length; i++) {

			linearRows[i] = new LinearLayout(this);
			linearRows[i].setGravity( Gravity.CENTER_HORIZONTAL ); // ��� ����
			linearRows[i].setOrientation( LinearLayout.HORIZONTAL );			
			
			for (int j = 0; j < maximum; j++) {
				// linearLayout �� �̹�����ư�� ��ġ�Ѵ�
				linearRows[i].addView(buttons[temp++]);
			}
			// ���� LinearLayout �� �並 ��ġ�Ѵ�,.
			mainLinear.addView(linearRows[i]);
		}
	}
	
	private void init(){

		// �̺�Ʈ ������ ���
		for( int i = 0 ; i < buttons.length ; i++ ){
			buttons[ i ].setOnClickListener( mClickListener );
		}
		
		
		// selectedindex �� ����ڰ� ������ ī���� index �� ����ִ�.
		// �̹� ���õ� ī�带 �ٽ� �����ߴ��� �Ǵ��Ѵ�.
		selectedIndex =  new int[ 2 ];		
		for( int i = 0 ; i < selectedIndex.length ; i++ ){
			selectedIndex[ i ] = -1;
		}
		
		// correctindexarray �� �̹� ���� ī���� index �� ����ȴ�.
		correctIndexArray = new int[ maximum * maximum ];
		for( int i = 0 ; i < correctIndexArray.length ; i++ ){
			correctIndexArray[ i ] = -1;
		}
		
		// ������ ī���� �̹��� index �� ����ȴ�.
		// �̹����� ���� ��ġ�ϴ��� �Ǵ��Ѵ�,.
		selectedCardIndex = -1;
		
		// bPassible �� true �϶��� ��ưŬ���� �۵��Ѵ�.
		// �̰��� 1 �ʵ��� �����̻����϶� �ٸ� ��ư�� Ŭ���Ǵ°��� ���´�.
		bPassible = true;
		
	}
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage( Message msg ){
			
			switch( msg.what )
			{
			case 0:				// Ʋ�����
				recoverCard();				
			case 1: // ������
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
		
		// �ٽü��� �޴� �߰�
		MenuItem item = menu.add( 0, 1, 0, "�ٽ� ����"); 
		item.setIcon( R.drawable.icon );
		
		return true;
	}
	
	public boolean onOptionsItemSelected( MenuItem item ){
		switch( item.getItemId() )
		{
			case 1:
				
				//�ʱ�ȭ
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
					
					 // �̹� ������ ī���ϰ��					
					if( selectedIndex[ 0 ] == i ){
						tv.setTextColor( Color.RED );
						tv.setText( R.string.already_selected );
						return ;
					}
						
					// �̹� ������ ���� ī���� ���
					if( correctIndexArray[ i ] != -1 ){
						tv.setTextColor( Color.RED );
						tv.setText( R.string.already_correct );
						return ;
					}
					
					// Ŭ���� �̹����� �����ش�.
					buttons[ i ].setImageBitmap( bitmaps[ indexes[ i ] ] );

					// ó��Ŭ���� �̹����̸�
					if( selectedIndex[ 0 ] == -1 ){
						selectedCardIndex = indexes[ i ];
						tv.setTextColor( Color.YELLOW );
						tv.setText( R.string.choice2 ); // �ι�°�� Ŭ�����ּ���
						selectedIndex[ 0 ] = i;
					}												
					else{
						//�ι�° Ŭ���� �̹����̸�
						selectedIndex[ 1 ] = i;
						
						// �̹����� ��ġ�ϸ�
						if( selectedCardIndex == indexes[ i ] ){
							
							tv.setTextColor( Color.BLUE );
							tv.setText( R.string.correct ); // ������ϴ�!
							// �����̹��� index �� �����Ѵ�.
							correctIndexArray[ selectedIndex[ 0 ] ] = 1;
							correctIndexArray[ selectedIndex[ 1 ] ] = 1;
							for( int j = 0 ; j < correctIndexArray.length ; j++ ){
								
								// ������ ���� ī�尡 ���� ���
								if( correctIndexArray[ j ] == -1 ){
									// 1���Ŀ� �ڵ鷯 ����
									mHandler.sendEmptyMessageDelayed( 1 , 1000 );
									return ;									
								}								
							}
							
							tv.setTextColor( Color.BLUE );
							tv.setText( R.string.clear ); // ��� ������ ���
							
							bPassible = false;
							
							return ;
														
							
						}
						else{
							
							tv.setTextColor( Color.RED );
							tv.setText( R.string.wrong ); // Ʋ�Ƚ��ϴ�.
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
		// �� �޼���� start ~ end ������ ������ num ������ŭ�� ������
		// �����ϰ� �ߺ����� �̾Ƴ��ϴ�.
		int size; // ũ���Դϴ�
		int temp; // �ӽú����Դϴ�.

		if (start > end || start == end) // ������ġ�� �� ��ġ���� ũ�ų� ������ �����Դϴ�.
			return null;

		size = end - start + 1; // size �� ũ�⸦ ���մϴ�.

		if (size < num) // size �� ũ�⺸�� ���ϴ� ������ ������ ������ �����Դϴ�.
			return null;

		int[] numbers;
		double[] dRandom;
		int[] result = null;
		
		try {

			numbers = new int[size];
			dRandom = new double[size];
			result = new int[num];

			// ������ ���� size ��ŭ �����մϴ�.
			for (int i = 0; i < dRandom.length; i++) {
				dRandom[i] = Math.random();
			}
			// ����� �Ҵ� ��
			temp = start;
			for (int i = 0; i < numbers.length; i++) {
				numbers[i] = temp++;
			}

			// �������� ������ ���ڸ� �����Ͽ� �����ϴ�.
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
			// ����� �����մϴ�.
			for (int i = 0; i < result.length; i++) {
				result[i] = numbers[i];
			}
		} catch (Exception e) {
		}

		return result;
	}
}
